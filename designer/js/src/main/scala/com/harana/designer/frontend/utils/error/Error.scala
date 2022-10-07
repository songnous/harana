package com.harana.designer.frontend.utils.error

import com.harana.designer.frontend.utils.http.Http
import com.harana.designer.shared._
import io.circe.syntax._
import org.scalajs.dom.{ErrorEvent, document, window}
import typings.sourceMap.anon.Positionbiasnumberundefin
import typings.sourceMap.mod._

import scala.collection.mutable
import org.scalajs.macrotaskexecutor.MacrotaskExecutor.Implicits._
import scala.concurrent.Future
import scala.language.{implicitConversions, reflectiveCalls}
import scala.scalajs.js
import scala.scalajs.js.{JSON, |}
import scala.util.Try

object Error {
  private var cachedMaps = mutable.Map.empty[String, SourceMapConsumer]
  implicit def withColumnNumber(ste: StackTraceElement): StackTraceElementWithColumnNumber = ste.asInstanceOf[StackTraceElementWithColumnNumber]
  

  type StackTraceElementWithColumnNumber = StackTraceElement {
    def getColumnNumber: Int
  }


  def fromJS: js.Function5[String, String, Int, Int, Throwable | js.Error, Unit] = (message: String, source: String, line: Int, column: Int, err: Throwable | js.Error) => {
    err match {
      case t: Throwable => toError(message, source, line, column, Some(t)).foreach(e => Http.postRelative("/system/error", List(), e.asJson.noSpaces))
      case e: js.Error => toError(message, source, line, column, Some(js.JavaScriptException(e))).foreach(e => Http.postRelative("/system/error", List(), e.asJson.noSpaces))
    }
    ()
  }


  private def toError(event: ErrorEvent): Future[JavaScriptError] = {
    toError(event.message, event.filename, event.lineno, event.colno, None)
  }


  private def toError(message: String, source: String, line: Int, column: Int, error: Option[Throwable]): Future[JavaScriptError] = Future {
    toErrorInternal(cachedMaps.get(source), message, source, line, column, error)
  }


  def init(sourceMaps: List[String]): Future[List[Unit]] = {
    Future.sequence(
      sourceMaps.map { sourceMap =>
        println(s"Caching source map: ${sourceMap}")
        for {
          json              <- Http.getRelative(s"$sourceMap.map")
          rawSourceMap      =  JSON.parse(json.get).asInstanceOf[RawSourceMap]
          consumer          =  new SourceMapConsumerCls(rawSourceMap).asInstanceOf[SourceMapConsumer]
          url               =  s"${window.location.protocol}//${window.location.host}$sourceMap"
          _                 =  cachedMaps += (url -> consumer)
        } yield ()
      }
    )
  }


  def clear: Unit =
    cachedMaps.clear()


  private def toError(throwable: Throwable): Future[JavaScriptError] = {
    val message = throwable.getMessage
    val first = throwable.getStackTrace.head
    val source = first.getFileName
    val line = first.getLineNumber
    val column = 0
    toError(message, source, line, column, Some(throwable))
  }


  private def map(sourceMapConsumer: SourceMapConsumer, line: Int, column: Int): Option[MappedPosition] = Try {
    val position = Positionbiasnumberundefin(column.toDouble, line.toDouble)
    sourceMapConsumer.originalPositionFor(position).asInstanceOf[MappedPosition]
  }.toOption


  private def toErrorInternal(consumerOption: Option[SourceMapConsumer], message: String, source: String, line: Int, column: Int, error: Option[Throwable]): JavaScriptError = {
    val (fileName, sourcePosition) = consumerOption.map { consumer =>
      val sourcePosition = map(consumer, line, column)
      sourcePosition.map(_.source).getOrElse("-") -> JavaScriptPosition(sourcePosition.map(_.line.toInt), sourcePosition.map(_.column.toInt))
    }.getOrElse(source -> JavaScriptPosition(None, None))

    val cause = error.map(toCause(consumerOption, _))

    JavaScriptError(
      message = message,
      source = source,
      fileName = Option(fileName),
      jsPosition = JavaScriptPosition(Option(line), Option(column)),
      position = sourcePosition,
      url = document.location.href,
      cause = cause
    )
  }


  private def toCause(consumerOption: Option[SourceMapConsumer], throwable: Throwable): JavaScriptCause = {
    consumerOption.map { consumer =>
      val trace = throwable.getStackTrace.toList.map { element =>
        val tracePosition = map(consumer, element.getLineNumber, element.getColumnNumber)
        JavaScriptTrace(
          className = element.getClassName,
          methodName = element.getMethodName,
          fileName = Option(element.getFileName),
          source = tracePosition.map(_.source),
          jsPosition = JavaScriptPosition(Option(element.getLineNumber), Option(element.getColumnNumber)),
          position = JavaScriptPosition(tracePosition.map(_.line.toInt), tracePosition.map(_.column.toInt))
        )
      }.collect {
        case t if t.source.isDefined && t.source.get != null && !t.source.get.endsWith("scala/scalajs/runtime/StackTrace.scala") && !t.source.get.endsWith("java/lang/Throwables.scala") => t
      }

      JavaScriptCause(
        message = throwable.getLocalizedMessage,
        trace = trace,
        cause = Option(throwable.getCause).map(t => toCause(consumerOption, t))
      )
    }.getOrElse {
      val trace = throwable.getStackTrace.toList.map { element =>
        JavaScriptTrace(
          className = element.getClassName,
          methodName = element.getMethodName,
          fileName = Option(element.getFileName),
          source = None,
          jsPosition = JavaScriptPosition(Option(element.getLineNumber), Option(element.getColumnNumber)),
          position = JavaScriptPosition(None, None)
        )
      }
      JavaScriptCause(throwable.getLocalizedMessage, trace, None)
    }
  }


  private def toFuture[A](promise: typings.std.Promise[A]): Future[A] = {
    val p2 = scala.concurrent.Promise[A]()
    scala.scalajs.js.Dynamic.global.console.dir(promise)
    promise.`then`(
      (value: A) => {
        p2.success(value)
        (): Unit | js.Thenable[Unit]
      },
      (e: js.Any) => {
        p2.failure(e match {
          case th: Throwable => th
          case _ => js.JavaScriptException(e)
        })
        (): Unit | js.Thenable[Unit]
      }
    )
    p2.future
  }
}
