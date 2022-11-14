import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import sbt.Keys._
import sbt.nio.Keys.ReloadOnSourceChanges
import sbt.{nio, _}
import sbtassembly.AssemblyPlugin.autoImport._
import sbtghpackages.GitHubPackagesPlugin.autoImport._
import scalajsbundler.sbtplugin.ScalaJSBundlerPlugin.autoImport._

object Settings {

  val assemblyMergeStrategies =
    assembly / assemblyMergeStrategy := {
      case PathList("reference.conf")                                         => MergeStrategy.concat
      case PathList("META-INF", xs @ _*)                                      => MergeStrategy.discard
      case PathList("io", "netty", xs @ _*)                                   => MergeStrategy.first
      case PathList("org", "bouncycastle", xs @ _*)                           => MergeStrategy.last
      case PathList("com", "mongodb", xs @ _*)                                => MergeStrategy.last
      case PathList("org", "mongodb", xs @ _*)                                => MergeStrategy.last
      case PathList(ps @ _*) if ps.last endsWith "BUILD"                      => MergeStrategy.first
      case PathList("META-INF", "versions", "9", "module-info.class")         => MergeStrategy.discard
      case PathList("scala", "annotation", "nowarn.class" | "nowarn$.class")  => MergeStrategy.first
      case PathList(ps@_*) if ps.last == "project.properties"                 => MergeStrategy.filterDistinctLines
      case PathList(ps@_*) if ps.last == "logback.xml"                        => MergeStrategy.first
      case PathList(ps@_*) if Set(
        "codegen.config" ,
        "service-2.json" ,
        "waiters-2.json" ,
        "customization.config" ,
        "examples-1.json" ,
        "paginators-1.json").contains(ps.last)                                => MergeStrategy.discard

      case x@PathList("META-INF", path@_*)                                    =>
        path map {
          _.toLowerCase
        } match {
          case "spring.tooling" :: xs                                         => MergeStrategy.discard
          case "io.netty.versions.properties" :: Nil                          => MergeStrategy.first
          case "maven" :: "com.google.guava" :: xs                            => MergeStrategy.first
          case _                                                              => (assembly / assemblyMergeStrategy).value.apply(x)
        }

      case x@PathList("OSGI-INF", path@_*) =>
        path map {
          _.toLowerCase
        } match {
          case "l10n" :: "bundle.properties" :: Nil                           => MergeStrategy.concat
          case _                                                               => (assembly / assemblyMergeStrategy).value.apply(x)
        }

      case "application.conf"                                                 => MergeStrategy.concat
      case "module-info.class"                                                => MergeStrategy.discard
      case x                                                                  => (assembly / assemblyMergeStrategy).value.apply(x)
    }

  val common = Seq(
    javaOptions                               := javaLaunchOptions,
    scalaVersion                              := "2.13.8",
    scalacOptions                             := Seq(
                                                      "-deprecation",
                                                      "-feature",
                                                      "-unchecked",
                                                      "-language:higherKinds",
                                                      "-language:implicitConversions",
                                                      "-language:postfixOps",
                                                      "-Xmaxerrs", "10000",
                                                      "-Ymacro-annotations",
                                                      "-Yrangepos",
                                                      "-Ybackend-parallelism", "8",
                                                      "-Ybackend-worker-queue", "8",
                                                      "-Xlog-implicits"
                                                    ),
    doc / sources                             := Seq(),
    packageDoc / publishArtifact              := false,
    Compile / packageDoc / publishArtifact    := false,
    packageSrc / publishArtifact              := false,
    Compile / packageSrc / publishArtifact    := false,
    maxErrors                                 := 1000,
    fork                                      := true,
    Global / cancelable                       := true,
    githubOwner                               := "harana",
    organization                              := "com.harana",
    githubTokenSource                         := TokenSource.Environment("GITHUB_TOKEN"),
    dependencyOverrides                       ++= Library.globalDependencyOverrides.value,
    updateOptions                             := updateOptions.value.withCachedResolution(true),
    Global / nio.Keys.onChangedBuildSource    := ReloadOnSourceChanges,
//    testFrameworks                            := Seq(new TestFramework("zio.test.sbt.ZTestFramework"))
  )

  val javaVersion = {
    var version = System.getProperty("java.version")
    if (version.startsWith("1.")) version = version.substring(2, 3)
    else {
      val dot = version.indexOf(".")
      if (dot != -1) version = version.substring(0, dot)
    }
    val dash = version.indexOf("-")
    if (dash != -1) version = version.substring(0, dash)
    version.toInt
  }

  val javaLaunchOptions = if (javaVersion > 9) Seq(
    "--add-modules=jdk.incubator.foreign",
    "--add-opens=java.base/java.io=ALL-UNNAMED",
    "--add-opens=java.base/java.lang.reflect=ALL-UNNAMED",
    "--add-opens=java.base/java.lang=ALL-UNNAMED",
    "--add-opens=java.base/java.math=ALL-UNNAMED",
    "--add-opens=java.base/java.net=ALL-UNNAMED",
    "--add-opens=java.base/java.security=ALL-UNNAMED",
    "--add-opens=java.base/java.text=ALL-UNNAMED",
    "--add-opens=java.base/java.util.concurrent.atomic=ALL-UNNAMED",
    "--add-opens=java.base/java.util.concurrent.locks=ALL-UNNAMED",
    "--add-opens=java.base/java.util.concurrent=ALL-UNNAMED",
    "--add-opens=java.base/java.util=ALL-UNNAMED",
    "--add-opens=java.base/jdk.internal.reflect=ALL-UNNAMED",
    "--add-opens=java.base/sun.security.pkcs=ALL-UNNAMED",
    "--add-opens=java.base/sun.security.rsa=ALL-UNNAMED",
    "--add-opens=java.base/sun.security.x509=ALL-UNNAMED",
    "--add-opens=java.management/javax.management.openmbean=ALL-UNNAMED",
    "--add-opens=java.management/javax.management=ALL-UNNAMED",
    "--add-opens=java.naming/javax.naming=ALL-UNNAMED",
    "--add-opens=java.sql/java.sql=ALL-UNNAMED"
  ) else Seq()

  val js = Seq(
    scalaJSUseMainModuleInitializer           := true,
    scalaJSLinkerConfig                       ~= { _.withModuleKind(ModuleKind.CommonJSModule) },
    useYarn                                   := false,
    webpack / version                         := "5.74.0",
    startWebpackDevServer / version           := "4.11.1",
    fastOptJS / webpackDevServerExtraArgs     := Seq("--inline", "--hot"),
    webpackEmitSourceMaps                     := false,
    fastOptJS / webpackBundlingMode           := BundlingMode.LibraryAndApplication(),
    fastOptJS / webpackConfigFile             := Some(baseDirectory.value / "webpack-dev.js"),
    fullOptJS / webpackConfigFile             := Some(baseDirectory.value / "webpack-prod.js")
  )

  val jvm = Seq(
    javacOptions                              ++= Seq("-encoding", "UTF-8"),
    excludeDependencies                       ++= Library.globalExclusions.value,
    libraryDependencySchemes                  ++= Library.libraryDependencySchemes.value,
    javaOptions                               ++= {
      val Digits = "^(\\d+)$".r
      sys.env.get("HARANA_DEBUG") match {
        case Some("true") => Seq("-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=5005")
        case Some(Digits(port)) => Seq(s"-agentlib:jdwp=transport=dt_socket,server=y,suspend=y,address=$port")
        case _ => Seq.empty
      }
    }
  )

  val resolvers = Seq(
    Resolver.mavenLocal,
    Resolver.githubPackages("harana"),
    Resolver.jcenterRepo,
    Resolver.sonatypeOssRepos("releases").head,
    Resolver.sonatypeOssRepos("snapshots").head,
    Resolver.url("heroku-sbt-plugin-releases", url("https://dl.bintray.com/heroku/sbt-plugins/"))(Resolver.ivyStylePatterns),
    "typesafe.com" at "https://repo.typesafe.com/typesafe/repo/",
    "sonatype.org" at "https://oss.sonatype.org/content/repositories/releases",
    "spray.io"     at "https://repo.spray.io",
    "newmotion public repo".at("https://nexus.newmotion.com/content/groups/public/"),
    "central.maven.org" at "https://central.maven.org/maven2/",
    "jitpack" at "https://jitpack.io",
    "shibboleth" at "https://build.shibboleth.net/nexus/content/repositories/releases/",
    "typesafe" at "https://repo.typesafe.com/typesafe/releases/",
    "spark-packages" at "https://dl.bintray.com/spark-packages/maven",
    "orientdb" at "https://dl.bintray.com/sbcd90/org.apache.spark",
    "mulesoft" at "https://repository.mulesoft.org/nexus/content/repositories/public/",
    "ossrh" at "https://oss.sonatype.org/service/local/staging/deploy/maven2",
    "exasol" at "https://maven.exasol.com/artifactory/exasol-releases/",
    "airbyte" at "https://airbyte.mycloudrepo.io/public/repositories/airbyte-public-jars"
  )


  def ramDisk(baseDirectory: File, name: String, size: Long) = {
    val cmd = s"""(rm -rf $baseDirectory/target) &&
                 | (mount | grep -q /Volumes/$name || diskutil apfs create `hdiutil attach -nomount ram://$size` $name) &&
                 | (ln -s /Volumes/$name $baseDirectory/target)
    """.stripMargin
    Seq("bash", "-c", cmd)
  }
}
