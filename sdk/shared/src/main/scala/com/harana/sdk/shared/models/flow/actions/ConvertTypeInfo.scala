package com.harana.sdk.shared.models.flow.actions

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.actionobjects.TypeConverterInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.Transformation.FeatureConversion
import com.harana.sdk.shared.models.flow.documentation.ActionDocumentation

import scala.reflect.runtime.universe.TypeTag

trait ConvertTypeInfo extends TransformerAsActionInfo[TypeConverterInfo] with ActionDocumentation {

  val id = "04084863-fdda-46fd-b1fe-796c6b5a0967"
  val name = "Convert Type"
  val since = Version(0, 4, 0)
  val category = FeatureConversion

  lazy val portO_1: TypeTag[TypeConverterInfo] = typeTag
}

object ConvertTypeInfo extends ConvertTypeInfo with UIActionInfo[ConvertTypeInfo] {
  def apply(pos: (Int, Int), color: Option[String] = None) = new ConvertTypeInfo {
    override val position = Some(pos)
    override val overrideColor = color
  }
}