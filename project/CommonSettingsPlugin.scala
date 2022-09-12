import sbt.Keys._
import sbt._
import sbtassembly.AssemblyPlugin.autoImport._
import sbtassembly.PathList

object CommonSettingsPlugin extends AutoPlugin {
  override def trigger = allRequirements

  lazy val OurIT = config("it") extend Test

  override def globalSettings = Seq(
    scalaVersion := Version.scala,
  )

  override def projectSettings = Seq(
    organization := "com.harana",
    crossScalaVersions := Seq(Version.scala),
    scalacOptions := Seq(
      "-unchecked", "-deprecation", "-encoding", "utf8", "-feature",
      "-language:existentials", "-language:implicitConversions", "-Vimplicits"
    ),
    javacOptions ++= Seq(
      "-source", Version.java,
      "-target", Version.java
    ),
    doc / javacOptions := Seq(
      "-source", Version.java,
      "-Xdoclint:none"
    ),
    resolvers ++= Settings.resolvers,
    crossPaths := true
  ) ++ ouritSettings ++ testSettings ++ Seq(
  )

  lazy val assemblySettings = Seq(
    assembly / assemblyMergeStrategy := {
      case PathList("META-INF", "MANIFEST.MF")               => MergeStrategy.discard
      case PathList("META-INF", "INDEX.LIST")                => MergeStrategy.discard
      case PathList("META-INF", "ECLIPSEF.SF")               => MergeStrategy.discard
      case PathList("META-INF", "ECLIPSEF.RSA")              => MergeStrategy.discard
      case PathList("META-INF", "DUMMY.SF")                  => MergeStrategy.discard
      case PathList("META-INF", "services", "org.apache.hadoop.fs.FileSystem") => MergeStrategy.concat
      case "reference.conf"                                  => MergeStrategy.concat
      case _ => MergeStrategy.first
    },
    assembly / test := {}
  )

  lazy val ouritSettings = inConfig(OurIT)(Defaults.testSettings) ++ inConfig(OurIT) {
    Seq(
      testOptions ++= Seq(Tests.Argument(TestFrameworks.ScalaTest, "-oF", "-u", s"target/test-reports-${Version.spark}")),
      javaOptions := Seq(s"-DlogFile=${name.value}", "-Xmx2G", "-Xms2G"),
      fork := true,
      unmanagedClasspath += baseDirectory.value / "conf"
    )
  }

  lazy val testSettings = inConfig(Test) {
    Seq(
      testOptions := Seq(Tests.Argument(TestFrameworks.ScalaTest, "-o", "-u", s"target/test-reports-${Version.spark}")),
      fork := true,
      javaOptions := Seq(s"-DlogFile=${name.value}"),
      unmanagedClasspath += baseDirectory.value / "conf"
    )
  }

  override def projectConfigurations = OurIT +: super.projectConfigurations
}