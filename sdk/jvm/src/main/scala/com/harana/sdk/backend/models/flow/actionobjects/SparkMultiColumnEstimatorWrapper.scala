package com.harana.sdk.backend.models.flow.actionobjects

import com.harana.sdk.backend.models.flow.ExecutionContext
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.{DataFrame, DataFrameColumnsGetter}
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.MultiColumnParameters.MultiColumnInPlaceChoices.{MultiColumnNoInPlace, MultiColumnYesInPlace}
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.MultiColumnParameters.SingleOrMultiColumnChoices.{MultiColumnChoice, SingleColumnChoice}
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.SingleColumnParameters.SingleColumnInPlaceChoice
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.SingleColumnParameters.SingleTransformInPlaceChoices.{NoInPlaceChoice, YesInPlaceChoice}
import com.harana.sdk.shared.models.flow.parameters.selections.NameSingleColumnSelection
import com.harana.sdk.shared.models.flow.utils.TypeUtils
import org.apache.spark.ml
import org.apache.spark.sql.types.StructType

import izumi.reflect.Tag

abstract class SparkMultiColumnEstimatorWrapper[MD <: ml.Model[MD] { val outputCol: ml.param.Param[String] }, E <: ml.Estimator[MD] { val outputCol: ml.param.Param[String] }, MP <: Transformer, SMW <: SparkSingleColumnParameterModelWrapper[MD, E]
  with MP, EW <: SparkSingleColumnParameterEstimatorWrapper[MD, E, SMW], MMW <: MultiColumnModel[MD, E, SMW] with MP](
    implicit val modelWrapperTag: Tag[SMW],
    implicit val estimatorTag: Tag[E],
    implicit val modelsParentTag: Tag[MP],
    implicit val estimatorWrapperTag: Tag[EW],
    implicit val multiColumnModelTag: Tag[MMW],
    implicit val ev1: SMW <:< MP,
    implicit val ev2: MMW <:< MP
) extends MultiColumnEstimator[MP, MMW, SMW] {

  val sparkEstimatorWrapper: EW = createEstimatorWrapperInstance()

  def handleSingleColumnChoice(ctx: ExecutionContext, df: DataFrame, single: SingleColumnChoice): SMW = {

    val estimator = sparkEstimatorWrapper
      .replicate()
      .set(sparkEstimatorWrapper.inputColumnParameter -> single.getInputColumn)
      .setSingleInPlaceChoice(single.getInPlaceChoice)

    val mw = estimator._fit(ctx, df)
    mw.set(mw.inputColumnParameter -> single.getInputColumn)
      .setSingleInPlaceChoice(single.getInPlaceChoice)
  }

  def handleMultiColumnChoice(ctx: ExecutionContext, df: DataFrame, multi: MultiColumnChoice): MMW = {
    val inputColumns         = df.getColumnNames(multi.getInputColumns)
    val multiToSingleDecoder = multiInPlaceToSingleInPlace(multi) _

    val models = inputColumns.map { inputColumnName =>
      import sparkEstimatorWrapper._
      val estimator = sparkEstimatorWrapper
        .replicate()
        .set(inputColumnParameter -> NameSingleColumnSelection(inputColumnName))
        .setSingleInPlaceChoice(multiToSingleDecoder(inputColumnName))
      estimator
        ._fit(ctx, df)
        .setSingleInPlaceChoice(multiToSingleDecoder(inputColumnName))
    }

    createMultiColumnModel()
      .setModels(models)
      .setInputColumns(multi.getInputColumns)
      .setInPlaceChoice(multi.getInPlaceChoice)
  }

  def handleSingleColumnChoiceInfer(schema: Option[StructType], single: SingleColumnChoice): SMW = {
    import sparkEstimatorWrapper._

    sparkEstimatorWrapper
      .replicate()
      .set(inputColumnParameter -> single.getInputColumn)
      .setSingleInPlaceChoice(single.getInPlaceChoice)
      ._fit_infer(schema)
      .setSingleInPlaceChoice(single.getInPlaceChoice)
  }

  def handleMultiColumnChoiceInfer(schema: Option[StructType], multi: MultiColumnChoice): MMW = {

    schema.map { s =>
      val inputColumns = DataFrameColumnsGetter.getColumnNames(s, multi.getInputColumns)
      val multiToSingleDecoder = multiInPlaceToSingleInPlace(multi) _

      val models = inputColumns.map { inputColumnName =>
        import sparkEstimatorWrapper._
        sparkEstimatorWrapper
          .replicate()
          .set(inputColumnParameter -> NameSingleColumnSelection(inputColumnName))
          .setSingleInPlaceChoice(multiToSingleDecoder(inputColumnName))
          ._fit_infer(Some(s))
          .setSingleInPlaceChoice(multiToSingleDecoder(inputColumnName))
      }

      createMultiColumnModel()
        .setModels(models)
        .setInputColumns(multi.getInputColumns)
        .setInPlaceChoice(multi.getInPlaceChoice)
    }.getOrElse {
      val model = createMultiColumnModel().setModels(Seq.empty)

      val inputColumnsParamValue = multi.getOrDefaultOption(multi.inputColumnsParameter)
      val inPlaceParamValue      = multi.getOrDefaultOption(multi.inPlaceChoiceParameter)

      inputColumnsParamValue.map(v => model.set(model.multiColumnChoice.inputColumnsParameter -> v))
      inPlaceParamValue.map(v => model.set(model.multiColumnChoice.inPlaceChoiceParameter -> v))

      model
    }
  }

  def createEstimatorWrapperInstance(): EW = TypeUtils.instanceOfType(estimatorWrapperTag)
  def createMultiColumnModel(): MMW = TypeUtils.instanceOfType(multiColumnModelTag)

  private def multiInPlaceToSingleInPlace(multi: MultiColumnChoice)(inputColumnName: String) =
    multi.getInPlaceChoice match {
      case MultiColumnYesInPlace()  => YesInPlaceChoice()
      case no: MultiColumnNoInPlace => NoInPlaceChoice().setOutputColumn(DataFrameColumnsGetter.prefixedColumnName(inputColumnName, no.getColumnsPrefix))
    }
}
