package com.harana.modules.ognl

import com.harana.modules.ognl.Ognl.Service
import com.harana.modules.ognl.models.{OgnlMemberAccess, OgnlObjectPropertyAccessor}
import ognl.{DefaultClassResolver, OgnlRuntime, Ognl => jOgnl}
import zio.{Task, ZLayer}

import scala.jdk.CollectionConverters._

object LiveOgnl {
  val layer = ZLayer.succeed(new Service {

    OgnlRuntime.setPropertyAccessor(classOf[Object], new OgnlObjectPropertyAccessor())

    def render(expression: String, context: Map[String, Any]): Task[Any] = {
      val ognlContext = jOgnl.createDefaultContext(context.asJava, new OgnlMemberAccess, new DefaultClassResolver, null)
      val ognlExpression = jOgnl.parseExpression(expression)
      Task.succeed(jOgnl.getValue(ognlExpression, ognlContext, context.asJava))
    }
  })
}