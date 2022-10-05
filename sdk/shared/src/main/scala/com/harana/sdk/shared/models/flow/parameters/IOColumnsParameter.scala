package com.harana.sdk.shared.models.flow.parameters

import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.MultiColumnParameters.SingleOrMultiColumnChoice
import com.harana.sdk.shared.models.flow.parameters.choice.ChoiceParameter
import io.circe.generic.JsonCodec

case class IOColumnsParameter(override val required: Boolean = false,
                              override val default: Option[SingleOrMultiColumnChoice] = None) extends ChoiceParameter[SingleOrMultiColumnChoice]("operate on")
