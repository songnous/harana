import com.harana.sbt.common.Settings

import java.nio.charset.Charset
import sbt.{File, _}
import scalajsbundler.sbtplugin.ScalaJSBundlerPlugin.autoImport.npmDependencies

val utf8 = Charset.forName("UTF-8")
val electronMainPath = SettingKey[File]("electron-main-path", "The absolute path where to write the Electron application's main.")
val electronMain = TaskKey[File]("electron", "Generate Electron application's main file.")

lazy val electron = project.in(file("electron"))
	.enablePlugins(ScalaJSBundlerPlugin)
	.settings(
		scalaJSUseMainModuleInitializer := true,
		electronMainPath := baseDirectory.value / "target" / "main.js",
		electronMain := {
			val jsCode: String = IO.read((fastOptJS in Compile).value.data, utf8)
			val hack = """
					var addGlobalProps = function(obj) { obj.require = require; obj.__dirname = __dirname; }
					if((typeof __ScalaJSEnv === "object") && typeof __ScalaJSEnv.global === "object") { addGlobalProps(__ScalaJSEnv.global); }
					else if(typeof  global === "object") { addGlobalProps(global); }
					else if(typeof __ScalaJSEnv === "object") { __ScalaJSEnv.global = {}; addGlobalProps(__ScalaJSEnv.global); }
					else { var __ScalaJSEnv = { global: {} }; addGlobalProps(__ScalaJSEnv.global) }
					"""
			val dest = electronMainPath.value
			IO.write(dest, hack + jsCode, utf8)
			dest
		}
	)

lazy val designer = haranaCrossProject("designer").in(file("."))
	.settings(
		name := "harana-designer",
		maintainer := "Harana <support@harana.com>",
		packageSummary := "Harana Designer",
		dependencyOverrides ++= Seq(
			"io.suzaku"                	%%% "diode"						% "1.1.14+1-0247ec05+20220122-0708-SNAPSHOT",
			"io.suzaku"                	%%% "diode-core"						% "1.1.14+1-0247ec05+20220122-0708-SNAPSHOT",
			"io.suzaku"                	%%% "diode-data"						% "1.1.14+1-0247ec05+20220122-0708-SNAPSHOT",
			"xml-apis" 									% "xml-apis" % "1.4.01"
		),
		libraryDependencies ++= Seq(
			"com.harana" 								%%% "id-jwt" 					% "1.0.0",
			"com.harana" 								%%% "modules" 				% "1.0.0",
			"com.harana"								%%% "sdk"							% "1.0.0",
			"com.lihaoyi" 							%%% "upickle" 				% "1.4.3",

			// FIXME - This should be moved to Modules Core
			"io.circe"                 						%%% "circe-derivation"                        % "0.13.0-M5" exclude("io.circe", "circe-core"),
			"com.beachape"												 %% "enumeratum-circe"                        % "1.7.0",
			"io.circe"                 						%%% "circe-core"                              % "0.14.1",
			"io.circe"                 						%%% "circe-derivation"                        % "0.13.0-M5" exclude("io.circe", "circe-core"),
			"io.circe"                 						%%% "circe-generic-extras"                    % "0.14.1",
			"io.circe"                 						%%% "circe-generic"                           % "0.14.1",
			"io.circe"                 						%%% "circe-parser"                            % "0.14.1",
			"io.circe"                 						%%% "circe-shapes"                            % "0.14.1",
			"org.latestbit"            						%%% "circe-tagged-adt-codec"                  % "0.10.0",
			"io.circe"                 						%%% "circe-optics"                            % "0.14.1",
			"org.typelevel"			   		 						%%% "squants"                                 % "1.8.3",
			"com.softwaremill.sttp.client3" 			%%% "core" 							% "3.3.18",
			"com.softwaremill.sttp.client3" 			%%% "circe" 						% "3.3.18",
			"com.softwaremill.quicklens" 					%%% "quicklens" 				% "1.6.0"
		),
	).jsSettings(
		zonesFilter := {(z: String) => z == "Australia/Sydney" || z == "Pacific/Honolulu"},
		fastCompile := { compileJS(baseDirectory).dependsOn((Compile / fastOptJS / webpack)) }.value,
		fullCompile := { compileJS(baseDirectory).dependsOn((Compile / fullOptJS / webpack)) }.value,
		npmDependencies in Compile ++= Seq(
		)
	)
	.jsConfigure(_.enablePlugins(TzdbPlugin))
	.jvmSettings(
		libraryDependencies ++= Seq(
			"org.apache.parquet" 													% "parquet-avro" 					% "1.10.0",
			"com.github.mjakubowski84" 									 %% "parquet4s-core" 				% "1.6.0",
			"com.opencsv"  																% "opencsv"								% "5.5.2"
		)
	)
	onLoad in Global := (onLoad in Global).value andThen (Command.process("project designerJVM;", _))
