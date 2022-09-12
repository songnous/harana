package com.harana.sdk.backend.models.flow.utils

import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame

object DataFrameUtils {

  def collectValues(dataFrame: DataFrame, columnName: String) =
    dataFrame.sparkDataFrame.select(columnName).rdd.map(_.get(0)).collect().toSet

}
