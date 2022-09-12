package com.harana.sdk.shared.plugin

import com.harana.sdk.shared.models.catalog.Page
import com.harana.sdk.shared.utils.ProgressObserver

trait PageImporter extends Service {
  def importPages(pages: List[Page], progressObserver: ProgressObserver): Unit
  def requiredPageTypes[PT <: PageType]: List[PT]
}

object PageImporter {
  type PageImporterId = String
}