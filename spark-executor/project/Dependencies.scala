import sbt._
import org.portablescala.sbtplatformdeps.PlatformDepsPlugin.autoImport._

object Dependencies {

  // AzureCosmosDb - https://github.com/Azure/azure-cosmosdb-spark
  val azureCosmosDb = List(
    "com.microsoft.azure" % "azure-cosmosdb-spark_2.4.0_2.11" % "3.7.0"
  )

  // AzureKusto - https://github.com/Azure/azure-kusto-spark
  val azureKusto = List(
    "com.microsoft.azure.kusto" % "kusto-spark_2.4_2.11" % "2.9.3"
  )

  // BigQuery - https://github.com/GoogleCloudDataproc/spark-bigquery-connector
  val bigQuery = List(
    "com.google.cloud.spark" %% "spark-bigquery-with-dependencies" % "0.22.2"
  )

  // Cassandra - https://github.com/datastax/spark-cassandra-connector
  val cassandra = List(
    "com.datastax.spark" %% "spark-cassandra-connector" % "3.1.0"
  )

  // Clickhouse - https://github.com/DmitryBe/spark-clickhouse
  val clickhouse = List(
    "io.clickhouse" %% "spark-clickhouse-connector" % "0.23"
  )

  // Cloudant - http://bahir.apache.org/docs/spark/2.2.0/spark-sql-cloudant
  val cloudant = List(
    "org.apache.bahir" % "spark-sql-cloudant_2.11" % "2.4.0"
  )

  // Couchbase - https://github.com/couchbase/couchbase-spark-connector
  val couchbase = List(
    "com.couchbase.client" %% "spark-connector" % "2.4.1"
  )

  // CouchDb - http://bahir.apache.org/docs/spark/2.2.0/spark-sql-cloudant
  val couchDb = List(
    "org.apache.bahir" % "spark-sql-cloudant_2.11" % "2.4.0"
  )

  // DGraph - https://github.com/G-Research/spark-dgraph-connector
  val dgraph = List(
    "uk.co.gresearch.spark" %% "spark-dgraph-connector" % "1.4.0-2.4"
  )

  // Druid - https://mvnrepository.com/artifact/com.alibaba/druid
  val druid = List(
    "com.alibaba" % "druid" % "1.2.8"
  )

  // DynamoDb - https://github.com/audienceproject/spark-dynamodb
  val dynamoDb = List(
    "com.audienceproject" %% "spark-dynamodb" % "1.1.2"
  )

  // ElasticSearch - https://github.com/elastic/elasticsearch-hadoop
  val elasticSearch =  (
    "org.elasticsearch" % "elasticsearch-hadoop" % "7.15.2"
  )

  // Exasol - https://mvnrepository.com/artifact/com.exasol/spark-connector
  val exasol = List(
    "com.exasol" % "exasol-jdbc" % "7.0.4",
    "com.exasol" %% "spark-connector" % "0.3.2"
  )

  // GoogleAnalytics - https://github.com/crealytics/spark-google-analytics
  val googleAnalytics = List(
    "com.crealytics" % "spark-google-analytics_2.11" % "1.1.2"
  )

  // GoogleSheets - https://github.com/potix2/spark-google-spreadsheets
  val googleSheets = List(
    "com.github.potix2" %% "spark-google-spreadsheets" % "0.11.0"
  )

  // Hazelcast - https://github.com/hazelcast/hazelcast-spark
  val hazelcast = List(
    "com.hazelcast" % "hazelcast-spark" % "0.2"
  )

  // Ignite - https://apacheignite-fs.readme.io/docs/ignite-for-spark
  val ignite = List(
    "org.apache.ignite" % "ignite-spark" % "2.11.0"
  )

  // Infinispan - https://github.com/infinispan/infinispan-spark
  val infinispan = List(
    "org.infinispan" %% "infinispan-spark" % "0.10"
  )

  // Marketo - https://github.com/springml/spark-marketo
  val marketo = List(
    "com.springml" %% "spark-marketo" % "1.1.0"
  )

  // MemSql - https://github.com/memsql/memsql-spark-connector
  val memSql = List(
    "com.memsql" % "memsql-spark-connector_2.11" % "3.0.5-spark-2.4.4",
    "org.mariadb.jdbc" % "mariadb-java-client" % "2.+"
  )

  // MongoDb - https://docs.mongodb.com/spark-connector/current/scala-api
  val mongoDb = List(
    "org.mongodb.spark" %% "mongo-spark-connector" % "3.0.0"
  )

  // MySql
  val mySql = List(
    "mysql" % "mysql-connector-java" % "8.0.27"
  )

  // Neo4j - https://github.com/neo4j-contrib/neo4j-spark-connector
  val neo4j = List(
    "neo4j-contrib" % "neo4j-connector-apache-spark_2.11" % "4.1.0"
  )

  // Netsuite - https://github.com/springml/spark-netsuite
  val netsuite = List(
    "com.springml" % "spark-netsuite_2.11" % "1.1.0"
  )

  // Oracle
  val oracle = List(
    "com.oracle.database.jdbc" % "ojdbc10" % "19.12.0.0"
  )

  // PostgreSql
  val postgreSql = List(
    "org.postgresql" % "postgresql" % "42.3.1"
  )

  // PowerBi - https://github.com/hdinsight/spark-powerbi-connector
  val powerBi = List(
    "com.microsoft.azure" %% "spark-powerbi-connector" % "0.6.1"
  )

  // Quickbooks - https://github.com/inetsoft-technology/spark-quickbooks/
  val quickbooks = List(
    "com.inetsoft.connectors" % "spark-quickbooks" % "1.1.2"
  )

  // Redis - https://github.com/RedisLabs/spark-redis
  val redis = List(
    "com.redislabs" %% "spark-redis" % "2.5.0"
  )

  // Sagemaker - https://github.com/aws/sagemaker-spark
  val sagemaker = List(
    "com.amazonaws" % "sagemaker-spark_2.11" % "spark_2.2.0-1.0"
  )

  // Salesforce - https://github.com/springml/spark-salesforce
  val salesforce = List(
    "com.springml" % "spark-salesforce_2.11" % "1.1.3"
  )

  // Sftp - https://github.com/springml/spark-sftp
  val sftp = List(
    "com.springml" % "spark-sftp_2.11" % "1.1.3"
  )

  // Snowflake - https://github.com/snowflakedb/spark-snowflake
  val snowflake = List(
    "net.snowflake" %% "spark-snowflake" % "2.8.3-spark_2.4"
  )

  // Spark Search - https://github.com/phymbert/spark-search
  val sparkSearch = List(
    "io.github.phymbert" %% "spark-search" % "0.1.6"
  )

  // SqlServer - https://github.com/microsoft/sql-spark-connector
  val sqlServer = List(
    "com.microsoft.azure" % "spark-mssql-connector" % "1.0.1"
  )

  // TiDB/TiKV - https://github.com/pingcap/tispark
  val tiSpark = List(
    "com.pingcap.tispark" % "tispark-assembly" % "2.3.11"
  )

  // Trino - https://mvnrepository.com/artifact/io.trino/trino-jdbc
  val trino = List(
    "io.trino" % "trino-jdbc" % "351"
  )

  // Workday - https://github.com/springml/spark-workday
  val workday = List(
    "com.springml" % "spark-workday_2.11" % "1.1.0"
  )
}