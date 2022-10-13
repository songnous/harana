package com.harana.sdk.shared.models.flow.actiontypes.write

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.Action1To0TypeInfo
import com.harana.sdk.shared.models.flow.actionobjects.TransformerInfo
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.IO
import com.harana.sdk.shared.models.flow.documentation.ActionDocumentation
import com.harana.sdk.shared.models.flow.parameters.{BooleanParameter, ParameterGroup, Parameters, StringParameter}
import com.harana.sdk.shared.models.flow.utils.Id
import izumi.reflect.Tag

import scala.reflect.runtime.{universe => ru}

trait WriteTransformerInfo extends Action1To0TypeInfo[TransformerInfo] with Parameters with ActionDocumentation {

  val id: Id = "58368deb-68d0-4657-ae3f-145160cb1e2b"
  val name = "write-transformer"
  val since = Version(1, 1, 0)
  val category = IO

  @transient
  lazy val portI_0: Tag[TransformerInfo] = Tag[TransformerInfo]

  val shouldOverwriteParameter = BooleanParameter("overwrite", default = Some(true))
  def getShouldOverwrite = $(shouldOverwriteParameter)
  def setShouldOverwrite(value: Boolean): this.type = set(shouldOverwriteParameter, value)

  val outputPathParameter = StringParameter("output-path")
  def getOutputPath = $(outputPathParameter)
  def setOutputPath(value: String): this.type = set(outputPathParameter, value)

  override val parameterGroups = List(ParameterGroup("", outputPathParameter, shouldOverwriteParameter))

}

object WriteTransformerInfo extends WriteTransformerInfo