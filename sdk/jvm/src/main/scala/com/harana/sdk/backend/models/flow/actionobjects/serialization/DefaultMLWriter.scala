package com.harana.sdk.backend.models.flow.actionobjects.serialization

import com.harana.sdk.backend.models.flow.actionobjects.Transformer
import com.harana.sdk.shared.models.flow.parameters.Parameters
import com.harana.spark.ML.MLWriterWithSparkContext
import org.apache.hadoop.fs.Path
import org.apache.spark.SparkContext
import org.apache.spark.ml.param.{ParamPair, Params}
import org.apache.spark.ml.util.MLWriter
import org.json4s.JsonDSL._
import org.json4s._
import org.json4s.jackson.JsonMethods._

class DefaultMLWriter[T <: Params](instance: T) extends MLWriter with MLWriterWithSparkContext {

  def saveImpl(path: String) = {
    val modelPath = Transformer.modelFilePath(path)
    saveMetadata(instance, path, sc)
    CustomPersistence.save(sparkContext, instance, modelPath)
  }

  private def saveMetadata(instance: Params, path: String, sc: SparkContext, extraMetadata: Option[JObject] = None, paramMap: Option[JValue] = None) = {
    val uid           = instance.uid
    val cls           = instance.getClass.getName
    val parameters        = instance.extractParamMap().toSeq.asInstanceOf[Seq[ParamPair[Any]]]
    val jsonParameters    = paramMap.getOrElse(render(parameters.map { case ParamPair(p, v) => p.name -> parse(p.jsonEncode(v))}.toList))
    val basicMetadata = ("class" -> cls) ~
      ("timestamp"    -> System.currentTimeMillis()) ~
      ("sparkVersion" -> sc.version) ~
      ("uid"          -> uid) ~
      ("paramMap"     -> jsonParameters)
    val metadata     = extraMetadata match {
      case Some(jObject) => basicMetadata ~ jObject
      case None          => basicMetadata
    }
    val metadataPath = new Path(path, "metadata").toString
    val metadataJson = compact(render(metadata))
    sc.parallelize(Seq(metadataJson), 1).saveAsTextFile(metadataPath)
  }
}
