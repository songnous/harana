import org.portablescala.sbtplatformdeps.PlatformDepsPlugin.autoImport._
import sbt.Keys.scalaVersion
import sbt._

import scala.language.postfixOps

class Spark(version: String) {
  val all = Library.sparkWithConfigs(version, Seq("default"))
  val provided = Library.sparkWithConfigs(version, Seq("provided"))
  val test = Library.sparkWithConfigs(version, Seq("test,it"))
  val onlyInTests = Library.sparkWithConfigs(version, Seq("provided", "test,it"))
}

object Hadoop {
  private lazy val components = Library.hadoop
  lazy val provided = components.map(_.map(_ % Provided))
  lazy val test = components.map(_.map(_ % s"$Test,it"))
  lazy val onlyInTests = components.map(a => a.map(_ % Provided) ++ a.map(_ % s"$Test,it"))
}

object Version {
  val spark = sys.props.getOrElse("SPARK_VERSION", "3.3.0")
  val (scala, java, hadoop, akka, apacheCommons) = spark match {
    case "3.3.0" => ("2.13.8", "11", "3.3.2", "2.5.32", "3.5")
  }
}

object Library {

  val globalDependencyOverrides = Def.setting(Seq(
    "org.json4s" %% "json4s-core" % "3.7.0-M11",
    "org.json4s" %% "json4s-native" % "3.7.0-M11",
    "org.json4s" %% "json4s-jackson" % "3.7.0-M11",
    "org.json4s" %% "json4s-ext" % "3.7.0-M11",
    "org.codehaus.janino" % "janino" % "3.0.16",
    "org.slf4j" % "slf4j-api" % "1.7.36",
    "io.spray" %%% "spray-json" % "1.4.0"
  ))

  val libraryDependencySchemes = Def.setting(Seq(
    "org.scala-lang.modules" %% "scala-java8-compat" % VersionScheme.Always,
    "org.scala-lang.modules" %% "scala-parser-combinators" % VersionScheme.Always
  ))

  val globalExclusions = Def.setting(Seq(
    ExclusionRule("org.slf4j", "slf4j-log4j12"),
    ExclusionRule("org.slf4j", "slf4j-reload4j")
  ))

  implicit class RichModuleID(m: ModuleID) {
    def excludeAkkaActor = m excludeAll ExclusionRule("com.typesafe.akka")
    def excludeJackson = m excludeAll ExclusionRule("com.fasterxml.jackson.core")
    def excludeGuava = m excludeAll ExclusionRule("com.google.guava", "guava")
    def excludeScalatest = m excludeAll ExclusionRule("org.scalatest")
    def excludeSprayJson = m excludeAll ExclusionRule("io.spray")
  }

  lazy val spark = new Spark(Version.spark).all
  lazy val providedSpark = new Spark(Version.spark).provided
  lazy val testSpark = new Spark(Version.spark).test

  val akka = Def.setting(Seq(
    "com.typesafe.akka" %% "akka-actor" % "2.6.19",
    "com.typesafe.akka" %% "akka-http-core" % "10.2.9",
    "com.typesafe.akka" %% "akka-http-spray-json" % "10.2.9" excludeSprayJson,
    "com.typesafe.akka" %% "akka-slf4j" % "2.6.19",
    "com.typesafe.akka" %% "akka-testkit" % "2.6.19",
    "com.typesafe.akka" %% "akka-protobuf-v3" % "2.6.19",
    "com.typesafe.akka" %% "akka-stream" % "2.6.19"
  ))

  val alluxio = Def.setting(Seq(
    "org.alluxio" % "alluxio-core-client" % "2.8.0",
    "org.alluxio" % "alluxio-core-client-fs" % "2.8.0",
    "org.alluxio" % "alluxio-job-client" % "2.8.0"
  ))

  val asm = Def.setting(Seq(
    "org.ow2.asm" % "asm" % "6.0",
    "org.ow2.asm" % "asm-commons" % "6.0",
    "org.ow2.asm" % "asm-tree" % "6.0"
  ))

  val circe = Def.setting(Seq(
    "io.circe" %% "circe-core" % "0.14.2",
    "io.circe" %% "circe-derivation" % "0.13.0-M5" exclude("io.circe", "circe-core"),
    "io.circe" %% "circe-generic-extras" % "0.14.2",
    "io.circe" %% "circe-generic" % "0.14.2",
    "io.circe" %% "circe-optics" % "0.14.1",
    "io.circe" %% "circe-parser" % "0.14.2",
    "io.circe" %% "circe-shapes" % "0.14.2",
    "io.circe" %% "circe-yaml" % "0.14.1",
    "org.latestbit" %%% "circe-tagged-adt-codec" % "0.10.0",
    "com.beachape" %% "enumeratum-circe" % "1.7.0"
  ))

  val compilerPlugins = Seq(
    addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1"),
    addCompilerPlugin("org.typelevel" % "kind-projector" % "0.13.2" cross CrossVersion.full)
  )

  val diode = Def.setting(Seq(
    "io.suzaku" %%% "diode" % "1.2.0-RC4",
    "io.suzaku" %%% "diode-core" % "1.2.0-RC4",
    "io.suzaku" %%% "diode-data" % "1.2.0-RC4"
  ))

  val dockerJava = Def.setting(Seq(
    "com.github.docker-java" % "docker-java" % "3.2.13",
    "com.github.docker-java" % "docker-java-transport-okhttp" % "3.2.13"
  ))

  val googleServiceApi = Def.setting(Seq(
    "com.google.api-client" % "google-api-client" % "1.34.1",
    "com.google.api-client" % "google-api-client-gson" % "1.34.1",
    "com.google.apis" % "google-api-services-drive" % s"v3-rev197-1.25.0"
  ).map(_.excludeJackson.exclude("com.google.guava", "guava-jdk5")))

  val hadoop = Def.setting(Seq(
    "org.apache.hadoop" % "hadoop-aws" % "3.3.3",
    "org.apache.hadoop" % "hadoop-client" % "3.3.3",
    "org.apache.hadoop" % "hadoop-common" % "3.3.3"
  ))

  val jackson = Def.setting(Seq(
    "com.fasterxml.jackson.dataformat" % "jackson-dataformat-yaml" % "2.13.3",
    "com.fasterxml.jackson.module" % "jackson-module-afterburner" % "2.13.3",
    "com.fasterxml.jackson.module" % "jackson-modules-java8" % "2.13.3",
    "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.13.3"
  ))

  val jgrapht = Def.setting(Seq(
    "org.jgrapht" % "jgrapht-core" % "1.5.1",
    "org.jgrapht" % "jgrapht-ext" % "1.5.1"
  ))

  val json4s = Def.setting(Seq(
    "org.json4s" %% "json4s-core" % "3.7.0-M11",
    "org.json4s" %% "json4s-native" % "3.7.0-M11",
    "org.json4s" %% "json4s-jackson" % "3.7.0-M11",
    "org.json4s" %% "json4s-ext" % "3.7.0-M11"
  ))

  val logging = Def.setting(Seq(
    "org.apache.logging.log4j" % "log4j-api" % "2.17.2",
    "org.apache.logging.log4j" % "log4j-1.2-api" % "2.17.2",
    "org.apache.logging.log4j" % "log4j-core" % "2.17.2",
    "org.apache.logging.log4j" % "log4j-slf4j-impl" % "2.17.2",
    "org.apache.logging.log4j" % "log4j-web" % "2.17.2",
    "org.slf4j" % "slf4j-api" % "1.7.36"
  ))

  val micrometer = Def.setting(Seq(
    "io.github.mweirauch" % "micrometer-jvm-extras" % "0.2.2",
    "io.micrometer" % "micrometer-registry-prometheus" % "1.9.0"
  ))

  val netty = Def.setting(Seq(
    "io.netty" % "netty-resolver-dns-native-macos" % "4.1.78.Final" classifier "osx-aarch_64",
    "io.netty" % "netty-resolver-dns-native-macos" % "4.1.78.Final" classifier "osx-x86_64",
    "io.netty" % "netty-transport-native-epoll" % "4.1.78.Final" classifier "linux-x86_64",
    "io.netty.incubator" % "netty-incubator-transport-native-io_uring" % "0.0.14.Final" classifier "linux-x86_64"
  ))

  val npmDev = Def.setting(Map(
    "awesome-typescript-loader" -> "5.2.1",
    "electron-builder" -> "22.10.5",
    "hard-source-webpack-plugin" -> "0.13.1",
    "html-webpack-plugin" -> "4.5.2",
    "static-site-generator-webpack-plugin" -> "3.4.2",
    "typescript" -> "4.2.3",
    "webpack-merge" -> "5.7.3"
  ))

  val npmResolutions = Def.setting(Map(
    "source-map" -> "0.7.3"
  ))

  val npmDependencies = Def.setting(Seq(
    "@data-ui/sparkline" -> "0.0.75",
    "@nivo/bar" -> "0.79.1",
    "@nivo/calendar" -> "0.79.1",
    "@nivo/core" -> "0.79.0",
    "@nivo/waffle" -> "0.79.1",
    "@shoelace-style/shoelace" -> "2.0.0-beta.74",
    "@vertx/eventbus-bridge-client.js" -> "1.0.0",
    "@visx/gradient" -> "2.1.0",
    "@visx/shape" -> "2.4.0",
    "closest" -> "0.0.1",
    "copy-webpack-plugin" -> "5.1.0",
    "css-loader" -> "3.3.0",
    "file-loader" -> "5.0.2",
    "filepond" -> "4.21.1",
    "hard-source-webpack-plugin" -> "0.13.1",
    "history" -> "4.10.1",
    "prop-types" -> "15.7.2",
    "react" -> "17.0.1",
    "react-color" -> "2.17.0",
    "react-dom" -> "17.0.1",
    "react-filepond" -> "7.1.0",
    "react-flow-renderer" -> "8.0.0",
    "react-helmet" -> "6.1.0",
    "react-intl" -> "2.9.0",
    "react-lazylog" -> "4.5.3",
    "react-lazyload" -> "3.1.0",
    "react-markdown" -> "5.0.3",
    "react-proxy" -> "1.1.8",
    "react-router" -> "5.1.2",
    "react-router-dom" -> "5.1.2",
    "react-router-cache-route" -> "1.10.1",
    "react-split-pane" -> "0.1.89",
    "react-syntax-highlighter" -> "15.2.1",
    "sockjs-client" -> "1.4.0",
    "style-loader" -> "1.0.1",
    "throttle-debounce" -> "2.2.1",
    "url-loader" -> "3.0.0"
  ))

  val okhttp = Def.setting(Seq(
    "com.squareup.okhttp3" % "logging-interceptor" % "4.9.3",
    "com.squareup.okhttp3" % "okhttp" % "4.9.3",
    "io.github.dkorobtsov.plinter" % "okhttp3-interceptor" % "5.2.2"
  ))

  val osgi = Def.setting(Seq(
    "org.apache.felix" % "org.apache.felix.framework" % "7.0.1",
    "org.apache.felix" % "org.apache.felix.fileinstall" % "3.6.4",
    "org.apache.felix" % "org.apache.felix.configadmin" % "1.8.16",
    "org.apache.felix" % "org.apache.felix.bundlerepository" % "2.0.10",
    "org.osgi" % "org.osgi.core" % "6.0.0"
  ))

  val pac4j = Def.setting(Seq(
    "org.pac4j" % "pac4j-cas" % "5.1.3",
    "org.pac4j" % "pac4j-config" % "5.1.3",
    "org.pac4j" % "pac4j-http" % "5.1.3",
    "org.pac4j" % "pac4j-jwt" % "5.1.3",
    "org.pac4j" % "pac4j-ldap" % "5.1.3",
    "org.pac4j" % "pac4j-oauth" % "5.1.3",
    "org.pac4j" % "pac4j-oidc" % "5.1.3",
    "org.pac4j" % "pac4j-saml" % "5.1.3"
  ))

  val parquet = Def.setting(Seq(
    "com.github.mjakubowski84"  %% "parquet4s-core" % "2.5.1",
    "org.apache.parquet" % "parquet-avro" % "1.12.2"
  ))

  val retrofit = Def.setting(Seq(
    "com.squareup.retrofit2" % "converter-gson" % "2.9.0",
    "com.squareup.retrofit2" % "converter-scalars" % "2.9.0",
    "com.squareup.retrofit2" % "retrofit" % "2.9.0"
  ))

  val scala = Def.setting(Seq(
    "org.scala-lang" % "scala-compiler" % scalaVersion.value % "provided",
    "org.scala-lang" % "scala-reflect" % scalaVersion.value
  ))

  val scalajs = Def.setting(Seq(
    "org.scala-js" % "sbt-scalajs" % "1.10.0",
    "com.vmunier" %% "scalajs-scripts" % "1.2.0"
  ))

  val sisu = Def.setting(Seq(
    "org.eclipse.sisu" % "org.eclipse.sisu.plexus" % "0.3.5",
    "org.eclipse.sisu" % "org.eclipse.sisu.inject" % "0.3.5"
  ))

  val slinky = Def.setting(Seq(
    "me.shadaj" %%% "slinky-core" % "0.7.2",
    "me.shadaj" %%% "slinky-web" % "0.7.2",
    "me.shadaj" %%% "slinky-history"  % "0.7.2",
    "me.shadaj" %%% "slinky-hot" % "0.7.2",
    "me.shadaj" %%% "slinky-react-router"  % "0.7.2",
    "me.shadaj" %%% "slinky-readwrite" % "0.7.2"
  ))

  private val sparkExclusionRules = Seq(
    ExclusionRule(organization = "org.apache.curator"),
    ExclusionRule("javax.ws.rs", "jsr311-api"),
    ExclusionRule("com.sun.jersey", "jersey-client"),
    ExclusionRule("com.sun.jersey", "jersey-core"),
    ExclusionRule("com.sun.jersey", "jersey-servlet"),
    ExclusionRule("com.sun.xml.bind", "jaxb-impl"),
    ExclusionRule("org.slf4j", "slf4j-log4j12"),
    ExclusionRule("org.slf4j", "slf4j-reload4j"),
    ExclusionRule("log4j", "log4j")
  )

  val sparkWithConfigs = (version: String, configurations: Seq[String]) => Def.setting {
    configurations.flatMap(c => {
      Seq(
        "org.apache.spark" %% "spark-core" % version % c excludeAll (sparkExclusionRules: _*),
        "org.apache.spark" %% "spark-hive" % version % c excludeAll (sparkExclusionRules: _*),
        "org.apache.spark" %% "spark-mllib" % version % c excludeAll (sparkExclusionRules: _*),
        "org.apache.spark" %% "spark-sql" % version % c excludeAll (sparkExclusionRules: _*),
        "org.apache.spark" %% "spark-streaming" % version % c excludeAll (sparkExclusionRules: _*),
      )
    })
  }

  val sttp = Def.setting(Seq(
    "com.softwaremill.sttp.client3" %%% "core" % "3.3.18",
    "com.softwaremill.sttp.client3" %%% "circe" % "3.3.18"
  ))

  val testing = Def.setting(Seq(
    "org.scalatest" %%% "scalatest" % "3.2.12" % Test,
    "org.scalatestplus" %% "scalacheck-1-15" % "3.2.11.0" % Test,
    "org.scalatestplus" %% "mockito-4-5" % "3.2.12.0" % Test,
    "org.mockito" % "mockito-core" % "4.6.1" % Test,
    "org.scoverage" %% "scalac-scoverage-runtime" % "1.4.11" % Test,
    "com.github.tomakehurst" % "wiremock" % "2.27.2" % Test exclude ("com.google.guava", "guava") excludeJackson
  ))

  val vertx = Def.setting(Seq(
    "io.vertx" % "vertx-auth-jwt" % "4.2.1",
    "io.vertx" % "vertx-config-git" % "4.2.1",
    "io.vertx" % "vertx-core" % "4.2.1",
    "io.vertx" % "vertx-health-check" % "4.2.1",
    "io.vertx" % "vertx-micrometer-metrics" % "4.2.1",
    "io.vertx" % "vertx-service-discovery-bridge-consul" % "4.2.1",
    "io.vertx" % "vertx-service-discovery-bridge-docker" % "4.2.1",
    "io.vertx" % "vertx-service-discovery-bridge-kubernetes" % "4.2.1",
    "io.vertx" % "vertx-tcp-eventbus-bridge" % "4.2.1",
    "io.vertx" % "vertx-unit" % "4.2.1",
    "io.vertx" % "vertx-web" % "4.2.1",
    "io.vertx" % "vertx-web-client" % "4.2.1",
    "io.vertx" % "vertx-web-sstore-cookie" % "4.2.1",
    "io.vertx" % "vertx-web-templ-handlebars" % "4.2.1",
    "io.vertx" % "vertx-zookeeper" % "4.2.1"
  ))

  val vfs = Def.setting(Seq(
    "org.apache.commons" % "commons-vfs2" % "2.9.0",
    "org.apache.commons" % "commons-vfs2-jackrabbit2" % "2.9.0",
    "com.github.abashev" % "vfs-s3" % "4.3.5"
  ))

  val zio1 = Def.setting(Seq(
    "dev.zio" %% "zio" % "1.0.15",
    "dev.zio" %% "zio-interop-cats" % "3.2.9.1",
    "dev.zio" %% "zio-macros" % "1.0.15",
    "dev.zio" %% "zio-process" % "0.6.1",
    "dev.zio" %% "zio-streams" % "1.0.14",
    "dev.zio" %% "zio-test" % "1.0.15" % "test",
    "dev.zio" %% "zio-test-sbt" % "1.0.15" % "test",
    "dev.zio" %% "zio-test-magnolia" % "1.0.15" % "test"
  ))

  val zio2 = Def.setting(Seq(
    "dev.zio" %% "zio" % "2.0.1",
    "dev.zio" %% "zio-interop-cats" % "22.0.0.0",
    "dev.zio" %% "zio-macros" % "2.0.1",
    "dev.zio" %% "zio-process" % "0.7.1",
    "dev.zio" %% "zio-streams" % "2.0.1",
    "dev.zio" %% "zio-test" % "2.0.1" % "test",
    "dev.zio" %% "zio-test-sbt" % "2.0.1" % "test",
    "dev.zio" %% "zio-test-magnolia" % "2.0.1" % "test"
  ))

  val airtable = Def.setting("dev.fuxing" % "airtable-api" % "0.3.2")
  val amazonS3 = Def.setting("com.amazonaws" % "aws-java-sdk-s3" % "1.12.237" excludeJackson)
  val auth0 = Def.setting("com.auth0" % "auth0" % "1.42.0")
  val automapper = Def.setting("io.bfil"  %% "automapper" % "0.6.2")
  val avro4s = Def.setting("com.sksamuel.avro4s" %% "avro4s-core" % "4.1.0")
  val awsJavaSes = Def.setting("com.amazonaws" % "aws-java-sdk-ses" % "1.12.237")
  val awsScalaIam = Def.setting("com.github.seratch" %% "awscala-iam" % "0.8.5")
  val awsScalaS3 = Def.setting("com.github.seratch" %% "awscala-s3" % "0.8.5")
  val betterFiles = Def.setting("com.github.pathikrit" %% "better-files" % "3.9.1")
  val calciteCore = Def.setting("org.apache.calcite" % "calcite-core" % "1.30.0")
  val chargebee = Def.setting("com.chargebee" % "chargebee-java" % "2.15.0")
  val chimney = Def.setting("io.scalaland" %% "chimney" % "0.6.1")
  val commonsCodec = Def.setting("commons-codec" % "commons-codec" % "1.15")
  val commonsCsv = Def.setting("org.apache.commons" % "commons-csv" % "1.9.0")
  val commonsEmail = Def.setting("org.apache.commons" % "commons-email" % "1.5")
  val commonsIo = Def.setting("commons-io" % "commons-io" % "2.11.0")
  val commonsLang3 = Def.setting("org.apache.commons" % "commons-lang3" % "3.12.0")
  val commonsText = Def.setting("org.apache.commons" % "commons-text" % "1.9")
  val config = Def.setting("com.typesafe" % "config" % "1.4.2")
  val deepstream = Def.setting("io.deepstream" % "deepstream.io-client-java" % "2.2.2")
  val facebook = Def.setting("com.facebook.business.sdk" % "facebook-java-business-sdk" % "13.0.0")
  val ficus = Def.setting("com.iheart" %% "ficus" % "1.5.2")
  val fst = Def.setting("de.ruedigermoeller" % "fst" % "3.0.3")
  val gson = Def.setting("com.google.code.gson" % "gson" % "2.9.0")
  val guava = Def.setting("com.google.guava" % "guava" % "31.0.1-jre")
  val guice = Def.setting("com.google.inject" % "guice" % "5.1.0")
  val handlebars = Def.setting("com.github.jknack" % "handlebars" % "4.3.0")
  val httpCore = Def.setting("org.apache.httpcomponents" % "httpcore" % "4.4.15")
  val javaMail = Def.setting("com.sun.mail" % "jakarta.mail" % "2.0.1")
  val javassist = Def.setting("org.javassist" % "javassist" % "3.23.0-GA")
  val javaWebsocket = Def.setting("org.java-websocket" % "Java-WebSocket" % "1.5.3")
  val javaxAnnotations = Def.setting("javax.annotation" % "javax.annotation-api" % "1.3.2")
  val javaxInject = Def.setting("javax.inject" % "javax.inject" % "1")
  val jbrowserDriver = Def.setting("com.machinepublishers" % "jbrowserdriver" % "1.1.1")
  val jgit = Def.setting("org.eclipse.jgit" % "org.eclipse.jgit" % "6.1.0.202203080745-r")
  val jose = Def.setting("com.nimbusds" % "nimbus-jose-jwt" % "9.23")
  val jose4j = Def.setting("org.bitbucket.b_c" % "jose4j" % "0.7.12")
  val jsch = Def.setting("com.jcraft" % "jsch" % "0.1.55")
  val jsonLenses = Def.setting("net.virtual-void" %% "json-lenses" % "0.6.2" excludeSprayJson)
  val jsoup = Def.setting("org.jsoup" % "jsoup" % "1.14.3")
  val jsr305 = Def.setting("com.google.code.findbugs" % "jsr305" % "3.0.2")
  val kubernetesClient = Def.setting("io.kubernetes" % "client-java" % "15.0.1")
  val mixpanel = Def.setting("com.mixpanel" % "mixpanel-java" % "1.5.0")
  val mongodbScala = Def.setting("org.mongodb.scala" %% "mongo-scala-driver" % "4.6.0")
  val nscalaTime = Def.setting("com.github.nscala-time" %% "nscala-time" % "2.30.0")
  val ognl = Def.setting("ognl" % "ognl" % "3.3.2")
  val opencsv = Def.setting("com.opencsv" % "opencsv"% "5.6")
  val parboiled = Def.setting("org.parboiled" %% "parboiled" % "2.4.0")
  val playJsonExtensions = Def.setting("ai.x" %% "play-json-extensions" % "0.42.0")
  val plexusUtils = Def.setting("org.codehaus.plexus" % "plexus-utils" % "3.4.2")
  val pureCsv = Def.setting("io.kontainers" %% "purecsv" % "1.3.10")
  val rabbitmq = Def.setting("com.newmotion" %% "akka-rabbitmq" % "6.0.0" excludeAkkaActor)
  val redisson = Def.setting("org.redisson" % "redisson" % "3.17.3")
  val reflections = Def.setting("org.reflections" % "reflections" % "0.10.2")
  val scaffeine = Def.setting("com.github.blemale" %% "scaffeine" % "5.2.0")
  val scalaHashing = Def.setting("com.desmondyeung.hashing"%% "scala-hashing" % "0.1.0")
  val scalajsDom = Def.setting("org.scala-js" %%% "scalajs-dom" % "2.0.0")
  val scalajsStubs = Def.setting("org.scala-js" %% "scalajs-stubs" % "1.1.0")
  val scalate = Def.setting("org.scalatra.scalate" %% "scalate-core" % "1.9.8")
  val scalazCore = Def.setting("org.scalaz" %%% "scalaz-core" % "7.3.3")
  val scopt = Def.setting("com.github.scopt" %% "scopt" % "4.0.1")
  val segment = Def.setting("com.segment.analytics.java" % "analytics" % "3.2.0")
  val sentry = Def.setting("io.sentry" % "sentry" % "6.0.0")
  val shapeless = Def.setting("com.chuusai" %% "shapeless" % "2.3.3")
  val shopify = Def.setting("com.channelape" % "shopify-sdk" % "2.4.3" excludeAll(ExclusionRule(organization = "com.sun.xml.bind")))
  val siteCrawler = Def.setting("io.github.jasperroel" % "SiteCrawler" % "1.0.0")
  val skuber = Def.setting("io.skuber" %% "skuber" % "2.6.4")
  val slack = Def.setting("com.hubspot.slack" % "slack-java-client" % "1.12")
  val snappy = Def.setting("org.xerial.snappy" % "snappy-java" % "1.1.8.4")
  val sourcecode = Def.setting("com.lihaoyi" %% "sourcecode" % "0.2.8")
  val sprayJson = Def.setting("io.spray" %%% "spray-json" % "1.4.0")
  val squants = Def.setting("org.typelevel"  %%% "squants" % "1.8.3")
  val sshj = Def.setting("com.hierynomus" % "sshj" % "0.33.0")
  val stripe = Def.setting("com.stripe" % "stripe-java" % "20.127.0")
  val sttpQuicklens = Def.setting("com.softwaremill.quicklens" %%% "quicklens" % "1.6.0")
  val sundial = Def.setting("org.knowm" % "sundial" % "2.2.2")
  val ulid = Def.setting("net.petitviolet" %% "ulid4s" % "0.5.0")
  val unboundid = Def.setting("com.unboundid" % "unboundid-ldapsdk" % "6.0.5")
  val univocity = Def.setting("com.univocity" % "univocity-parsers" % "2.9.1")
  val upickle = Def.setting("com.lihaoyi" %%% "upickle" % "1.4.3")
  val webjarsLocator = Def.setting("org.webjars" % "webjars-locator-core" % "0.50")
  val xmlApis = Def.setting("xml-apis" % "xml-apis" % "2.0.2")
  val youi = Def.setting("io.youi" %% "youi-client" % "0.14.4")
  val zendeskClient = Def.setting("com.cloudbees.thirdparty" % "zendesk-java-client" % "0.17.0")
  val zip4j = Def.setting("net.lingala.zip4j" % "zip4j" % "2.10.0")
  val ztZip = Def.setting("org.zeroturnaround" % "zt-zip" % "1.15")

  // Dependencies for swagger-client generated code
  val oauth2Client = Def.setting("org.apache.oltu.oauth2" % "org.apache.oltu.oauth2.client" % "1.0.2")
  val swaggerAnnotations = Def.setting("io.swagger" % "swagger-annotations" % "1.6.6")
  val jodaTime = Def.setting("joda-time" % "joda-time" % "2.10.14")

  // Scala.js
  val scalablyTyped = Def.setting("com.olvind" %%% "scalablytyped-runtime" % "2.1.0")
  val scalaJavaTime = Def.setting("io.github.cquiroz" %%% "scala-java-time" % "2.0.0")
}

