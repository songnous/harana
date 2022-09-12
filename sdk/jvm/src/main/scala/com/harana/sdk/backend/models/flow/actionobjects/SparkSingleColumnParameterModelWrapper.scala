package com.harana.sdk.backend.models.flow.actionobjects

import com.harana.sdk.backend.models.designer.flow.actionobjects.multicolumn._
import com.harana.sdk.backend.models.flow.ExecutionContext
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.{DataFrame, DataFrameColumnsGetter}
import com.harana.sdk.backend.models.flow.actionobjects.multicolumn.SingleColumnTransformerUtils
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.SingleColumnParameters.SingleTransformInPlaceChoices.{NoInPlaceChoice, YesInPlaceChoice}
import com.harana.sdk.shared.models.flow.actionobjects.SparkSingleColumnParameterModelWrapperInfo
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.{HasSingleInPlaceParameter, HasSpecificParameters}
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common.HasInputColumnParameter
import com.harana.sdk.shared.models.flow.parameters.ParameterMap
import org.apache.spark.ml
import org.apache.spark.ml.param.{Params, ParamMap => SparkParamMap}
import org.apache.spark.sql.types.StructType

import scala.language.reflectiveCalls

abstract class SparkSingleColumnParameterModelWrapper[MD <: ml.Model[MD] {val outputCol: ml.param.Param[String]}, E <: ml.Estimator[MD] { val outputCol: ml.param.Param[String] }]
  extends SparkModelWrapper[MD, E] with SparkSingleColumnParameterModelWrapperInfo {

  private var outputColumnValue: Option[String] = None

  override def applyTransform(ctx: ExecutionContext, df: DataFrame) = {
    val schema = df.schema.get
    val inputColumnName = DataFrameColumnsGetter.getColumnName(schema, $(inputColumnParameter))
    val conversionDoubleToVectorIsNecessary = convertInputNumericToVector && NumericToVectorUtils.isColumnNumeric(schema, inputColumnName)
    val convertedDataFrame = if (conversionDoubleToVectorIsNecessary) DataFrame.fromSparkDataFrame(NumericToVectorUtils.convertDataFrame(df, inputColumnName, ctx)) else df

    val transformedDataFrame = $(singleInPlaceChoiceParameter) match {
      case YesInPlaceChoice()  =>
        SingleColumnTransformerUtils.transformSingleColumnInPlace(
          convertedDataFrame.getColumnName($(inputColumnParameter)),
          convertedDataFrame,
          ctx,
          transformTo(ctx, convertedDataFrame)
        )
      case no: NoInPlaceChoice => transformTo(ctx, convertedDataFrame)(no.getOutputColumn)
    }

    if (conversionDoubleToVectorIsNecessary && convertOutputVectorToDouble) {
      val expectedSchema = applyTransformSchema(schema)
      val revertedTransformedDf =
        NumericToVectorUtils.revertDataFrame(
          transformedDataFrame.sparkDataFrame,
          expectedSchema.get,
          inputColumnName,
          outputColumnName(inputColumnName),
          ctx,
          convertOutputVectorToDouble
        )
      DataFrame.fromSparkDataFrame(revertedTransformedDf)
    } else
      transformedDataFrame
  }

  override def applyTransformSchema(schema: StructType): Option[StructType] = {
    val inputColumnName = DataFrameColumnsGetter.getColumnName(schema, $(inputColumnParameter))
    val conversionDoubleToVectorIsNecessary = convertInputNumericToVector && NumericToVectorUtils.isColumnNumeric(schema, inputColumnName)
    val convertedSchema = if (conversionDoubleToVectorIsNecessary) NumericToVectorUtils.convertSchema(schema, inputColumnName) else schema

    val transformedSchemaOption = $(singleInPlaceChoiceParameter) match {
      case YesInPlaceChoice()  =>
        val temporaryColumnName = DataFrameColumnsGetter.uniqueSuffixedColumnName(inputColumnName)
        val temporarySchema: Option[StructType] = transformSchemaTo(convertedSchema, temporaryColumnName)

        temporarySchema.map { schema =>
          StructType(schema.collect {
            case field if field.name == inputColumnName => schema(temporaryColumnName).copy(name = inputColumnName)
            case field if field.name != temporaryColumnName => field
          })
        }
      case no: NoInPlaceChoice => transformSchemaTo(convertedSchema, no.getOutputColumn)
    }

    if (conversionDoubleToVectorIsNecessary && convertOutputVectorToDouble) {
      transformedSchemaOption.map { transformedSchema =>
        NumericToVectorUtils.revertSchema(
          transformedSchema,
          inputColumnName,
          outputColumnName(inputColumnName),
          convertOutputVectorToDouble
        )
      }
    } else
      transformedSchemaOption
  }

  override def sparkParamMap(sparkEntity: Params, schema: StructType): SparkParamMap = {
    val map = super.sparkParamMap(sparkEntity, schema).put(ml.param.ParamPair(parentEstimator.sparkEstimator.outputCol, outputColumnValue.orNull))
    if (serializableModel != null) map.put(ml.param.ParamPair(sparkModel.outputCol, outputColumnValue.orNull)) else map
  }

  private def transformTo(ctx: ExecutionContext, df: DataFrame)(outputColumnName: String) =
    withOutputColumnValue(outputColumnName) {
      super.applyTransform(ctx, df)
    }

  private def transformSchemaTo(schema: StructType, temporaryColumnName: String) =
    withOutputColumnValue(temporaryColumnName) {
      super.applyTransformSchema(schema)
    }

  private def withOutputColumnValue[T](columnName: String)(f: => T): T = {
    outputColumnValue = Some(columnName)
    try
      f
    finally
      outputColumnValue = None
  }

  private def outputColumnName(inputColumnName: String) =
    $(singleInPlaceChoiceParameter) match {
      case YesInPlaceChoice()  => inputColumnName
      case no: NoInPlaceChoice => no.getOutputColumn
    }

  override def replicate(extra: ParameterMap): SparkSingleColumnParameterModelWrapper.this.type = {
    val model = super.replicate(extractParameterMap(extra))
    model.outputColumnValue = outputColumnValue
    model
  }
}
