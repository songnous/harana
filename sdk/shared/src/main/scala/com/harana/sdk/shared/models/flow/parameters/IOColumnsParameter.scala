package com.harana.sdk.shared.models.flow.parameters

import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.MultiColumnParameters.SingleOrMultiColumnChoice
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.MultiColumnParameters.SingleOrMultiColumnChoice
import com.harana.sdk.shared.models.flow.parameters.choice.ChoiceParameter

case class IOColumnsParameter() extends ChoiceParameter[SingleOrMultiColumnChoice]("operate on")
