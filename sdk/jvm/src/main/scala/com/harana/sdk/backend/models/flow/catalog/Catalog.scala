package com.harana.sdk.backend.models.flow.catalog

import com.harana.sdk.backend.models.flow.actionobjects._
import com.harana.sdk.backend.models.flow.actionobjects.dataframe._
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.estimators._
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.evaluators._
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.models._
import com.harana.sdk.backend.models.flow.actionobjects.spark.wrappers.transformers._
import com.harana.sdk.backend.models.flow.actiontypes._
import com.harana.sdk.backend.models.flow.actiontypes.custom._
import com.harana.sdk.backend.models.flow.actiontypes.read._
import com.harana.sdk.backend.models.flow.actiontypes.spark.wrappers.estimators._
import com.harana.sdk.backend.models.flow.actiontypes.spark.wrappers.evaluators._
import com.harana.sdk.backend.models.flow.actiontypes.spark.wrappers.transformers._
import com.harana.sdk.backend.models.flow.actiontypes.write._
import com.harana.sdk.shared.models.flow.ActionTypeInfo
import com.harana.sdk.shared.models.flow.actionobjects._
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators._
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.evaluators._
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.models._
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.transformers._
import com.harana.sdk.shared.models.flow.actiontypes._
import com.harana.sdk.shared.models.flow.actiontypes.custom._
import com.harana.sdk.shared.models.flow.actiontypes.read._
import com.harana.sdk.shared.models.flow.actiontypes.spark.wrappers.estimators._
import com.harana.sdk.shared.models.flow.actiontypes.spark.wrappers.evaluators._
import com.harana.sdk.shared.models.flow.actiontypes.spark.wrappers.transformers._
import com.harana.sdk.shared.models.flow.actiontypes.write._
import com.harana.sdk.shared.utils.HMap
import izumi.reflect.Tag

import scala.collection.mutable

object Catalog {

  val actionsByInfoMap = mutable.Map.empty[ActionTypeInfo, () => ActionType]
  val actionsByNameMap = mutable.Map.empty[String, () => ActionTypeInfo]

  def actionType[T <: ActionTypeInfo](actionTypeInfo: T) =
    actionsByInfoMap(actionTypeInfo)

  def actionType[T <: ActionType](tag: Tag[T]) =
    actionsByNameMap(tag.closestClass.getSimpleName)().asInstanceOf[T]

  def registerActions(actions: (ActionTypeInfo, () => ActionType)*) = actions.foreach { a =>
    actionsByInfoMap += a._1 -> a._2
    actionsByNameMap += a._2().getClass.getSimpleName -> a._2
  }

  val objectsByNameMap = mutable.Map.empty[String, () => ActionObjectInfo]

  def actionObject[T <: ActionObjectInfo](tag: Tag[T]) =
    objectsByNameMap(tag.closestClass.getSimpleName)().asInstanceOf[T]

  def registerObjects(objects: (ActionObjectInfo, () => ActionObjectInfo)*) = objects.foreach { a =>
    objectsByNameMap += a._2().getClass.getSimpleName -> a._2
  }

  registerActions(
    (ReadDatasourceInfo, () => new ReadDatasource),
    (ReadTransformerInfo, () => new ReadTransformer),
    (WriteDatasourceInfo, () => new WriteDatasource),
    (WriteTransformerInfo, () => new WriteTransformer),
    (SourceInfo, () => new Source),
    (SinkInfo, () => new Sink),
    (EvaluateInfo, () => new Evaluate),
    (FitInfo, () => new Fit),
    (FitPlusTransformInfo, () => new FitPlusTransform),
    (TransformInfo, () => new Transform),
    (JoinInfo, () => new Join),
    (SplitInfo, () => new Split),
    (UnionInfo, () => new Union),
    (SqlCombineInfo, () => new SqlCombine),
    (SortTransformationInfo, () => new SortTransformation),
    (FilterColumnsInfo, () => new FilterColumns),
    (FilterRowsInfo, () => new FilterRows),
    (HandleMissingValuesInfo, () => new HandleMissingValues),
    (ProjectionInfo, () => new Projection),
    (CreateCustomTransformerInfo, () => new CreateCustomTransformer),
    (SqlColumnTransformationInfo, () => new SqlColumnTransformation),
    (SqlTransformationInfo, () => new SqlTransformation),
    (PythonColumnTransformationInfo, () => new PythonColumnTransformation),
    (PythonTransformationInfo, () => new PythonTransformation),
    (RColumnTransformationInfo, () => new RColumnTransformation),
    (RTransformationInfo, () => new RTransformation),
    (AssembleVectorInfo, () => new AssembleVector),
    (BinarizeInfo, () => new Binarize),
    (ComposeDatetimeInfo, () => new ComposeDatetime),
    (ConvertTypeInfo, () => new ConvertType),
    (DCTInfo, () => new DCT),
    (DecomposeDatetimeInfo, () => new DecomposeDatetime),
    (GetFromVectorInfo, () => new GetFromVector),
    (NormalizeInfo, () => new Normalize),
    (PolynomialExpandInfo, () => new PolynomialExpand),
    (QuantileDiscretizerInfo, () => new QuantileDiscretizer),
    (StringIndexerInfo, () => new StringIndexer),
    (VectorIndexerInfo, () => new VectorIndexer),
    (MinMaxScalerInfo, () => new MinMaxScaler),
    (StandardScalerInfo, () => new StandardScaler),
    (ConvertToNGramsInfo, () => new ConvertToNGrams),
    (CountVectorizerInfo, () => new CountVectorizer),
    (HashingTFInfo, () => new HashingTF),
    (IDFInfo, () => new IDF),
    (RemoveStopWordsInfo, () => new RemoveStopWords),
    (TokenizeInfo, () => new Tokenize),
    (TokenizeWithRegexInfo, () => new TokenizeWithRegex),
    (Word2VecInfo, () => new Word2Vec),
    (GridSearchInfo, () => new GridSearch),
    (CreateAFTSurvivalRegressionInfo, () => new CreateAFTSurvivalRegression),
    (CreateDecisionTreeRegressionInfo, () => new CreateDecisionTreeRegression),
    (CreateGBTRegressionInfo, () => new CreateGBTRegression),
    (CreateIsotonicRegressionInfo, () => new CreateIsotonicRegression),
    (CreateLinearRegressionInfo, () => new CreateLinearRegression),
    (CreateRandomForestRegressionInfo, () => new CreateRandomForestRegression),
    (CreateDecisionTreeClassifierInfo, () => new CreateDecisionTreeClassifier),
    (CreateGBTClassifierInfo, () => new CreateGBTClassifier),
    (CreateLogisticRegressionInfo, () => new CreateLogisticRegression),
    (CreateMultilayerPerceptronClassifierInfo, () => new CreateMultilayerPerceptronClassifier),
    (CreateNaiveBayesInfo, () => new CreateNaiveBayes),
    (CreateRandomForestClassifierInfo, () => new CreateRandomForestClassifier),
    (CreateKMeansInfo, () => new CreateKMeans),
    (CreateLDAInfo, () => new CreateLDA),
    (UnivariateFeatureSelectorInfo, () => new UnivariateFeatureSelector),
    (CreateALSInfo, () => new CreateALS),
    (PCAInfo, () => new PCA),
    (CreatePythonEvaluatorInfo, () => new CreatePythonEvaluator),
    (CreateREvaluatorInfo, () => new CreateREvaluator),
    (CreateBinaryClassificationEvaluatorInfo, () => new CreateBinaryClassificationEvaluator),
    (CreateMulticlassClassificationEvaluatorInfo, () => new CreateMulticlassClassificationEvaluator),
    (CreateRegressionEvaluatorInfo, () => new CreateRegressionEvaluator)
  )

  registerObjects(
    (DataFrameInfo, () => new DataFrame),
    (ColumnsFiltererInfo, () => new ColumnsFilterer),
    (RowsFiltererInfo, () => new RowsFilterer),
    (MissingValuesHandlerInfo, () => new MissingValuesHandler),
    (ProjectorInfo, () => new Projector),
    (DatetimeComposerInfo, () => new DatetimeComposer),
    (DatetimeDecomposerInfo, () => new DatetimeDecomposer),
    (SqlTransformerInfo, () => new SqlTransformer),
    (SqlColumnTransformerInfo, () => new SqlColumnTransformer),
    (PythonTransformerInfo, () => new PythonTransformer),
    (PythonColumnTransformerInfo, () => new PythonColumnTransformer),
    (RTransformerInfo, () => new RTransformer),
    (RColumnTransformerInfo, () => new RColumnTransformer),
    (TypeConverterInfo, () => new TypeConverter),
//    (CustomTransformerInfo, () => new CustomTransformer),
    (GetFromVectorTransformerInfo, () => new GetFromVectorTransformer),
    (SortTransformerInfo, () => new SortTransformer),
    (LogisticRegressionInfo, () => new LogisticRegression),
    (LogisticRegressionModelInfo, () => new LogisticRegressionModel),
    (NaiveBayesInfo, () => new NaiveBayes),
    (NaiveBayesModelInfo, () => new NaiveBayesModel),
    (AFTSurvivalRegressionInfo, () => new AFTSurvivalRegression),
    (AFTSurvivalRegressionModelInfo, () => new AFTSurvivalRegressionModel),
    (ALSInfo, () => new ALS),
    (ALSModelInfo, () => new ALSModel),
    (KMeansInfo, () => new KMeans),
    (KMeansModelInfo, () => new KMeansModel),
    (LDAInfo, () => new LDA),
    (LDAModelInfo, () => new LDAModel),
    (GBTRegressionInfo, () => new GBTRegression),
    (GBTRegressionModelInfo, () => new GBTRegressionModel),
    (IsotonicRegressionInfo, () => new IsotonicRegression),
    (IsotonicRegressionModelInfo, () => new IsotonicRegressionModel),
    (LinearRegressionInfo, () => new LinearRegression),
    (LinearRegressionModelInfo, () => new LinearRegressionModel),
    (RandomForestRegressionInfo, () => new RandomForestRegression),
    (RandomForestRegressionModelInfo, () => new RandomForestRegressionModel),
    (DecisionTreeRegressionInfo, () => new DecisionTreeRegression),
    (DecisionTreeRegressionModelInfo, () => new DecisionTreeRegressionModel),
    (PCAEstimatorInfo, () => new PCAEstimator),
    (PCAModelInfo, () => new PCAModel),
    (StandardScalerEstimatorInfo, () => new StandardScalerEstimator),
    (StandardScalerModelInfo, () => new StandardScalerModel),
    (MinMaxScalerEstimatorInfo, () => new MinMaxScalerEstimator),
    (MinMaxScalerModelInfo, () => new MinMaxScalerModel),
    (VectorIndexerEstimatorInfo, () => new VectorIndexerEstimator),
    (VectorIndexerModelInfo, () => new VectorIndexerModel),
    (StringIndexerEstimatorInfo, () => new StringIndexerEstimator),
    (MultiColumnStringIndexerModelInfo, () => new MultiColumnStringIndexerModel),
    (SingleColumnStringIndexerModelParameterInfo, () => new SingleColumnStringIndexerModelParameter),
    (Word2VecEstimatorInfo, () => new Word2VecEstimator),
    (Word2VecModelInfo, () => new Word2VecModel),
    (CountVectorizerEstimatorInfo, () => new CountVectorizerEstimator),
    (CountVectorizerModelInfo, () => new CountVectorizerModel),
    (IDFEstimatorInfo, () => new IDFEstimator),
    (IDFModelInfo, () => new IDFModel),
    (GBTClassifierInfo, () => new GBTClassifier),
//    (GBTClassificationModelInfo, () => new GBTClassificationModel),
    (RandomForestClassifierInfo, () => new RandomForestClassifier),
//    (RandomForestClassificationModelInfo, () => new RandomForestClassificationModel),
    (DecisionTreeClassifierInfo, () => new DecisionTreeClassifier),
//    (DecisionTreeClassificationModelInfo, () => new DecisionTreeClassificationModel),
    (MultilayerPerceptronClassifierInfo, () => new MultilayerPerceptronClassifier),
    (MultilayerPerceptronClassifierModelInfo, () => new MultilayerPerceptronClassifierModel),
    (QuantileDiscretizerEstimatorInfo, () => new QuantileDiscretizerEstimator),
    (QuantileDiscretizerModelInfo, () => new QuantileDiscretizerModel),
//    (UnivariateFeatureSelectorEstimatorInfo, () => new UnivariateFeatureSelectorEstimator),
    (UnivariateFeatureSelectorModelInfo, () => new UnivariateFeatureSelectorModel),
    (BinarizerInfo, () => new Binarizer),
    (DiscreteCosineTransformerInfo, () => new DiscreteCosineTransformer),
    (NGramTransformerInfo, () => new NGramTransformer),
    (NormalizerInfo, () => new Normalizer),
    (PolynomialExpanderInfo, () => new PolynomialExpander),
    (RegexTokenizerInfo, () => new RegexTokenizer),
    (StopWordsRemoverInfo, () => new StopWordsRemover),
    (StringTokenizerInfo, () => new StringTokenizer),
    (VectorAssemblerInfo, () => new VectorAssembler),
    (HashingTFTransformerInfo, () => new HashingTFTransformer),
    (PythonEvaluatorInfo, () => new PythonEvaluator),
    (REvaluatorInfo, () => new REvaluator),
    (BinaryClassificationEvaluatorInfo, () => new BinaryClassificationEvaluator),
    (MulticlassClassificationEvaluatorInfo, () => new MulticlassClassificationEvaluator),
    (RegressionEvaluatorInfo, () => new RegressionEvaluator)
  )
}
