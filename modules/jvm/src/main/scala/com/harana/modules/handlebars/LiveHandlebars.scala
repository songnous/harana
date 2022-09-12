package com.harana.modules.handlebars

import com.github.jknack.handlebars.context.{JavaBeanValueResolver, MapValueResolver, MethodValueResolver}
import com.github.jknack.handlebars._
import com.github.jknack.handlebars.io.ClassPathTemplateLoader
import com.harana.modules.handlebars.Handlebars.Service
import zio.{Task, ZLayer}

object LiveHandlebars {
  val layer = ZLayer.succeed(new Service {

    private val handlebars = {
      val l = new ClassPathTemplateLoader
      l.setPrefix("/templates/")
      l.setSuffix(".hbs")
      val hb = new Handlebars(l)
      hb.registerHelper("each", ScalaEachHelper)
      hb.infiniteLoops(true)
    }

    def renderPath(name: String, props: Map[String, Object]): Task[String] =
      Task(handlebars.compile(name)(context(props)))


    def renderString(content: String, props: Map[String, Object]): Task[String] =
      Task(handlebars.compileInline(content)(context(props)))


    private def context(props: Map[String, Object]) =
      Context
      .newBuilder(props)
      .resolver(ScalaResolver, MapValueResolver.INSTANCE, MethodValueResolver.INSTANCE, JavaBeanValueResolver.INSTANCE
      ).build()

  })
} 