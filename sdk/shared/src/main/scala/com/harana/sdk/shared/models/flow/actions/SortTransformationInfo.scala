package com.harana.sdk.shared.models.flow.actions

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.SetAction
import com.harana.sdk.shared.models.flow.actionobjects.SortTransformerInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.SetAction
import com.harana.sdk.shared.models.flow.documentation.ActionDocumentation
import com.harana.sdk.shared.models.flow.utils.Id

import scala.reflect.runtime.universe.TypeTag

trait SortTransformationInfo extends TransformerAsActionInfo[SortTransformerInfo] with ActionDocumentation {

  val id: Id = "1fa337cc-26f5-4cff-bd91-517777924d66"
  val name = "Sort"
  val since = Version(1, 4, 0)
  val category = SetAction

  lazy val portO_1: TypeTag[SortTransformerInfo] = typeTag

}

object SortTransformationInfo extends SortTransformationInfo with UIActionInfo[SortTransformationInfo] {
  def apply(pos: (Int, Int), color: Option[String] = None) = new SortTransformationInfo {
    override val position = Some(pos)
    override val overrideColor = color
  }
}