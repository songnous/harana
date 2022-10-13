import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import org.scalajs.sbtplugin._
import sbt.Keys._
import sbt.nio.Keys.ReloadOnSourceChanges
import sbt.{nio, _}
import sbtcrossproject.CrossPlugin.autoImport._
import sbtcrossproject.CrossProject
import sbtghpackages.GitHubPackagesKeys.githubRepository
import scalajsbundler.sbtplugin.ScalaJSBundlerPlugin
import scalajsbundler.sbtplugin.ScalaJSBundlerPlugin.autoImport._
import scalajscrossproject.ScalaJSCrossPlugin.autoImport._

import java.nio.charset.Charset
import java.nio.file._
import scala.sys.process._

object ProjectsPlugin extends AutoPlugin {

  override def trigger = allRequirements

  object autoImport {

    val utf8: Charset = Charset.forName("UTF-8")
    val fastCompile = TaskKey[Unit]("fastCompile")
    val fullCompile = TaskKey[Unit]("fullCompile")

    def rootProject(crossProject: CrossProject): Project =
      Project("root", file(".")).
        aggregate(crossProject.js, crossProject.jvm).
        settings(
          Settings.common,
          publish         := {},
          publishLocal    := {},
          publishArtifact := false
        )

    def crossProject(id: String): CrossProject =
      CrossProject(id = id, file(id))(JSPlatform, JVMPlatform)
        .crossType(CrossType.Full)
        .jsConfigure(_.enablePlugins(ScalaJSBundlerPlugin))
        .settings(
          organization := "com.harana",
          name := id,
          githubRepository := id,
          Library.compilerPlugins,
          Settings.common,
          resolvers := Settings.resolvers,
        )
        .jsSettings(
          name := id,
          Library.compilerPlugins,
          fastCompile := { postCompileJS(baseDirectory).dependsOn(Compile / fastOptJS / webpack).dependsOn(preCompileJS(baseDirectory)) }.value,
          fullCompile := { postCompileJS(baseDirectory).dependsOn(Compile / fullOptJS / webpack).dependsOn(preCompileJS(baseDirectory)) }.value,
          scalaJSUseMainModuleInitializer := false,
          Settings.common,
          Settings.js,
          resolvers := Settings.resolvers,
        )
        .jvmSettings(
          name := id,
          Library.compilerPlugins,
          Settings.common,
          Settings.jvm,
          resolvers := Settings.resolvers,
        )

    def jvmProject(id: String): Project =
      Project(id = id, file(id))
        .settings(
          organization := "com.harana",
          name := id,
          githubRepository := id,
          Library.compilerPlugins,
          Settings.common,
          Settings.jvm,
          resolvers := Settings.resolvers,
        )

    def jsProject(id: String): Project =
      Project(id = id, file(id))
        .enablePlugins(ScalaJSPlugin)
        .settings(
          organization := "com.harana",
          name := id,
          githubRepository := id,
          Library.compilerPlugins,
          Settings.common,
          Settings.js,
          resolvers := Settings.resolvers,
        )


    def preCompileJS(baseDirectory: SettingKey[File]) = {
      baseDirectory.map { base =>
        println("Checking if Webpack etc needs to be installed.")
        val nodeModulesPath = s"${base.absolutePath}/target/scala-2.13/scalajs-bundler/main/node_modules"
        new File(nodeModulesPath).mkdir()

        val nodeModules = new File(nodeModulesPath).list().toList
        if (!nodeModules.contains("webpack")) s"npm i -D ml-matrix webpack webpack-cli webpack-merge --prefix $nodeModulesPath/.." !
      }
    }

    def postCompileJS(baseDirectory: SettingKey[File]) =
      baseDirectory.map { base =>
        println("Copying compiled Javascript to src directory.")

        val buildPath = s"${base.absolutePath}/target/scala-2.13/scalajs-bundler/main"
        new File(buildPath).listFiles((dir, name) => name.toLowerCase.contains("opt"))
          .foreach(file => Files.copy(file.toPath, new File(base, s"../jvm/src/main/resources/public/js/${file.getName}").toPath, StandardCopyOption.REPLACE_EXISTING))
      }
  }
}