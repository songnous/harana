package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.transformers

import java.nio.file.Files
import java.nio.file.Path
import org.apache.commons.io.FileUtils
import org.scalatest.BeforeAndAfter
import org.scalatest.Suite
import com.harana.sdk.backend.models.flow.{ExecutionContext, IntegratedTestSupport}
import com.harana.sdk.backend.models.flow.actionobjects.Transformer
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame

trait TransformerSerialization extends Suite with BeforeAndAfter {

  var tempDir: Path = _

  before {
    tempDir = Files.createTempDirectory("writeReadTransformer")
  }

  after {
    FileUtils.deleteDirectory(tempDir.toFile)
  }
}

object TransformerSerialization {

  implicit class TransformerSerializationOps(private val transformer: Transformer) {

    def applyTransformationAndSerialization(path: Path, df: DataFrame)(implicit
        executionContext: ExecutionContext
    ) = {
      val result                          = transformer._transform(executionContext, df)
      val deserialized                    = loadSerializedTransformer(path)
      val resultFromSerializedTransformer = deserialized._transform(executionContext, df)
      IntegratedTestSupport.assertDataFramesEqual(result, resultFromSerializedTransformer)
      result
    }

    def loadSerializedTransformer(path: Path)(implicit executionContext: ExecutionContext) = {
      val outputPath: Path = path.resolve(this.getClass.getName)
      transformer.save(executionContext, outputPath.toString)
      Transformer.load(executionContext, outputPath.toString)
    }

  }
}
