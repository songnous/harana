package com.harana.sdk.shared.models.flow.actions

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Filtering
import com.harana.sdk.shared.models.flow.actionobjects.MissingValuesHandlerInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Filtering
import com.harana.sdk.shared.models.flow.documentation.ActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

import scala.reflect.runtime.universe.{TypeTag, typeTag}

trait HandleMissingValuesInfo extends TransformerAsActionInfo[MissingValuesHandlerInfo] with ActionDocumentation {

  val id: Id = "d5f4e717-429f-4a28-a0d3-eebba036363a"
  val name = "Handle Missing Values"
  val since = Version(0, 4, 0)
  val category = Filtering

  lazy val portO_1: TypeTag[MissingValuesHandlerInfo] = typeTag

}

object HandleMissingValuesInfo extends HandleMissingValuesInfo