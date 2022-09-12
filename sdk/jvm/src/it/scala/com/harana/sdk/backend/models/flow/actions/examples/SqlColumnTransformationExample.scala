package com.harana.sdk.backend.models.flow.actions.examples

import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.MultiColumnParameters.SingleOrMultiColumnChoices.SingleColumnChoice
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.SingleColumnParameters.SingleTransformInPlaceChoices.NoInPlaceChoice
import com.harana.sdk.backend.models.flow.actions.SqlColumnTransformation
import com.harana.sdk.shared.models.flow.parameters.selections.NameSingleColumnSelection

class SqlColumnTransformationExample extends AbstractActionExample[SqlColumnTransformation] {

  def action: SqlColumnTransformation = {
    val o = SqlColumnTransformation()
    val myalias = "myAlias"
    val inPlace = NoInPlaceChoice().setOutputColumn("WeightCutoff")
    val single = SingleColumnChoice().setInputColumn(NameSingleColumnSelection("Weight")).setInPlaceChoice(inPlace)
    o.transformer.setFormula("MINIMUM(" + myalias + ", 2.0)").setInputColumnAlias(myalias).setSingleOrMultiChoice(single)
    o.set(o.transformer.extractParameterMap())
  }

  override def fileNames = Seq("example_animals")

}
