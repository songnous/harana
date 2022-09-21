package com.harana.sdk.shared.models.flow

import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators._
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.evaluators.{BinaryClassificationEvaluatorInfo, MulticlassClassificationEvaluatorInfo, RegressionEvaluatorInfo}
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.models._
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.transformers._
import com.harana.sdk.shared.models.flow.actionobjects._
import com.harana.sdk.shared.models.flow.actions.custom.{SinkInfo, SourceInfo}
import com.harana.sdk.shared.models.flow.actions.dataframe.DataFrameInfo
import com.harana.sdk.shared.models.flow.actions.read.{ReadDatasourceInfo, ReadTransformerInfo}
import com.harana.sdk.shared.models.flow.actions.spark.wrappers.estimators._
import com.harana.sdk.shared.models.flow.actions.spark.wrappers.evaluators.{CreateBinaryClassificationEvaluatorInfo, CreateMulticlassClassificationEvaluatorInfo, CreateRegressionEvaluatorInfo}
import com.harana.sdk.shared.models.flow.actions.spark.wrappers.transformers._
import com.harana.sdk.shared.models.flow.actions.write.{WriteDatasourceInfo, WriteTransformerInfo}
import com.harana.sdk.shared.models.flow.actions._
import com.harana.sdk.shared.models.flow.utils.Id

import scala.collection.mutable

object Catalog {

  val actionsMap = mutable.Map.empty[Id, ActionTypeInfo]
  def registerAction(ai: ActionTypeInfo) = actionsMap += ai.id -> ai

  val objectsMap = mutable.Map.empty[Id, ActionObjectInfo]
  def registerObject(ai: ActionObjectInfo) = objectsMap += ai.id -> ai

  registerAction(ReadDatasourceInfo)
  registerAction(ReadDatasourceInfo)
  registerAction(ReadTransformerInfo)
  registerAction(WriteDatasourceInfo)
  registerAction(WriteTransformerInfo)

  registerAction(SourceInfo)
  registerAction(SinkInfo)

  registerAction(EvaluateInfo)
  registerAction(FitInfo)
  registerAction(FitPlusTransformInfo)
  registerAction(TransformInfo)
  registerAction(JoinInfo)
  registerAction(SplitInfo)
  registerAction(UnionInfo)
  registerAction(SqlCombineInfo)
  registerAction(SortTransformationInfo)
  registerAction(FilterColumnsInfo)
  registerAction(FilterRowsInfo)
  registerAction(HandleMissingValuesInfo)
  registerAction(ProjectionInfo)

  registerAction(CreateCustomTransformerInfo)
  registerAction(SqlColumnTransformationInfo)
  registerAction(SqlTransformationInfo)
  registerAction(PythonColumnTransformationInfo)
  registerAction(PythonTransformationInfo)
  registerAction(RColumnTransformationInfo)
  registerAction(RTransformationInfo)
  registerAction(AssembleVectorInfo)
  registerAction(BinarizeInfo)
  registerAction(ComposeDatetimeInfo)
  registerAction(ConvertTypeInfo)

  registerAction(DCTInfo)
  registerAction(DecomposeDatetimeInfo)
  registerAction(GetFromVectorInfo)
  registerAction(NormalizeInfo)
  // FIXME - API changed in Spark 3 so was removed
  //registerAction( )(Transformation.FeatureConversion)
  registerAction(PolynomialExpandInfo)
  registerAction(QuantileDiscretizerInfo)
  registerAction(StringIndexerInfo)
  registerAction(VectorIndexerInfo)
  registerAction(MinMaxScalerInfo)
  registerAction(StandardScalerInfo)

  registerAction(ConvertToNGramsInfo)
  registerAction(CountVectorizerInfo)
  registerAction(HashingTFInfo)
  registerAction(IDFInfo)
  registerAction(RemoveStopWordsInfo)
  registerAction(TokenizeInfo)
  registerAction(TokenizeWithRegexInfo)
  registerAction(Word2VecInfo)

  registerAction(GridSearchInfo)
  registerAction(CreateAFTSurvivalRegressionInfo)
  registerAction(CreateDecisionTreeRegressionInfo)
  registerAction(CreateGBTRegressionInfo)
  registerAction(CreateIsotonicRegressionInfo)
  registerAction(CreateLinearRegressionInfo)
  registerAction(CreateRandomForestRegressionInfo)
  registerAction(CreateDecisionTreeClassifierInfo)
  registerAction(CreateGBTClassifierInfo)
  registerAction(CreateLogisticRegressionInfo)
  registerAction(CreateMultilayerPerceptronClassifierInfo)
  registerAction(CreateNaiveBayesInfo)
  registerAction(CreateRandomForestClassifierInfo)
  registerAction(CreateKMeansInfo)
  registerAction(CreateLDAInfo)

  registerAction(UnivariateFeatureSelectorInfo)
  registerAction(CreateALSInfo)
  registerAction(PCAInfo)
  registerAction(CreatePythonEvaluatorInfo)
  registerAction(CreateREvaluatorInfo)
  registerAction(CreateBinaryClassificationEvaluatorInfo)
  registerAction(CreateMulticlassClassificationEvaluatorInfo)
  registerAction(CreateRegressionEvaluatorInfo)

//  registerObject(Report)
//  registerObject(MetricValue)

  registerObject(DataFrameInfo)
  registerObject(ColumnsFiltererInfo)
  registerObject(RowsFiltererInfo)
  registerObject(MissingValuesHandlerInfo)
  registerObject(ProjectorInfo)
  registerObject(DatetimeComposerInfo)
  registerObject(DatetimeDecomposerInfo)
  registerObject(SqlTransformerInfo)
  registerObject(SqlColumnTransformerInfo)
  registerObject(PythonTransformerInfo)
  registerObject(PythonColumnTransformerInfo)
  registerObject(RTransformerInfo)
  registerObject(RColumnTransformerInfo)
  registerObject(TypeConverterInfo)
//  registerObject(CustomTransformerInfo)
  registerObject(GetFromVectorTransformerInfo)
  registerObject(SortTransformerInfo)

  // wrapped Spark ML estimators & models
  registerObject(LogisticRegressionInfo)
  registerObject(LogisticRegressionModelInfo)
  registerObject(NaiveBayesInfo)
  registerObject(NaiveBayesModelInfo)
  registerObject(AFTSurvivalRegressionInfo)
  registerObject(AFTSurvivalRegressionModelInfo)
  registerObject(ALSInfo)
  registerObject(ALSModelInfo)
  registerObject(KMeansInfo)
  registerObject(KMeansModelInfo)
  registerObject(LDAInfo)
  registerObject(LDAModelInfo)
  registerObject(GBTRegressionInfo)
  registerObject(GBTRegressionModelInfo)
  registerObject(IsotonicRegressionInfo)
  registerObject(IsotonicRegressionModelInfo)
  registerObject(LinearRegressionInfo)
  registerObject(LinearRegressionModelInfo)
  registerObject(RandomForestRegressionInfo)
  registerObject(RandomForestRegressionModelInfo)
  registerObject(DecisionTreeRegressionInfo)
  registerObject(DecisionTreeRegressionModelInfo)
  registerObject(PCAEstimatorInfo)
  registerObject(PCAModelInfo)
  registerObject(StandardScalerEstimatorInfo)
  registerObject(StandardScalerModelInfo)
  registerObject(MinMaxScalerEstimatorInfo)
  registerObject(MinMaxScalerModelInfo)
  registerObject(VectorIndexerEstimatorInfo)
  registerObject(VectorIndexerModelInfo)
  registerObject(StringIndexerEstimatorInfo)
  registerObject(MultiColumnStringIndexerModelInfo)
  registerObject(SingleColumnStringIndexerModelParameterInfo)
  registerObject(Word2VecEstimatorInfo)
  registerObject(Word2VecModelInfo)
  registerObject(CountVectorizerEstimatorInfo)
  registerObject(CountVectorizerModelInfo)
  registerObject(IDFEstimatorInfo)
  registerObject(IDFModelInfo)
  registerObject(GBTClassifierInfo)
//  registerObject(GBTClassificationModelInfo)
  registerObject(RandomForestClassifierInfo)
//  registerObject(RandomForestClassificationModelInfo)
  registerObject(DecisionTreeClassifierInfo)
//  registerObject(DecisionTreeClassificationModelInfo)
  registerObject(MultilayerPerceptronClassifierInfo)
  registerObject(MultilayerPerceptronClassifierModelInfo)
  registerObject(QuantileDiscretizerEstimatorInfo)
  registerObject(QuantileDiscretizerModelInfo)
  registerObject(UnivariateFeatureEstimatorInfo)
  registerObject(UnivariateFeatureModelInfo)

  // wrapped Spark transformers
  registerObject(BinarizerInfo)
  registerObject(DiscreteCosineTransformerInfo)
  registerObject(NGramTransformerInfo)
  registerObject(NormalizerInfo)

  // FIXME - Removed because broken in Spark 3
  //registerObject(OneHotEncoder)
  registerObject(PolynomialExpanderInfo)
  registerObject(RegexTokenizerInfo)
  registerObject(StopWordsRemoverInfo)
  registerObject(StringTokenizerInfo)
  registerObject(VectorAssemblerInfo)
  registerObject(HashingTFTransformerInfo)
  registerObject(PythonEvaluatorInfo)
  registerObject(REvaluatorInfo)

  // wrapped Spark evaluators
  registerObject(BinaryClassificationEvaluatorInfo)
  registerObject(MulticlassClassificationEvaluatorInfo)
  registerObject(RegressionEvaluatorInfo)
}