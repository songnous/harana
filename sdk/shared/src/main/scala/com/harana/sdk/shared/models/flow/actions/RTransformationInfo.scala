package com.harana.sdk.shared.models.flow.actions

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Transformation
import com.harana.sdk.shared.models.flow.actionobjects.RTransformerInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Transformation.Custom
import com.harana.sdk.shared.models.flow.documentation.ActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

import scala.reflect.runtime.universe.{TypeTag, typeTag}

trait RTransformationInfo extends TransformerAsActionInfo[RTransformerInfo] with ActionDocumentation {

  val id: Id = "b578ad31-3a5b-4b94-a8d1-4c319fac6add"
  val name = "R Transformation"
  val since = Version(1, 3, 0)
  val category = Custom

  lazy val portO_1: TypeTag[RTransformerInfo] = typeTag

}

object RTransformationInfo extends RTransformationInfo with UIActionInfo[RTransformationInfo] {
  def apply(pos: (Int, Int), color: Option[String] = None) = new RTransformationInfo {
    override val position = Some(pos)
    override val overrideColor = color
  }
}