package com.harana.sdk.backend.models.flow.actionobjects

import com.harana.sdk.backend.models.flow.ExecutionContext
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.{DataFrame, DataFrameColumnsGetter}
import com.harana.sdk.backend.models.flow.actionobjects.multicolumn.SingleColumnTransformerUtils
import com.harana.sdk.backend.models.flow.inference.exceptions.TransformSchemaError
import com.harana.sdk.shared.models.flow.actionobjects.MultiColumnTransformerInfo
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.MultiColumnParameters.MultiColumnInPlaceChoice
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.MultiColumnParameters.MultiColumnInPlaceChoices.{MultiColumnNoInPlace, MultiColumnYesInPlace}
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.MultiColumnParameters.SingleOrMultiColumnChoices.{MultiColumnChoice, SingleColumnChoice}
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.SingleColumnParameters.SingleTransformInPlaceChoices.{NoInPlaceChoice, YesInPlaceChoice}
import org.apache.spark.sql.types.StructType

trait MultiColumnTransformer extends Transformer with MultiColumnTransformerInfo {

  def transformSingleColumn(inputColumn: String, outputColumn: String, ec: ExecutionContext, df: DataFrame): DataFrame
  def transformSingleColumnSchema(inputColumn: String, outputColumn: String, schema: StructType): Option[StructType]

  def applyTransform(ctx: ExecutionContext, df: DataFrame) =
    getSingleOrMultiChoice match {
      case single: SingleColumnChoice => handleSingleColumnChoice(ctx, df, single)
      case multi: MultiColumnChoice => handleMultiColumnChoice(ctx, df, multi)
    }

  override def applyTransformSchema(schema: StructType): Option[StructType] =
    getSingleOrMultiChoice match {
      case single: SingleColumnChoice => handleSingleColumnChoiceSchema(schema, single)
      case multi: MultiColumnChoice => handleMultiColumnChoiceSchema(schema, multi)
    }

  private def handleSingleColumnChoice(ctx: ExecutionContext, df: DataFrame, single: SingleColumnChoice) = {
    val inputColumn = df.getColumnName(single.getInputColumn)
    single.getInPlaceChoice match {
      case no: NoInPlaceChoice => transformSingleColumn(inputColumn, no.getOutputColumn, ctx, df)
      case YesInPlaceChoice() => transformSingleColumnInPlace(ctx, df, inputColumn)
    }
  }

  private def handleMultiColumnChoice(ctx: ExecutionContext, df: DataFrame, multi: MultiColumnChoice) = {
    val inputColumnsSelection = multi.getInputColumns
    val inputColumns = df.getColumnNames(inputColumnsSelection)
    val inPlaceChoice: MultiColumnInPlaceChoice = multi.getInPlaceChoice
    inPlaceChoice match {
      case MultiColumnYesInPlace() =>
        inputColumns.foldLeft(df) { case (partialResult, inputColumn) =>
          transformSingleColumnInPlace(ctx, partialResult, inputColumn)
        }
      case newColumns: MultiColumnNoInPlace =>
        inputColumns.foldLeft(df) { case (partialResult, inputColumn) =>
          val outputColumn = DataFrameColumnsGetter.prefixedColumnName(inputColumn, newColumns.getColumnsPrefix)
          transformSingleColumn(inputColumn, outputColumn, ctx, partialResult)
        }
    }
  }

  private def handleMultiColumnChoiceSchema(schema: StructType, multi: MultiColumnChoice): Option[StructType] = {
    val inputColumns = DataFrameColumnsGetter.getColumnNames(schema, multi.getInputColumns)
    val inPlaceChoice = multi.getInPlaceChoice
    val someSchema: Option[StructType] = Some(schema)
    inPlaceChoice match {
      case MultiColumnYesInPlace()  =>
        inputColumns.foldLeft(someSchema) { case (partialResult, inputColumn) =>
          partialResult.flatMap(r => transformSingleColumnSchemaInPlace(inputColumn, r))
        }
      case no: MultiColumnNoInPlace =>
        val columnPrefix = no.getColumnsPrefix
        inputColumns.foldLeft(someSchema) { case (partialResult, inputColumn) =>
          partialResult.flatMap { schema =>
            val outputColumn = DataFrameColumnsGetter.prefixedColumnName(inputColumn, columnPrefix)
            transformSingleColumnSchema(inputColumn, outputColumn, schema)
          }
        }
    }
  }

  private def handleSingleColumnChoiceSchema(schema: StructType, single: SingleColumnChoice): Option[StructType] = {
    val inputColumn = DataFrameColumnsGetter.getColumnName(schema, single.getInputColumn)
    single.getInPlaceChoice match {
      case no: NoInPlaceChoice => transformSingleColumnSchema(inputColumn, no.getOutputColumn, schema)
      case YesInPlaceChoice() => transformSingleColumnSchemaInPlace(inputColumn, schema)
    }
  }

  private def transformSingleColumnInPlace(ctx: ExecutionContext, df: DataFrame, inputColumn: String) =
    SingleColumnTransformerUtils.transformSingleColumnInPlace(
      inputColumn,
      df,
      ctx,
      outputColumn => transformSingleColumn(inputColumn, outputColumn, ctx, df)
    )

  private def transformSingleColumnSchemaInPlace(inputColumn: String, schema: StructType): Option[StructType] = {
    val temporaryColumnName = DataFrameColumnsGetter.uniqueSuffixedColumnName(inputColumn)
    val temporarySchema = transformSingleColumnSchema(inputColumn, temporaryColumnName, schema)

    temporarySchema.map { schema =>
      StructType(schema.collect {
        case field if field.name == inputColumn => schema(temporaryColumnName).copy(name = inputColumn)
        case field if field.name != temporaryColumnName => field
      })
    }
  }
}

object MultiColumnTransformer {

  def assertColumnDoesNotExist(outputColumn: String, schema: StructType) =
    if (schema.fieldNames.contains(outputColumn))
      throw TransformSchemaError(s"Output column '$outputColumn' already exists.").toException

  def assertColumnExist(inputColumn: String, schema: StructType) =
    if (!schema.fieldNames.contains(inputColumn))
      throw TransformSchemaError(s"Input column '$inputColumn' does not exist.").toException

}
