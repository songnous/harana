package com.harana.sdk.backend.models.flow.inference

import com.harana.sdk.backend.models.designer.flow.Catalog.{ActionCatalog, ActionObjectCatalog}
import com.harana.sdk.backend.models.designer.flow.actionobjects.dataframe.DataFrameBuilder
import com.harana.sdk.backend.models.flow.Catalog.{ActionCatalog, ActionObjectCatalog}

case class InferContext(dataFrameBuilder: DataFrameBuilder,
                        actionsCatalog: ActionCatalog,
                        actionObjectCatalog: ActionObjectCatalog)