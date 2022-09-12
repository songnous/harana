package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.models

import com.harana.sdk.shared.models.flow.actionobjects.{MultiColumnModelInfo, SparkSingleColumnParameterModelWrapperInfo, TransformerInfo}
import com.harana.sdk.shared.models.flow.parameters.Parameter

trait StringIndexerModelInfo extends TransformerInfo

trait MultiColumnStringIndexerModelInfo extends MultiColumnModelInfo with StringIndexerModelInfo {
  val id = "B50ABC20-CE76-4998-88B3-5EA5CA74C275"
  val specificParameters = Array.empty[Parameter[_]]
}

object MultiColumnStringIndexerModelInfo extends MultiColumnStringIndexerModelInfo {
  val numberOfModelsKey = "numberOfModels"
  val numberOfModelsFileName = "numberOfModels"
}

trait SingleColumnStringIndexerModelParameterInfo extends SparkSingleColumnParameterModelWrapperInfo {
  val id = "F16FACA0-A40E-4E5D-8285-3A53DA610E38"
  val specificParameters = Array.empty[Parameter[_]]
}

object SingleColumnStringIndexerModelParameterInfo extends SingleColumnStringIndexerModelParameterInfo