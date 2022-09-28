package com.harana.sdk.shared.models.flow.actions

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Transformation
import com.harana.sdk.shared.models.flow.actionobjects.DatetimeDecomposerInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Transformation.FeatureConversion
import com.harana.sdk.shared.models.flow.documentation.ActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

import scala.reflect.runtime.universe.TypeTag

trait DecomposeDatetimeInfo extends TransformerAsActionInfo[DatetimeDecomposerInfo] with ActionDocumentation {

  val id: Id = "6c18b05e-7db7-4315-bce1-3291ed530675"
  val name = "Decompose Datetime"
  val since = Version(0, 4, 0)
  val category = FeatureConversion

  lazy val portO_1: TypeTag[DatetimeDecomposerInfo] = typeTag

}

object DecomposeDatetimeInfo extends DecomposeDatetimeInfo with UIActionInfo[DecomposeDatetimeInfo] {
  def apply(pos: (Int, Int), color: Option[String] = None) = new DecomposeDatetimeInfo {
    override val position = Some(pos)
    override val overrideColor = color
  }
}