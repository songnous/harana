package com.harana.sdk.backend.models.flow.inference

import com.harana.sdk.backend.models.flow.Catalog.{ActionCatalog, ActionObjectCatalog}
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrameBuilder

case class InferContext(dataFrameBuilder: DataFrameBuilder,
                        actionsCatalog: ActionCatalog,
                        actionObjectCatalog: ActionObjectCatalog)