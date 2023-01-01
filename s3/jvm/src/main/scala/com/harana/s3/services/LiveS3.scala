package com.harana.s3.services

import com.harana.modules.ognl.Ognl.Service
import ognl.{DefaultClassResolver, OgnlRuntime, Ognl => jOgnl}
import zio.{Task, ZLayer}

import scala.jdk.CollectionConverters._

object LiveS3 {
  val layer = ZLayer.succeed(new Service {

    OgnlRuntime.setPropertyAccessor(classOf[Object], new models.OgnlObjectPropertyAccessor())

    def render(expression: String, context: Map[String, Any]): Task[Any] = {
      val ognlContext = jOgnl.createDefaultContext(context.asJava, new models.OgnlMemberAccess, new DefaultClassResolver, null)
      val ognlExpression = jOgnl.parseExpression(expression)
      Task.succeed(jOgnl.getValue(ognlExpression, ognlContext, context.asJava))
    }
  })
}