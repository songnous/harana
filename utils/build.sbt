import sbt.Keys._

name := "utils"

libraryDependencies ++= Library.akka.value

libraryDependencies ++= Library.logging.value

libraryDependencies ++= Library.testSpark.value

libraryDependencies ++= Seq(
  Library.commonsIo.value,
  Library.commonsLang3.value,
  Library.config.value,
  Library.gson.value,
  Library.javaMail.value,
  Library.nscalaTime.value,
  Library.scalate.value,
  Library.sprayJson.value
)