package com.harana.sdk.shared.models.flow.parameters

import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.MultiColumnParameters.SingleOrMultiColumnChoice
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.MultiColumnParameters.SingleOrMultiColumnChoice
import com.harana.sdk.shared.models.flow.parameters.choice.ChoiceParameter

case class IOColumnsParameter()
    extends ChoiceParameter[SingleOrMultiColumnChoice](
      name = "operate on",
      description = Some("The input and output columns for the action.")
    )
