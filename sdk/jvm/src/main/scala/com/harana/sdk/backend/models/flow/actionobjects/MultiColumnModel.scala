package com.harana.sdk.backend.models.flow.actionobjects

import com.harana.sdk.backend.models.flow.ExecutionContext
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.MultiColumnParameters.MultiColumnInPlaceChoice
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.MultiColumnParameters.MultiColumnInPlaceChoices.MultiColumnNoInPlace
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.MultiColumnParameters.MultiColumnInPlaceChoices.MultiColumnYesInPlace
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.MultiColumnParameters.SingleOrMultiColumnChoices.MultiColumnChoice
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.SingleColumnParameters.SingleTransformInPlaceChoices.NoInPlaceChoice
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.SingleColumnParameters.SingleTransformInPlaceChoices.YesInPlaceChoice
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.{DataFrame, DataFrameColumnsGetter}
import com.harana.sdk.backend.models.flow.inference.exceptions.SelectedIncorrectColumnsNumber
import com.harana.sdk.shared.models.flow.actionobjects.MultiColumnModelInfo
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.HasSpecificParameters
import com.harana.sdk.shared.models.flow.parameters.{Parameter, ParameterMap}
import com.harana.sdk.shared.models.flow.parameters.selections.MultipleColumnSelection
import org.apache.spark.ml
import org.apache.spark.sql.types.StructType

abstract class MultiColumnModel[MD <: ml.Model[MD] { val outputCol: ml.param.Param[String] }, E <: ml.Estimator[MD] {
  val outputCol: ml.param.Param[String] }, SCW <: SparkSingleColumnParameterModelWrapper[MD, E]]
    extends SparkModelWrapper[MD, E] with MultiColumnModelInfo {

  var models: Seq[SCW] = _

  override def applyTransform(ctx: ExecutionContext, df: DataFrame) = {
    val inputColumnNames = df.getColumnNames($(multiColumnChoice.inputColumnsParameter))

    $(multiColumnChoice.inPlaceChoiceParameter) match {
      case MultiColumnYesInPlace()  =>
        models.zip(inputColumnNames).foldLeft(df) { case (partialResult, (m, inputColumnName)) =>
          replicateWithParent(m)
            .setInputColumn(inputColumnName)
            .setSingleInPlaceChoice(YesInPlaceChoice())
            ._transform(ctx, partialResult)

        }
      case no: MultiColumnNoInPlace =>
        val prefix = no.getColumnsPrefix

        models.zip(inputColumnNames).foldLeft(df) { case (partialResult, (m, inputColumnName)) =>
          val outputColumnName = DataFrameColumnsGetter.prefixedColumnName(inputColumnName, prefix)
          replicateWithParent(m)
            .setInputColumn(inputColumnName)
            .setSingleInPlaceChoice(NoInPlaceChoice().setOutputColumn(outputColumnName))
            ._transform(ctx, partialResult)
        }
    }
  }

  override def applyTransformSchema(schema: StructType) =
    if (models.isEmpty)
      None
    else {
      val inputColumnNames =
        DataFrameColumnsGetter.getColumnNames(schema, $(multiColumnChoice.inputColumnsParameter))

      if (inputColumnNames.size != models.size)
        throw SelectedIncorrectColumnsNumber($(multiColumnChoice.inputColumnsParameter), inputColumnNames, models.size).toException

      $(multiColumnChoice.inPlaceChoiceParameter) match {
        case MultiColumnYesInPlace() =>
          models.zip(inputColumnNames).foldLeft[Option[StructType]](Some(schema)) { case (partialResult, (m, inputColumnName)) =>
            partialResult.flatMap { s =>
              replicateWithParent(m)
                .setInputColumn(inputColumnName)
                .setSingleInPlaceChoice(YesInPlaceChoice())
                ._transformSchema(s)
            }
          }

        case no: MultiColumnNoInPlace =>
          val prefix = no.getColumnsPrefix
          models.zip(inputColumnNames).foldLeft[Option[StructType]](Some(schema)) { case (partialResult, (m, inputColumnName)) =>
            partialResult.flatMap { s =>
              val prefixedColumnName = DataFrameColumnsGetter.prefixedColumnName(inputColumnName, prefix)
              replicateWithParent(m)
                .setInputColumn(inputColumnName)
                .setSingleInPlaceChoice(NoInPlaceChoice().setOutputColumn(prefixedColumnName))
                ._transformSchema(s)
            }
          }
      }
    }

  override def replicate(extra: ParameterMap) = {
    val that = this.getClass.getConstructor().newInstance().asInstanceOf[this.type]
    copyValues(that, extractParameterMap(extra)).setModels(models.map(_.replicate(extra))).asInstanceOf[this.type]
  }

  private def replicateWithParent(m: SCW): SCW =
    m.replicate().setParent(m.parentEstimator).setModel(m.serializableModel)


  def setModels(models: Seq[SCW]): this.type = {
    this.models = models
    this
  }
}