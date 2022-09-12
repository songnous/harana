package com.harana.executor

import com.harana.modules.core.{ Layers => CoreLayers }
import com.harana.modules.core.logger.Logger
import okhttp3.{OkHttpClient, Request, Response}
import zio.duration.durationInt
import zio.{Schedule, Task, UIO}

package object spark {

  private val httpClient = new OkHttpClient

  def everySecond =
    Schedule.spaced(1.second).forever

  def get(url: String): Task[String] =
    Task(httpClient.newCall(new Request.Builder().url(url).build()).execute.body().string)

  def httpCall(url: String, request: Request.Builder => Request.Builder): Task[Response] =
    Task(httpClient.newCall(request(new Request.Builder().url(url)).build()).execute)

  def logDebug(s: String): UIO[Unit] =
    Logger.info(s).provideLayer(CoreLayers.logger)

  def logError(s: String): UIO[Unit] =
    Logger.error(s).provideLayer(CoreLayers.logger)
}