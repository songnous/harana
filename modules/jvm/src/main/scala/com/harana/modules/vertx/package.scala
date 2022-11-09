package com.harana.modules

import com.harana.modules.vertx.models._
import com.harana.modules.vertx.models.streams.{BufferReadStream, GzipReadStream, InputStreamReadStream}
import com.harana.modules.core.logger.Logger
import com.harana.modules.core.micrometer.Micrometer
import io.vertx.core.buffer.Buffer
import io.vertx.core.http.{Cookie, HttpHeaders, HttpMethod, HttpServerResponse, ServerWebSocket, WebSocket, WebSocketBase, WebSocketFrame}
import io.vertx.core.http.HttpHeaders.CONTENT_TYPE
import io.vertx.core.streams.Pump
import io.vertx.core.{AsyncResult, Handler, Promise, Vertx => VX}
import io.vertx.ext.web.templ.handlebars.HandlebarsTemplateEngine
import io.vertx.ext.web.{Router, RoutingContext}
import org.apache.logging.log4j.LogManager
import org.pac4j.core.config.{Config => Pac4jConfig}
import org.pac4j.core.context.session.SessionStore
import org.pac4j.vertx.VertxWebContext
import org.pac4j.vertx.auth.Pac4jAuthProvider
import org.pac4j.vertx.handler.impl.{SecurityHandler, SecurityHandlerOptions}
import zio.internal.Platform
import zio.{Exit, Runtime, Task, ZIO}

import java.io.{File, FileInputStream}
import scala.jdk.CollectionConverters._

package object vertx {

  val logger = LogManager.getLogger("Vertx")

  val runtime = Runtime[Unit]((), Platform.default
      .withReportFailure(cause => if (!cause.interrupted) logger.error(cause.prettyPrint)))

  @inline
  def run[A](zio: Task[A]): A =
    runtime.unsafeRun(zio)


  @inline
  def runAsync(zio: Task[_]): Unit =
    runtime.unsafeRunAsync_(zio)


  def generateResponse(vx: VX,
                       micrometer: Micrometer.Service,
                       templateEngine: HandlebarsTemplateEngine,
                       context: RoutingContext,
                       handler: RoutingContext => Task[Response],
                       auth: Boolean): Unit =
    runAsync(
      for {
        sample        <- micrometer.startTimer
        _             <- handler(context).map {
                          case Response.Buffer(buffer, gzipped, _, _, _, _) =>
                            val brs = new BufferReadStream(buffer)
                            val rs = if (gzipped) new GzipReadStream(brs) else brs
                            val pump = Pump.pump(rs, context.response())
                            rs.endHandler(_ => context.response().close())
                            pump.start()
                            rs.resume()

                          case Response.Content(content, contentType, cookies, statusCode, headers) =>
                            response(context, contentType, cookies, statusCode, headers).end(content)

                          case Response.Empty(contentType, cookies, statusCode, headers) =>
                            response(context, contentType, cookies, statusCode, headers).end()

                          case Response.File(filename, inputStream, gzipped, contentSize, contentType, cookies, statusCode, headers) =>
                            val r = response(context, contentType, cookies, statusCode, headers)
                            r.putHeader("Content-Disposition",  s"attachment; filename=$filename;")
                            r.setChunked(true)
                            if (contentSize.isDefined) r.putHeader(HttpHeaders.CONTENT_LENGTH, contentSize.get.toString)
                            val isrs = new InputStreamReadStream(inputStream, vx)
                            val rs = if (gzipped) new GzipReadStream(isrs) else isrs
                            val pump = Pump.pump(rs, r)
                            rs.endHandler(_ => {
                              r.end()
                              r.close()
                            })
                            pump.start()
                            rs.resume()

                          case Response.InputStream(inputStream, gzipped, contentSize, contentType, cookies, statusCode, headers) =>
                            val r = response(context, contentType, cookies, statusCode, headers)
                            r.setChunked(true)
                            val isrs = new InputStreamReadStream(inputStream, vx)
                            if (contentSize.isDefined) r.putHeader(HttpHeaders.CONTENT_LENGTH, contentSize.get.toString)
                            val rs = if (gzipped) new GzipReadStream(isrs) else isrs
                            val pump = Pump.pump(rs, r)
                            rs.endHandler(_ => {
                              r.end()
                              r.close()
                            })
                            pump.start()
                            rs.resume()

                          case Response.JSON(json, contentType, cookies, statusCode, headers) =>
                            response(context, contentType, cookies, statusCode, headers).end(json.toString)

                          case Response.Redirect(url, contentType, cookies, _, headers) =>
                            response(context, contentType, cookies, Some(302), headers).putHeader("location", url).end()

                          case Response.Template(path, parameters, contentType, cookies, statusCode, headers) =>
                            templateEngine.render(parameters.asJava, path, new Handler[AsyncResult[Buffer]] {
                              override def handle(result: AsyncResult[Buffer]): Unit =
                                if (result.succeeded()) response(context, contentType, cookies, statusCode, headers).end(result.result())
                                else {
                                  result.cause().printStackTrace()
                                  context.fail(result.cause())
                                }
                            })
                        }
        _           <-  micrometer.stopTimer(sample, s"route_${context.normalizedPath().substring(1).replaceAll("/", "_")}")
      } yield ()
    )                         


  def anonymousAuth(vx: VX,
                    sessionStore: SessionStore,
                    config: Pac4jConfig,
                    authProvider: Pac4jAuthProvider,
                    url: String,
                    router: Router) = {

    val options = new SecurityHandlerOptions().setClients("AnonymousClient")
    router.get(url).handler(new SecurityHandler(vx, sessionStore, config, authProvider, options))
  }


  def sendFile(file: File, vx: VX, rc: RoutingContext) = {
    val r = rc.response()
    r.putHeader("Content-Disposition", s"attachment; filename=${file.getName};")
    r.setChunked(true)
    r.putHeader(HttpHeaders.CONTENT_LENGTH, file.length().toString)
    val rs = new InputStreamReadStream(new FileInputStream(file), vx)
    val pump = Pump.pump(rs, r)
    rs.endHandler(_ => {
      r.end()
      r.close()
    })
    pump.start()
    rs.resume()
  }


  def auth(vx: VX,
           sessionStore: SessionStore,
           config: Pac4jConfig,
           authProvider: Pac4jAuthProvider,
           router: Router,
           url: String,
           handler: Handler[RoutingContext],
           clientNames: String,
           authorizerName: Option[String]): Unit = {

    var options = new SecurityHandlerOptions().setClients(clientNames)
    if (authorizerName.isDefined) options = options.setAuthorizers(authorizerName.get)
    router.get(url).handler(new SecurityHandler(vx, sessionStore, config, authProvider, options))
    router.get(url).handler(setContentType(ContentType.HTML.value))
    router.get(url).handler(handler)
  }


  def setContentType(contentType: String): Handler[RoutingContext] =
    (rc: RoutingContext) => {
      rc.response.putHeader(CONTENT_TYPE, contentType)
      rc.next()
    }


  def toHandler[R, A](runtime: Runtime[_], zio: ZIO[R, _, A], environment: R) =
    (p: Promise[A]) => p.complete(runtime.unsafeRun(zio.provide(environment)))


  def getVersion = {
    var version = System.getProperty("java.version")
    if (version.startsWith("1.")) version = version.substring(2, 3)
    else {
      val dot = version.indexOf(".")
      if (dot != -1) version = version.substring(0, dot)
    }
    version.toInt
  }

  def defaultAllowedHeaders =
    Set(
      "x-requested-with",
      "Access-Control-Allow-Origin",
      "Access-Control-Allow-Methods",
      "Access-Control-Allow-Headers",
      "Access-Control-Allow-Credentials",
      "origin",
      "Content-Type",
      "accept",
      "Authorization")

  def defaultAllowedMethods =
    Set(
      HttpMethod.GET,
      HttpMethod.POST,
      HttpMethod.OPTIONS,
      HttpMethod.DELETE,
      HttpMethod.PATCH,
      HttpMethod.PUT)

  def syncSockets(source: ServerWebSocket, target: WebSocket) = {
    syncSocket(source, target)
    syncSocket(target, source)
  }

  private def syncSocket(first: WebSocketBase, second: WebSocketBase) = {
    if (!first.isClosed) {
      first.frameHandler(frame => {
        if (frame.isClose) second.close()
        if (frame.isContinuation) second.writeFrame(WebSocketFrame.continuationFrame(frame.binaryData(), frame.isFinal))

        (frame.isFinal, frame.isBinary, frame.isText) match {
          case (true, true, _) => second.writeFinalBinaryFrame(frame.binaryData())
          case (true, _, true) => second.writeFinalTextFrame(frame.textData())
          case (false, true, _) => second.writeBinaryMessage(frame.binaryData())
          case (false, _, true) => second.writeTextMessage(frame.textData())
          case (_, _, _) =>
        }
      })
    }
  }

  private def response(context: RoutingContext,
                       contentType: Option[ContentType],
                       cookies: List[Cookie],
                       statusCode: Option[Int],
                       headers: Map[String, List[String]]) = {
    val response = context.response()
    if (contentType.isDefined) response.putHeader(CONTENT_TYPE, contentType.get.value)
    cookies.foreach(response.addCookie)
    if (statusCode.isDefined) response.setStatusCode(statusCode.get)
    headers.foreach { case (k, v) => if (v.size == 1) response.putHeader(k, v.head) else response.putHeader(k, v.asJava) }
    response
  }
}