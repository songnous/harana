import java.nio.file._
import sbt.{File, _}

val fastCompile = TaskKey[Unit]("fastCompile")
val fullCompile = TaskKey[Unit]("fullCompile")

lazy val id = project.in(file("."))
	.aggregate(crossProject.js, crossProject.jvm)
	.settings(
		publish := {},
		publishLocal := {}
	)

lazy val crossProject = haranaCrossProject("id").in(file("."))
  .enablePlugins(JavaServerAppPackaging)
	.jsConfigure(_.enablePlugins(ScalaJSBundlerPlugin))
  .settings(
    name := "harana-id",
		maintainer := "Harana <support@harana.com>",
		packageSummary := "Harana Designer",
    version := "0.0.2",
    libraryDependencies ++= Seq(
			"com.harana" 								  %% "id-jwt" 			 		% "1.0.0",
			"com.harana" 								  %% "modules" 			 		% "1.0.0",
			"com.desmondyeung.hashing"		%% "scala-hashing" 		% "0.1.0",
			"io.bfil" 									  %% "automapper" 			% "0.6.2"
		)
	).jsSettings(
		fastCompile := { compileJS(baseDirectory).dependsOn((Compile / fastOptJS / webpack)) }.value,
		fullCompile := { compileJS(baseDirectory).dependsOn((Compile / fullOptJS / webpack)) }.value,
		npmDependencies in Compile ++= Seq()
  ).jvmSettings(
		dependencyOverrides += "org.slf4j" % "slf4j-api" % "1.7.32",
		libraryDependencies ++= Seq(
			"org.apache.logging.log4j"		% "log4j-slf4j-impl"	% "2.14.1",
			"org.apache.logging.log4j"		% "log4j-api"					% "2.14.1"
		)
	)

onLoad in Global := (onLoad in Global).value andThen (Command.process("project idJVM;", _))
