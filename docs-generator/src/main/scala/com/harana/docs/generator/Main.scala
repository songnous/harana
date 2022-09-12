package com.harana.docs.generator

import com.harana.sdk.shared.models.flow.Catalog
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory
import zio.ZIO

class Main {

 def generateCategory(category: ActionCategory) =
  for {
   actions  <- ZIO.from(Catalog.actionsMap.values.filter(_.category == category).toSeq.sortBy(_.name))


  } yield ()

}