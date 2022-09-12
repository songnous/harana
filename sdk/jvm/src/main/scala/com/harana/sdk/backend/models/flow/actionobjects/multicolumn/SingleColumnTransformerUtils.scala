package com.harana.sdk.backend.models.flow.actionobjects.multicolumn

import com.harana.sdk.backend.models.flow.ExecutionContext
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.{DataFrame, DataFrameColumnsGetter}

trait SingleColumnTransformerUtils {

  def transformSingleColumnInPlace(inputColumn: String, dataFrame: DataFrame, ec: ExecutionContext, transform: String => DataFrame) = {
    val temporaryColumnName = DataFrameColumnsGetter.uniqueSuffixedColumnName(inputColumn)
    val temporaryDataFrame  = transform(temporaryColumnName)
    val allColumnNames      = temporaryDataFrame.sparkDataFrame.schema.map(_.name)
    val filteredColumns     = allColumnNames.collect {
      case columnName if columnName == inputColumn         => temporaryDataFrame.sparkDataFrame(temporaryColumnName).as(inputColumn)
      case columnName if columnName != temporaryColumnName => temporaryDataFrame.sparkDataFrame(columnName)
    }

    val filteredDataFrame = temporaryDataFrame.sparkDataFrame.select(filteredColumns: _*)
    DataFrame.fromSparkDataFrame(filteredDataFrame)
  }
}

object SingleColumnTransformerUtils extends SingleColumnTransformerUtils
