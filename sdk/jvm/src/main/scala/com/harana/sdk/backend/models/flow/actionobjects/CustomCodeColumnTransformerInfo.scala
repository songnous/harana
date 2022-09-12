package com.harana.sdk.backend.models.flow.actionobjects

import com.harana.sdk.shared.models.flow.actionobjects.MultiColumnTransformerInfo
import org.apache.spark.sql.types.DataType

trait CustomCodeColumnTransformerInfo extends MultiColumnTransformerInfo {

  def getComposedCode(userCode: String, inputColumn: String, outputColumn: String, targetType: DataType): String

}