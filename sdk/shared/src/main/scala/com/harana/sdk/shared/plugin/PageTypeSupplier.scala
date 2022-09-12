package com.harana.sdk.shared.plugin

import com.harana.sdk.shared.models.catalog.Page
import com.harana.sdk.shared.utils.ProgressObserver

import scala.concurrent.Future

trait PageTypeSupplier extends Service {

  def getPages(pages: List[Page], progressObserver: ProgressObserver): Future[List[(Page, Boolean, String)]]

}

object PageTypeSupplier {
  type PageTypeSupplierId = String
}
