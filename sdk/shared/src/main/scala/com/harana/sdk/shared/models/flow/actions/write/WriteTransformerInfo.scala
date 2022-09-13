package com.harana.sdk.shared.models.flow.actions.write

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.IO
import com.harana.sdk.shared.models.flow.Action1To0Info
import com.harana.sdk.shared.models.flow.actionobjects.TransformerInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.IO
import com.harana.sdk.shared.models.flow.documentation.ActionDocumentation
import com.harana.sdk.shared.models.flow.parameters.{BooleanParameter, Parameters, StringParameter}
import com.harana.sdk.shared.models.flow.utils.Id

import scala.reflect.runtime.{universe => ru}

trait WriteTransformerInfo extends Action1To0Info[TransformerInfo] with Parameters with ActionDocumentation {

  val id: Id = "58368deb-68d0-4657-ae3f-145160cb1e2b"
  val name = "Write Transformer"
  val description = "Writes a Transformer to a directory"
  val since = Version(1, 1, 0)
  val category = IO

  val shouldOverwriteParameter = BooleanParameter("overwrite", Some("Should an existing transformer with the same name be overwritten?"))
  setDefault(shouldOverwriteParameter, true)
  def getShouldOverwrite = $(shouldOverwriteParameter)
  def setShouldOverwrite(value: Boolean): this.type = set(shouldOverwriteParameter, value)

  val outputPathParameter = StringParameter("output path", Some("The output path for writing the Transformer."))
  def getOutputPath = $(outputPathParameter)
  def setOutputPath(value: String): this.type = set(outputPathParameter, value)

  override val parameters =  Array(outputPathParameter, shouldOverwriteParameter)

  @transient
  lazy val portI_0: ru.TypeTag[TransformerInfo] = ru.typeTag[TransformerInfo]

}

object WriteTransformerInfo extends WriteTransformerInfo