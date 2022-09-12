package com.harana.sdk.backend.models.flow.actionobjects

import com.harana.sdk.backend.models.flow.ExecutionContext
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.MultiColumnParameters.MultiColumnInPlaceChoices.MultiColumnNoInPlace
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.MultiColumnParameters.MultiColumnInPlaceChoices.MultiColumnYesInPlace
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.MultiColumnParameters.SingleOrMultiColumnChoices.MultiColumnChoice
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.MultiColumnParameters.SingleOrMultiColumnChoices.SingleColumnChoice
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.SingleColumnParameters.SingleTransformInPlaceChoices.NoInPlaceChoice
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.SingleColumnParameters.SingleTransformInPlaceChoices.YesInPlaceChoice
import com.harana.sdk.shared.models.flow.actionobjects.MultiColumnEstimatorInfo
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.HasSpecificParameters
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common.HasInputColumnParameter
import com.harana.sdk.shared.models.flow.parameters.IOColumnsParameter
import com.harana.sdk.shared.models.flow.parameters.selections.NameSingleColumnSelection
import org.apache.spark.sql.types.StructType

import scala.language.reflectiveCalls
import scala.reflect.runtime.universe.TypeTag

abstract class MultiColumnEstimator[T <: Transformer, MC <: T, SC <: T with HasInputColumnParameter](implicit val transformerTypeTag: TypeTag[T]
) extends Estimator[T] with MultiColumnEstimatorInfo {

  def handleSingleColumnChoice(ctx: ExecutionContext, df: DataFrame, single: SingleColumnChoice): SC
  def handleMultiColumnChoice(ctx: ExecutionContext, df: DataFrame, multi: MultiColumnChoice): MC

  override def _fit(ctx: ExecutionContext, df: DataFrame) =
    $(singleOrMultiChoiceParameter) match {
      case single: SingleColumnChoice => handleSingleColumnChoice(ctx, df, single)
      case multi: MultiColumnChoice   => handleMultiColumnChoice(ctx, df, multi)
    }

  def handleSingleColumnChoiceInfer(schema: Option[StructType], single: SingleColumnChoice): SC
  def handleMultiColumnChoiceInfer(schema: Option[StructType], multi: MultiColumnChoice): MC

  override def _fit_infer(schema: Option[StructType]) =
    $(singleOrMultiChoiceParameter) match {
      case single: SingleColumnChoice => handleSingleColumnChoiceInfer(schema, single)
      case multi: MultiColumnChoice   => handleMultiColumnChoiceInfer(schema, multi)
    }
}