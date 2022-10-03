package com.harana.sdk.shared.models.flow.actiontypes

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Transformation
import com.harana.sdk.shared.models.flow.actionobjects.RColumnTransformerInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Transformation.Custom
import com.harana.sdk.shared.models.flow.documentation.ActionDocumentation
import com.harana.sdk.shared.models.flow.parameters.Parameter
import com.harana.sdk.shared.models.flow.utils.Id
import shapeless.HMap

import scala.reflect.runtime.universe.{TypeTag, typeTag}

trait RColumnTransformationInfo extends TransformerAsActionInfo[RColumnTransformerInfo] with ActionDocumentation {

  val id: Id = "52e2652a-0c90-445e-87e9-a04f92ff75f0"
  val name = "r-column-transformation"
  val since = Version(1, 3, 0)
  val category = Custom

  lazy val portO_1: TypeTag[RColumnTransformerInfo] = typeTag

}

object RColumnTransformationInfo extends RColumnTransformationInfo