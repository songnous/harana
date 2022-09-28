package com.harana.sdk.shared.models.flow.actions

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Transformation
import com.harana.sdk.shared.models.flow.actionobjects.DatetimeComposerInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Transformation.FeatureConversion
import com.harana.sdk.shared.models.flow.documentation.ActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

import scala.reflect.runtime.universe.TypeTag

trait ComposeDatetimeInfo extends TransformerAsActionInfo[DatetimeComposerInfo] with ActionDocumentation {

  val id: Id = "291cdd16-b57a-4613-abbe-3fd73011e579"
  val name = "Compose Datetime"
  val since = Version(1, 3, 0)
  val category = FeatureConversion

  lazy val portO_1: TypeTag[DatetimeComposerInfo] = typeTag

}

object ComposeDatetimeInfo extends ComposeDatetimeInfo with UIActionInfo[ComposeDatetimeInfo] {
  def apply(pos: (Int, Int), color: Option[String] = None) = new ComposeDatetimeInfo {
    override val position = Some(pos)
    override val overrideColor = color
  }
}