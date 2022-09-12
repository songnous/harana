package com.harana.sdk.backend.models.flow.actionobjects.serialization

import com.harana.sdk.backend.models.flow.ExecutionContext
import io.circe.{Json, parser}

object JsonObjectPersistence {

  def loadJsonFromFile(ctx: ExecutionContext, path: String): Json =
    parser.parse(ctx.sparkContext.textFile(path, 1).first()).toOption.get

  def saveJsonToFile(ctx: ExecutionContext, path: String, json: Json) =
    ctx.sparkContext.parallelize(Seq(json.noSpaces), 1).saveAsTextFile(path)

}