package com.harana.designer.shared

import io.circe.generic.JsonCodec
import com.harana.designer.shared.JavaScriptErrors._

@JsonCodec
case class JavaScriptError(message: String,
                           source: String,
                           fileName: Option[String],
                           jsPosition: JavaScriptPosition,
                           position: JavaScriptPosition,
                           url: String,
                           cause: Option[JavaScriptCause]) {
  override def toString: String =
    s"""
       |{
       |  message: $message
       |  jsTrace: $source:${num(jsPosition.line)}:${num(jsPosition.column)}
       |  trace: ${cleanPath(str(fileName))}:${num(position.line)}:${num(position.column)}
       |  url: $url
       |  ${if (cause.nonEmpty) s"cause: ${cause.get}"}
       |}
     """.stripMargin.trim
}

@JsonCodec
case class JavaScriptCause(message: String,
                           trace: List[JavaScriptTrace],
                           cause: Option[JavaScriptCause]) {
  override def toString: String =
    s"""
       |  {
       |    message: $message
       |    trace:\n${trace.map(s => s"\t$s").mkString("\n")}
       |    ${if (cause.nonEmpty) s"cause: ${cause.get}"}
       |  }
     """.stripMargin.trim
}

@JsonCodec
case class JavaScriptTrace(className: String,
                           methodName: String,
                           fileName: Option[String],
                           source: Option[String],
                           jsPosition: JavaScriptPosition,
                           position: JavaScriptPosition) {
  override def toString: String = s"${cleanPath(str(source))}:${num(position.line)}:${num(position.column)} ($className.$methodName)"
}

@JsonCodec
case class JavaScriptPosition(line: Option[Int], column: Option[Int]) {
  override def toString: String = s"Position(line: ${num(line)}, column: ${num(column)})"
}

object JavaScriptErrors {
  @inline
  def num(value: Option[Int]): String = value.map(_.toString).getOrElse("-")

  @inline
  def str(value: Option[String]): String = value.getOrElse("None")

  @inline
  def cleanPath(source: String): String =
    source
      .replace("https://raw.githubusercontent.com/", "")
      .replace("file://~/Developer/harana/", "")
}
