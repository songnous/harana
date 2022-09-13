package com.harana.sdk.shared.models.flow.actions.read

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.IO
import com.harana.sdk.shared.models.flow.Action0To1Info
import com.harana.sdk.shared.models.flow.actionobjects.TransformerInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.IO
import com.harana.sdk.shared.models.flow.documentation.ActionDocumentation
import com.harana.sdk.shared.models.flow.parameters.{Parameters, StringParameter}
import com.harana.sdk.shared.models.flow.utils.Id

import scala.reflect.runtime.{universe => ru}

trait ReadTransformerInfo
  extends Action0To1Info[TransformerInfo]
    with Parameters
    with ActionDocumentation {

  val id: Id = "424dc996-a471-482d-b08c-bc12849f0b68"
  val name = "Read Transformer"
  val description = "Reads a Transformer from a directory"
  val since = Version(1, 1, 0)
  val category = IO

  val sourcePathParameter = StringParameter("source", Some("A path to the Transformer directory."))
  def getSourcePath = $(sourcePathParameter)
  def setSourcePath(value: String): this.type = set(sourcePathParameter, value)

  override val parameters =  Array(sourcePathParameter)

  @transient
  lazy val portO_0: ru.TypeTag[TransformerInfo] = ru.typeTag[TransformerInfo]
}

object ReadTransformerInfo extends ReadTransformerInfo