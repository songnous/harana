package com.harana.sdk.backend.models.flow.utils

import akka.actor.{ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.Future
import scala.concurrent.duration.FiniteDuration

trait Retry[T] {

  def work: Future[T]

  def retryInterval: FiniteDuration
  def retryLimit: Int
  def actorSystem: ActorSystem

  // the timeout should exceed the retryLimit * retryInterval + (retryLimit + 1) * avgWorkDuration
  // otherwise the ask in tryWork method may timeout before all the retries have been attempted
  implicit def timeout: Timeout

  def workDescription: Option[String]

  private lazy val retryActor = actorSystem.actorOf(
    Props(
      new RetryActor[T](
        retryInterval,
        retryLimit,
        work,
        workDescription
      )
    )
  )

  def tryWork: Future[T] = (retryActor ? RetryActor.Trigger).asInstanceOf[Future[T]]

}