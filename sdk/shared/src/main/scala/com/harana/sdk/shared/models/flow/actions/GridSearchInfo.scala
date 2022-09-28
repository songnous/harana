package com.harana.sdk.shared.models.flow.actions

import com.harana.sdk.shared.models.common.Version
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.ML
import com.harana.sdk.shared.models.flow.Action3To1Info
import com.harana.sdk.shared.models.flow.actionobjects.{DataFrameInfo, EstimatorInfo, EvaluatorInfo}
import com.harana.sdk.shared.models.flow.actionobjects.report.Report
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory.ML.HyperOptimization
import com.harana.sdk.shared.models.flow.documentation.ActionDocumentation
import com.harana.sdk.shared.models.flow.parameters.{DynamicParameter, NumericParameter, ParameterGroup}
import com.harana.sdk.shared.models.flow.parameters.gridsearch.GridSearchParameter
import com.harana.sdk.shared.models.flow.parameters.validators.RangeValidator
import com.harana.sdk.shared.models.flow.utils.Id
import io.circe.Json

import scala.reflect.runtime.universe.TypeTag

trait GridSearchInfo
  extends Action3To1Info[EstimatorInfo, DataFrameInfo, EvaluatorInfo, Report]
    with ActionDocumentation {

  val id: Id = "9163f706-eaaf-46f6-a5b0-4114d92032b7"
  val name = "Grid Search"
  val since = Version(1, 0, 0)
  val category = HyperOptimization

  val estimatorParameters = new GridSearchParameter("Parameters of input Estimator", default = Some(Json.Null), inputPort = 0)
  def getEstimatorParameters = $(estimatorParameters)
  def setEstimatorParameters(jsValue: Json): this.type = set(estimatorParameters, jsValue)

  val evaluatorParameters = new DynamicParameter("Parameters of input Evaluator", default = Some(Json.Null), inputPort = 2)
  def getEvaluatorParameters = $(evaluatorParameters)
  def setEvaluatorParameters(jsValue: Json): this.type = set(evaluatorParameters, jsValue)

  val numberOfFoldsParameter = NumericParameter("number of folds", default = Some(2.0), validator = RangeValidator(begin = 2.0, end = Int.MaxValue, step = Some(1.0)))
  def getNumberOfFolds = $(numberOfFoldsParameter).toInt
  def setNumberOfFolds(numOfFolds: Int): this.type = set(numberOfFoldsParameter, numOfFolds.toDouble)

  override val parameterGroups = List(ParameterGroup(None, estimatorParameters, evaluatorParameters, numberOfFoldsParameter))

  lazy val portI_0: TypeTag[EstimatorInfo] = typeTag
  lazy val portI_1: TypeTag[DataFrameInfo] = typeTag
  lazy val portI_2: TypeTag[EvaluatorInfo] = typeTag
  lazy val portO_0: TypeTag[Report] = typeTag

}

object GridSearchInfo extends GridSearchInfo with UIActionInfo[GridSearchInfo] {
  def apply(pos: (Int, Int), color: Option[String] = None) = new GridSearchInfo {
    override val position = Some(pos)
    override val overrideColor = color
  }
}