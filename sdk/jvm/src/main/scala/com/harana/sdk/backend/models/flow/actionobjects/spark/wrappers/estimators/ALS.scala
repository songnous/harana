package com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators

import com.harana.sdk.backend.models.flow.actionobjects.SparkEstimatorWrapper
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrameColumnsGetter
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.models.ALSModel
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators.ALSInfo
import com.harana.sdk.shared.models.flow.utils.ColumnType
import org.apache.spark.ml.recommendation.{ALS => SparkALS, ALSModel => SparkALSModel}
import org.apache.spark.sql.types.StructType

class ALS
    extends SparkEstimatorWrapper[SparkALSModel, SparkALS, ALSModel]
    with ALSInfo {

  override def _fit_infer(maybeSchema: Option[StructType]): ALSModel = {
    maybeSchema.foreach { schema =>
      DataFrameColumnsGetter.assertExpectedColumnType(schema, getItemColumn, ColumnType.Numeric)
      DataFrameColumnsGetter.assertExpectedColumnType(schema, getUserColumn, ColumnType.Numeric)
      DataFrameColumnsGetter.assertExpectedColumnType(schema, getRatingColumn, ColumnType.Numeric)
    }
    super._fit_infer(maybeSchema)
  }
}