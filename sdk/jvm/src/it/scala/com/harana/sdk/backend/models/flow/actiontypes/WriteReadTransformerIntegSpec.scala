package com.harana.sdk.backend.models.flow.actiontypes

import java.nio.file.Files
import java.nio.file.Path
import org.scalatest.BeforeAndAfter
import com.harana.sdk.backend.models.flow.actionobjects.{PythonTransformer, TypeConverter}
import com.harana.sdk.backend.models.flow.actiontypes.exceptions.HaranaIOError
import com.harana.sdk.shared.models.flow.actionobjects.TargetTypeChoices

class WriteReadTransformerIntegSpec extends WriteReadTransformerIntegTest with BeforeAndAfter {

  val tempDir: Path = Files.createTempDirectory("writeReadTransformer")

  "ReadTransformer" should {
    "read previously written Transformer" in {
      val transformer = TypeConverter().setTargetType(TargetTypeChoices.BooleanTargetTypeChoice())
      val outputPath = tempDir.resolve("TypeConverter")

      writeReadTransformer(transformer, outputPath.toString)
    }
  }

  "WriteTransformer" should {
    "overwrite the previously written Transformer if the overwrite parameter is set to true" in {
      val transformer1 = new PythonTransformer()
      val transformer2 = TypeConverter().setTargetType(TargetTypeChoices.BooleanTargetTypeChoice())
      val outputPath = tempDir.resolve("TypeConverter")
      writeTransformer(transformer1, outputPath.toString, overwrite = true)
      writeReadTransformer(transformer2, outputPath.toString)
    }

    "throw an exception if a Transformer with the given name exists and the overwrite parameter is set to false" in {
      val transformer = TypeConverter().setTargetType(TargetTypeChoices.BooleanTargetTypeChoice())
      val outputPath = tempDir.resolve("TypeConverter")
      writeTransformer(transformer, outputPath.toString, overwrite = true)
      a[HaranaIOError] shouldBe thrownBy {
        writeTransformer(transformer, outputPath.toString, overwrite = false)
      }
    }
  }

  after {
    tempDir.toFile.delete()
  }
}