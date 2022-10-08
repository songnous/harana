// scalastyle:off

import ProjectsPlugin.autoImport.crossProject
import scalajsbundler.sbtplugin.ScalaJSBundlerPlugin.autoImport.{npmDependencies, npmResolutions}

/****************************************************************************************************************
 * Spark Patching                                                                                               *
 ****************************************************************************************************************/

val sparkSettings = (
  libraryDependencies ++=
    Library.akka.value ++
    Library.providedSpark.value :+
    Library.univocity.value
  )

val sparkPatchesCsv2_2 = project settings(sparkSettings) in file(s"spark-patches/csv-2_2")
val sparkPatchesCsv2_4 = project settings(sparkSettings) in file(s"spark-patches/csv-2_4")

val sparkPatchesCsv = Version.spark match {
  case "2.2.3" => sparkPatchesCsv2_2
  case "2.4.8" => sparkPatchesCsv2_4
  case "3.3.0" => sparkPatchesCsv2_4
}

val sparkPatchesPatch2_2_x = project settings(sparkSettings) in file("spark-patches/patch-2.2.x")

val sparkPatchesCurrentVersion = Version.spark match {
  case "2.2.3" => sparkPatchesPatch2_2_x
  case "2.4.8" => sparkPatchesPatch2_2_x
  case "3.3.0" => sparkPatchesPatch2_2_x
}

val sparkPatchesPatch2_x = project settings(sparkSettings) in file(s"spark-patches/patch-2.x") dependsOn (sparkPatchesCsv, sparkPatchesCurrentVersion)

/****************************************************************************************************************
 * Id JWT / Modules / SDK                                                                                                *
 ****************************************************************************************************************/

val sdk = crossProject("sdk")
  .enablePlugins(HaranaBuildInfoPlugin)
  .settings(
    buildInfoPackage := "com.harana.sdk.shared",
    libraryDependencies ++=
      Library.circe.value ++
      Library.testing.value :+
      Library.shapeless.value :+
      Library.squants.value
  )

  .jsSettings(
    libraryDependencies +=
      Library.scalajsDom.value
  )

  .jvmSettings(
    libraryDependencies ++=
      Library.googleServiceApi.value ++
      Library.logging.value ++
      Library.osgi.value ++
      Library.providedSpark.value ++
      Library.testing.value :+

      Library.commonsIo.value :+
      Library.commonsLang3.value :+
      Library.config.value :+
      Library.gson.value :+
      Library.guice.value :+
      Library.javaMail.value :+
      Library.jodaTime.value :+
      Library.nscalaTime.value :+
      Library.reflections.value :+
      Library.scalajsStubs.value :+
      Library.scalate.value
  )

val sdkJVM = sdk.jvm dependsOn(sparkPatchesPatch2_x)
val sdkJS = sdk.js

val modulesCore = jvmProject("modules-core")
  .dependsOn(sdkJVM)
  .settings(
    Library.compilerPlugins,
    libraryDependencies ++=
      Library.jackson.value ++
      Library.logging.value ++
      Library.micrometer.value ++
      Library.okhttp.value ++
      Library.testing.value ++
      Library.vertx.value ++
      Library.zio1.value :+
      Library.commonsIo.value :+
      Library.ficus.value :+
      Library.scaffeine.value :+
      Library.sourcecode.value
    )

val idJwt = crossProject("id-jwt")
  .settings(
    libraryDependencies ++=
      Library.circe.value
  )
  .jvmSettings(
    Library.compilerPlugins,
    libraryDependencies ++=
      Library.vertx.value ++
      Library.zio1.value :+
      Library.fst.value :+
      Library.jose.value :+
      Library.jose4j.value :+
      Library.scaffeine.value
  )
val idJwtVM = idJwt.jvm dependsOn (modulesCore)
  .settings(
    libraryDependencies +=
      Library.scalaHashing.value
  )

val modules = crossProject("modules")
  .settings(
    libraryDependencies ++=
      Library.circe.value
  )

  .jsSettings(
    Compile / npmDependencies ++= Library.npmDependencies.value,
    Compile / npmResolutions ++= Library.npmResolutions.value,
    libraryDependencies ++=
      Library.diode.value ++
      Library.slinky.value :+
      Library.scalablyTyped.value :+
      Library.scalaJavaTime.value
  )

  .jvmSettings(
    unmanagedBase := baseDirectory.value / "lib",
    libraryDependencies ++=
      Library.airbyte.value ++
      Library.alluxio.value ++
      Library.dockerJava.value ++
      Library.jackson.value ++
      Library.jgrapht.value ++
      Library.json4s.value ++
      Library.netty.value ++
      Library.pac4j.value ++
      Library.scala.value ++
      Library.testing.value ++
      Library.vertx.value ++
      Library.vfs.value ++
      Library.zio1.value :+

      Library.airtable.value :+
      Library.avro4s.value :+
      Library.awsJavaSes.value :+
      Library.awsScalaIam.value :+
      Library.awsScalaS3.value :+
      Library.auth0.value :+
      Library.betterFiles.value :+
      Library.calciteCore.value :+
      Library.chargebee.value :+
      Library.chimney.value :+
      Library.commonsEmail.value :+
      Library.deepstream.value :+
      Library.facebook.value :+
      Library.handlebars.value :+
      Library.javaWebsocket.value :+
      Library.jbrowserDriver.value :+
      Library.jgit.value :+
      Library.jsch.value :+
      Library.jsoup.value :+
      Library.kubernetesClient.value :+
      Library.mixpanel.value :+
      Library.mongodbScala.value :+
      Library.ognl.value :+
      Library.playJsonExtensions.value :+
      Library.pureCsv.value :+
      Library.redisson.value :+
      Library.segment.value :+
      Library.sentry.value :+
      Library.shopify.value :+
      Library.skuber.value :+
      Library.sshj.value :+
      Library.siteCrawler.value :+
      Library.snappy.value :+
      Library.slack.value :+
      Library.stripe.value :+
      Library.sundial.value :+
      Library.ulid.value :+
      Library.unboundid.value :+
      Library.zendeskClient.value :+
      Library.zip4j.value :+
      Library.ztZip.value
  )
val modulesJVM = modules.jvm dependsOn (modulesCore, sdkJVM)
val modulesJS = modules.js dependsOn (sdkJS)


/****************************************************************************************************************
 * Applications                                                                                                 *
 ****************************************************************************************************************/

val designer = crossProject("designer")
  .settings(
    libraryDependencies ++=
      Library.sttp.value :+
      Library.sttpQuicklens.value :+
      Library.upickle.value
  )
  .jvmSettings(
    libraryDependencies ++=
      Library.hadoop.value ++
      Library.parquet.value :+
      Library.opencsv.value
  )
  .jsSettings(
    libraryDependencies ++=
      Library.scalajs.value
  )

val designerJVM = designer.jvm dependsOn (idJwtVM, modulesJVM, modulesCore, sdkJVM)
val designerJS = designer.js dependsOn (modulesJS, sdkJS)

val executor = jvmProject("executor")
  .dependsOn(
    modulesJVM,
    modulesCore,
    sdkJVM,
    sdkJVM % "test->test",
    sdkJVM % "test->it",
    sdkJVM % "it->it",
    sparkPatchesCurrentVersion)
  .settings(
    libraryDependencies ++=
      Library.akka.value ++
        Library.logging.value ++
        Library.providedSpark.value ++
        Library.scala.value ++
        Library.testing.value ++
        Library.zio1.value :+
        Library.jsonLenses.value :+
        Library.parboiled.value :+ // FIXME: Only using this for Base64
        Library.rabbitmq.value :+
        Library.scopt.value
  )

val sparkExecutor = jvmProject("spark-executor")
  .dependsOn(
    modulesJVM,
    modulesCore,
    sdkJVM,
    sdkJVM % "test->test",
    sdkJVM % "test->it",
    sdkJVM % "it->it",
    sparkPatchesCurrentVersion)
  .settings(
    libraryDependencies ++=
      Library.akka.value ++
      Library.logging.value ++
      Library.providedSpark.value ++
      Library.scala.value ++
      Library.testing.value :+
      Library.jsonLenses.value :+
      Library.parboiled.value :+ // FIXME: Only using this for Base64
      Library.rabbitmq.value :+
      Library.scopt.value
  )

val id = crossProject("id")
  .settings(
    libraryDependencies ++=
      Library.akka.value
  )
val idJVM = id.jvm dependsOn(idJwtVM, modulesJVM, modulesCore, sdkJVM)
val idJS = id.js dependsOn (idJwt.js, modulesJS, sdkJS)

/****************************************************************************************************************
 * Root                                                                                                         *
 ****************************************************************************************************************/

val root = project
  .in(file("."))
  .settings(name := "harana")
  .aggregate(
    designerJS,
    designerJVM,
    idJS,
    idJVM,
    sdkJS,
    sdkJVM,
    sparkExecutor)


/****************************************************************************************************************
 * Command Aliases                                                                                              *
 ****************************************************************************************************************/

// Sequentially perform integration tests
addCommandAlias(
  "ds-it",
  ";commons/it:test " +
    ";flow/it:test " +
    ";graph/it:test " +
    ";workflowjson/it:test " +
    ";reportlib/it:test " +
    ";workflowexecutor/it:test" +
    ";workflowexecutormqprotocol/it:test"
)

addCommandAlias(
  "generateExamples",
  "flow/it:testOnly com.harana.flow.operations.examples.*")

// scalastyle:on
