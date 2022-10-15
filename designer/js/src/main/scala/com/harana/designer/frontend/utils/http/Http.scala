package com.harana.designer.frontend.utils.http

import io.circe.parser._
import io.circe.{Decoder, Json}
import org.scalajs.dom.window
import sttp.capabilities
import sttp.client3._
import sttp.model.{Header, Method}

import org.scalajs.macrotaskexecutor.MacrotaskExecutor.Implicits._
import scala.concurrent.Future

object Http {

  implicit val sttpBackend = new MetricWrapper[capabilities.WebSockets](FetchBackend(), new CloudMetricsServer)

  def getRelative(suffix: String, headers: List[Header] = List()): Future[Option[String]] = requestRelative(suffix, Method.GET, headers)
  def getRelativeAs[T](suffix: String, headers: List[Header] = List())(implicit decoder: Decoder[T]): Future[Option[T]] = requestRelativeAs[T](suffix, Method.GET, headers)(decoder)
  def getRelativeAsJson(url: String, headers: List[Header] = List()): Future[Option[Json]] = requestRelativeAsJson(url, Method.GET, headers)
  def get(url: String, headers: List[Header] = List()): Future[Option[String]] = request(url, Method.GET, headers)
  def getAs[T](url: String, headers: List[Header] = List())(implicit decoder: Decoder[T]): Future[Option[T]] = requestAs[T](url, Method.GET, headers)(decoder)
  def getAsJson(url: String, headers: List[Header] = List()): Future[Option[Json]] = requestAsJson(url, Method.GET, headers)

  def deleteRelative(suffix: String, headers: List[Header] = List(), body: Option[String] = None): Future[Unit] =
    if (body.isDefined) requestRelativeWithBody(suffix, Method.DELETE, headers, body.get) else requestRelative(suffix, Method.DELETE, headers).map(_ => ())

  def post(url: String, headers: List[Header] = List(), body: String): Future[Unit] = requestWithBody(url, Method.POST, headers, body)
  def postRelative(suffix: String, headers: List[Header] = List(), body: String): Future[Unit] = requestRelativeWithBody(suffix, Method.POST, headers, body)
  def put(url: String, headers: List[Header] = List(), body: String): Future[Unit] = requestWithBody(url, Method.PUT, headers, body)
  def putRelative(suffix: String, headers: List[Header] = List(), body: String): Future[Unit] = requestRelativeWithBody(suffix, Method.PUT, headers, body)
  def putRelativeAs[T](suffix: String, headers: List[Header] = List(), body: String)(implicit decoder: Decoder[T]): Future[Option[T]] = requestRelativeWithBodyAs[T](suffix, Method.PUT, headers, body)(decoder)


  private def requestWithBody(url: String, method: Method, headers: List[Header] = List(), body: String): Future[Unit] =
    basicRequest.method(method, uri"$url").headers(headers: _*).body(body).send().map(_ => ())


  private def requestRelativeWithBody(suffix: String, method: Method, headers: List[Header] = List(), body: String): Future[Unit] = {
    val url = s"${window.location.protocol}//${window.location.host}$suffix"
    basicRequest.method(method, uri"$url").headers(headers: _*).body(body).send().map { response =>
      if (!response.isSuccess) {
        println(response.toString)
      }}
  }

  private def requestRelativeWithBodyAs[T](suffix: String, method: Method, headers: List[Header] = List(), body: String)(implicit decoder: Decoder[T]): Future[Option[T]] = {
    val url = s"${window.location.protocol}//${window.location.host}$suffix"
    basicRequest.method(method, uri"$url").headers(headers: _*).body(body).send().map(decodeResponse(suffix, _)(decoder))
  }


  private def requestRelative(suffix: String, method: Method, headers: List[Header] = List()): Future[Option[String]] = {
    val url = s"${window.location.protocol}//${window.location.host}$suffix"
    request(s"$url", method, headers)
  }


  private def requestRelativeAsJson(suffix: String, method: Method, headers: List[Header] = List()): Future[Option[Json]] = {
    val url = s"${window.location.protocol}//${window.location.host}$suffix"
    requestAsJson(s"$url", method, headers)
  }


  private def requestRelativeAs[T](suffix: String, method: Method, headers: List[Header] = List())(implicit decoder: Decoder[T]): Future[Option[T]] = {
    val url = s"${window.location.protocol}//${window.location.host}$suffix"
    requestAs[T](s"$url", method, headers)
  }


  private def request(url: String, method: Method, headers: List[Header] = List()): Future[Option[String]] = {
    basicRequest.method(method, uri"$url").headers(headers: _*).response(asString).send().map(_.body.toOption)
  }


  private def requestAs[T](url: String, method: Method, headers: List[Header] = List())(implicit decoder: Decoder[T]): Future[Option[T]] =
    basicRequest.method(method, uri"$url").headers(headers: _*).response(asString).send().map(decodeResponse(url, _)(decoder))


  private def requestAsJson(url: String, method: Method, headers: List[Header] = List()): Future[Option[Json]] = {
    val request = basicRequest.method(method, uri"$url").headers(headers: _*).send().map(_.body)
    request.onComplete { response =>
      response.get match {
        case Left(e) => println(e)
        case Right(s) => {}
      }
    }
    request.map(r => {
      parse(r.toOption.get).toOption
    })
  }

  private def decodeResponse[T](url: String, response: Response[Either[String, String]])(implicit decoder: Decoder[T]): Option[T] = {
    if (response.isSuccess)
      decode[T](response.body.toOption.get) match {
        case Left(e) =>
          println(s"Failed decoding JSON: $url - ${e.getMessage}")
          throw new Exception(e)

        case Right(result) =>
          Some(result)
      }
    else
      None
  }
}