package com.harana.sdk.shared.plugin

import com.harana.sdk.shared.models.common.{File, Image, Video}

import scala.concurrent.Future

trait StorageHandler extends Service {

  def storeFile(file: File): Future[Boolean]

	def storeImage(image: Image): Future[Boolean]

	def storeVideo(video: Video): Future[Boolean]

}

object StorageHandler {
	type StorageHandlerId = String
}