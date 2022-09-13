package com.harana.sdk.backend.models.flow.actions.write

import com.harana.sdk.backend.models.flow.actionobjects.Transformer
import com.harana.sdk.backend.models.flow.actions.exceptions.HaranaIOError
import com.harana.sdk.backend.models.flow.utils.FileOperations.deleteRecursivelyIfExists
import com.harana.sdk.backend.models.flow.{Action1To0, ExecutionContext}
import com.harana.sdk.backend.models.flow.actionobjects.Transformer
import com.harana.sdk.shared.models.flow.actions.write.WriteTransformerInfo
import org.apache.hadoop.fs.{FileSystem, Path}

import java.io.{File, IOException}
import java.net.URI

class WriteTransformer extends Action1To0[Transformer] with WriteTransformerInfo {

  def execute(transformer: Transformer)(context: ExecutionContext) = {
    val outputDictPath = getOutputPath
    try {
      if (getShouldOverwrite) removeDirectory(context, outputDictPath)
      transformer.save(context, outputDictPath)
    } catch {
      case e: IOException =>
        println(s"WriteTransformer error. Could not write transformer to the directory", e)
        throw HaranaIOError(e).toException
    }
  }

  private def removeDirectory(context: ExecutionContext, path: String) =
    if (path.startsWith("hdfs://")) {
      val configuration = context.sparkContext.hadoopConfiguration
      val hdfs          = FileSystem.get(new URI(extractHdfsAddress(path)), configuration)
      hdfs.delete(new Path(path), true)
    } else
      deleteRecursivelyIfExists(new File(path))

  private def extractHdfsAddress(path: String) = {
    val regex                 = "(hdfs:\\/\\/[^\\/]*)(.*)".r
    val regex(hdfsAddress, _) = path
    hdfsAddress
  }
}

object WriteTransformer {
  def apply(outputPath: String): WriteTransformer = new WriteTransformer().setOutputPath(outputPath)
}