package com.harana.sdk.shared.plugin

import com.harana.sdk.shared.models.catalog.Page
import com.harana.sdk.shared.utils.ProgressObserver

import scala.concurrent.Future

trait PageExporter extends Service {
  def exportPages(pages: List[Page], progressObserver: ProgressObserver): Future[List[(Page, Boolean, String)]]
}

object PageExporter {
  type PageExporterId = String
}