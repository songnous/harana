package com.harana.sdk.shared.models.flow.actiontypes.read

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.IO
import com.harana.sdk.shared.models.flow.Action0To1TypeInfo
import com.harana.sdk.shared.models.flow.actionobjects.TransformerInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.IO
import com.harana.sdk.shared.models.flow.documentation.ActionDocumentation
import com.harana.sdk.shared.models.flow.parameters.{Parameter, ParameterGroup, Parameters, StringParameter}
import com.harana.sdk.shared.models.flow.utils.Id
import izumi.reflect.Tag
import shapeless.HMap

import scala.reflect.runtime.{universe => ru}

trait ReadTransformerInfo
  extends Action0To1TypeInfo[TransformerInfo]
    with Parameters
    with ActionDocumentation {

  val id: Id = "424dc996-a471-482d-b08c-bc12849f0b68"
  val name = "read-transformer"
  val since = Version(1, 1, 0)
  val category = IO

  val sourcePathParameter = StringParameter("source")
  def getSourcePath = $(sourcePathParameter)
  def setSourcePath(value: String): this.type = set(sourcePathParameter, value)

  override val parameterGroups = List(ParameterGroup("", sourcePathParameter))

  @transient
  lazy val portO_0: Tag[TransformerInfo] = Tag[TransformerInfo]
}

object ReadTransformerInfo extends ReadTransformerInfo