package com.harana.sdk.backend.models.flow.actionobjects.serialization

import com.harana.sdk.backend.models.flow.ExecutionContext
import com.harana.sdk.backend.models.flow.utils.TypeUtils
import com.harana.sdk.backend.models.flow.utils.catalog.exceptions.NoParameterlessConstructorInClassError
import com.harana.sdk.shared.models.flow.parameters.Parameters
import io.circe.Json
import io.circe.syntax.EncoderOps
import org.apache.hadoop.fs.Path

trait ParametersSerialization { self: Parameters =>

  def saveObjectWithParameters(ctx: ExecutionContext, path: String) = {
    saveMetadata(ctx, path)
    saveParameters(ctx, path)
  }

  def loadAndSetParameters(ctx: ExecutionContext, path: String): this.type =
    setParameters(loadParameters(ctx, path))

  def saveMetadata(ctx: ExecutionContext, path: String) = {
    val metadataFilePath = ParametersSerialization.metadataFilePath(path)
    val metadataJson = (ParametersSerialization.classNameKey -> this.getClass.getName).asJson
    JsonObjectPersistence.saveJsonToFile(ctx, metadataFilePath, metadataJson)
  }

  def saveParameters(ctx: ExecutionContext, path: String) = {
    val parametersFilePath = ParametersSerialization.parametersFilePath(path)
    JsonObjectPersistence.saveJsonToFile(ctx, parametersFilePath, parameterValuesToJson)
  }

  def loadParameters(ctx: ExecutionContext, path: String) =
    JsonObjectPersistence.loadJsonFromFile(ctx, ParametersSerialization.parametersFilePath(path))

  def setParameters(parametersJson: Json): this.type =
    set(parameterPairsFromJson(parametersJson): _*)
}

object ParametersSerialization {
  val classNameKey = "className"
  val parametersFileName = "parameters"
  val metadataFileName = "metadata"

  def load(ctx: ExecutionContext, path: String): Loadable = {
    null
  }

  //FIXME
//  def load(ctx: ExecutionContext, path: String): Loadable = {
//    val metadataPath = metadataFilePath(path)
//    val metadataJson = JsonObjectPersistence.loadJsonFromFile(ctx, metadataPath)
//    val className = metadataJson.hcursor.downField(classNameKey).as[String].toOption.get
//    val clazz = Class.forName(className)
//    val instance = TypeUtils.constructorForClass(clazz).getOrElse(throw NoParameterlessConstructorInClassError(clazz.getCanonicalName).toException)
//    val loadable = TypeUtils.createInstance(instance).asInstanceOf[Loadable]
//    loadable.load(ctx, path)
//  }

  def metadataFilePath(path: String) = new Path(path, metadataFileName).toString
  def parametersFilePath(path: String) = new Path(path, parametersFileName).toString
}