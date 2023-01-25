package com.harana.sdk.backend.models.flow

import com.harana.sdk.backend.models.flow

import java.net.Inet4Address
import java.net.NetworkInterface
import java.util.UUID
import scala.sys.process.Process
import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.scalatest.concurrent.Eventually._
import org.scalatest.time.SpanSugar._
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrameBuilder
import com.harana.sdk.backend.models.flow.filesystemclients.LocalFileSystemClient
import com.harana.sdk.shared.BuildInfo
import com.harana.sdk.shared.models.flow.ExecutionMode
import com.harana.spark.SparkSQLSession

object StandaloneSparkClusterForTests {

  private var sparkMasterAddress: Option[String] = None

  private val runId = UUID.randomUUID().toString.substring(0, 8)

  // This env is used within docker-compose.yml and allows multiple instances of the cluster to run simultaneously
  private val clusterIdEnv = "CLUSTER_ID" -> runId

  private val clusterManagementScript = "docker/spark-standalone-cluster-manage.sh"

  private val anIpAddress = {
    import collection.JavaConverters._

    NetworkInterface.getNetworkInterfaces.asScala.flatMap {
      _.getInetAddresses.asScala.map {
        case ip4: Inet4Address => Some(ip4.getHostAddress)
        case _ => None
      }.filter(_.nonEmpty)
    }.next().get
  }

  private def address(container: String, port: Int) = {
    def dockerInspect(container: String, format: String) =
      Process(Seq("docker", "inspect", "--format", format, s"$container-$runId")).!!.stripLineEnd

    val localPortFmt = "{{(index (index .NetworkSettings.Ports \"" + port + "/tcp\") 0).HostPort}}"
    s"$anIpAddress:${dockerInspect(container, localPortFmt)}"
  }

  def startDockerizedCluster() = {
    Process(Seq(clusterManagementScript, "up", sparkVersion), None, clusterIdEnv).!!

    // We turn off the safe mode for hdfs - we don't need it for testing.
    // The loop is here because we don't have control over when the docker is ready for this command.
    eventually(timeout(60.seconds), interval(1.second)) {
      val exec = Seq("docker", "exec", "-u", "root")
      val cmd  = Seq("/usr/local/hadoop/bin/hdfs", "dfsadmin", "-safemode", "leave")
      Process(exec ++ Seq(s"hdfs-$runId") ++ cmd, None, clusterIdEnv).!!
    }

    sparkMasterAddress = Some(address("sparkMaster", 7077))
  }

  def stopDockerizedCluster() =
    Process(Seq(clusterManagementScript, "down", sparkVersion), None, clusterIdEnv).!!

  lazy val executionContext: ExecutionContext = {
    import org.scalatestplus.mockito.MockitoSugar._

    System.setProperty("HADOOP_USER_NAME", "root")

    val sparkConf: SparkConf = new SparkConf()
      .setMaster(s"spark://${sparkMasterAddress.get}")
      .setAppName("TestApp")
      .setJars(
        Seq(s"./flow/target/scala-$majorScalaVersion/seahorse-executor-flow-assembly-${BuildInfo.version}.jar")
      )
      .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
      .registerKryoClasses(Array())
      .set("spark.executor.cores", "1")
      .set("spark.cores.max", "4")
      .set("spark.executor.memory", "512m")



    val sparkContext    = new SparkContext(sparkConf)
    sparkContext.setLogLevel("WARN")
    // After Spark writes all parquet file parts, it reads footers of all of them, merges and writes a "summary".
    // Since this is performed from the driver, it needs to be able to connect to the data node.
    // In our (dockerized) setup it isn't and this is why we turn this option off. It's off in Spark 2.0 by default.
    sparkContext.hadoopConfiguration.set("parquet.enable.summary-metadata", "false")
    val sparkSQLSession = new SparkSQLSession(sparkContext)

    val inferContext = MockedInferContext(dataFrameBuilder = DataFrameBuilder(sparkSQLSession))

    flow.ExecutionContext(
      sparkContext,
      sparkSQLSession,
      inferContext,
      ExecutionMode.Batch,
      LocalFileSystemClient(),
      "/tmp",
      "/tmp/library",
      mock[InnerWorkflowExecutor],
      mock[ContextualDataFrameStorage],
      None,
      new MockedContextualCodeExecutor
    )
  }

  private lazy val majorScalaVersion = util.Properties.versionNumberString.split('.').dropRight(1).mkString(".")

  private lazy val sparkVersion = org.apache.spark.SPARK_VERSION

}
