package com.harana.sdk.backend.models.flow.actionobjects

import com.harana.sdk.backend.models.flow.ExecutionContext
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.{DataFrame, DataFrameColumnsGetter}
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.SingleColumnParameters.SingleColumnInPlaceChoice
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.SingleColumnParameters.SingleTransformInPlaceChoices.NoInPlaceChoice
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.{HasSingleInPlaceParameter, HasSpecificParameters}
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common.HasInputColumnParameter
import org.apache.spark.ml
import org.apache.spark.sql.types.StructType

import scala.reflect.runtime.universe.TypeTag

abstract class SparkSingleColumnParameterEstimatorWrapper[MD <: ml.Model[MD] { val outputCol: ml.param.Param[String] }, E <: ml.Estimator[MD] { val outputCol: ml.param.Param[String] }, MW <: SparkSingleColumnParameterModelWrapper[MD, E]](
    implicit override val modelWrapperTag: TypeTag[MW],
    implicit override val estimatorTag: TypeTag[E]
) extends SparkEstimatorWrapper[MD, E, MW]
    with HasInputColumnParameter
    with HasSingleInPlaceParameter
    with HasSpecificParameters {

  val parameters =
    Left(
      if (specificParameters == null) List(inputColumnParameter, singleInPlaceChoiceParameter)
      else List(inputColumnParameter, singleInPlaceChoiceParameter) ++ specificParameters
    )

  def setNoInPlace(outputColumn: String): this.type = setSingleInPlaceChoice(NoInPlaceChoice().setOutputColumn(outputColumn))

  override def _fit(ctx: ExecutionContext, df: DataFrame) = {
    val schema = df.schema.get
    val inputColumnName = DataFrameColumnsGetter.getColumnName(schema, $(inputColumnParameter))
    val convertedDataFrame =
      if (convertInputNumericToVector && NumericToVectorUtils.isColumnNumeric(schema, inputColumnName))
        DataFrame.fromSparkDataFrame(NumericToVectorUtils.convertDataFrame(df, inputColumnName, ctx))
      else
        df
    super._fit(ctx, convertedDataFrame)
  }

  override def _fit_infer(maybeSchema: Option[StructType]) =
    maybeSchema match {
      case Some(schema) =>
        val inputColumnName = DataFrameColumnsGetter.getColumnName(schema, $(inputColumnParameter))
        val convertedSchema =
          if (convertInputNumericToVector && NumericToVectorUtils.isColumnNumeric(schema, inputColumnName))
            NumericToVectorUtils.convertSchema(schema, inputColumnName)
          else
            schema
        super._fit_infer(Some(convertedSchema))
      case None => super._fit_infer(None)
    }
}
