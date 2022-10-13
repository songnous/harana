package com.harana.sdk.shared.models.flow.catalog

import com.harana.sdk.shared.models.flow.actionobjects._
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.estimators._
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.evaluators._
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.models._
import com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.transformers._

import scala.scalajs.reflect.annotation.EnableReflectiveInstantiation

@EnableReflectiveInstantiation
class DataFrame extends DataFrameInfo

@EnableReflectiveInstantiation
class ColumnsFilterer extends ColumnsFiltererInfo

@EnableReflectiveInstantiation
class RowsFilterer extends RowsFiltererInfo

@EnableReflectiveInstantiation
class MissingValuesHandler extends MissingValuesHandlerInfo

@EnableReflectiveInstantiation
class Projector extends ProjectorInfo

@EnableReflectiveInstantiation
class DatetimeComposer extends DatetimeComposerInfo

@EnableReflectiveInstantiation
class DatetimeDecomposer extends DatetimeDecomposerInfo

@EnableReflectiveInstantiation
class SqlTransformer extends SqlTransformerInfo

@EnableReflectiveInstantiation
class SqlColumnTransformer extends SqlColumnTransformerInfo

@EnableReflectiveInstantiation
class PythonTransformer extends PythonTransformerInfo

@EnableReflectiveInstantiation
class PythonColumnTransformer extends PythonColumnTransformerInfo

@EnableReflectiveInstantiation
class RTransformer extends RTransformerInfo

@EnableReflectiveInstantiation
class RColumnTransformer extends RColumnTransformerInfo

@EnableReflectiveInstantiation
class TypeConverter extends TypeConverterInfo

@EnableReflectiveInstantiation
class CustomTransformer extends CustomTransformerInfo

@EnableReflectiveInstantiation
class GetFromVectorTransformer extends GetFromVectorTransformerInfo

@EnableReflectiveInstantiation
class SortTransformer extends SortTransformerInfo

@EnableReflectiveInstantiation
class LogisticRegression extends LogisticRegressionInfo

@EnableReflectiveInstantiation
class LogisticRegressionModel extends LogisticRegressionModelInfo

@EnableReflectiveInstantiation
class NaiveBayes extends NaiveBayesInfo

@EnableReflectiveInstantiation
class NaiveBayesModel extends NaiveBayesModelInfo

@EnableReflectiveInstantiation
class AFTSurvivalRegression extends AFTSurvivalRegressionInfo

@EnableReflectiveInstantiation
class AFTSurvivalRegressionModel extends AFTSurvivalRegressionModelInfo

@EnableReflectiveInstantiation
class ALS extends ALSInfo

@EnableReflectiveInstantiation
class ALSModel extends ALSModelInfo

@EnableReflectiveInstantiation
class KMeans extends KMeansInfo

@EnableReflectiveInstantiation
class KMeansModel extends KMeansModelInfo

@EnableReflectiveInstantiation
class LDA extends LDAInfo

@EnableReflectiveInstantiation
class LDAModel extends LDAModelInfo

@EnableReflectiveInstantiation
class GBTRegression extends GBTRegressionInfo

@EnableReflectiveInstantiation
class GBTRegressionModel extends GBTRegressionModelInfo

@EnableReflectiveInstantiation
class IsotonicRegression extends IsotonicRegressionInfo

@EnableReflectiveInstantiation
class IsotonicRegressionModel extends IsotonicRegressionModelInfo

@EnableReflectiveInstantiation
class LinearRegression extends LinearRegressionInfo

@EnableReflectiveInstantiation
class LinearRegressionModel extends LinearRegressionModelInfo

@EnableReflectiveInstantiation
class RandomForestRegression extends RandomForestRegressionInfo

@EnableReflectiveInstantiation
class RandomForestRegressionModel extends RandomForestRegressionModelInfo

@EnableReflectiveInstantiation
class DecisionTreeRegression extends DecisionTreeRegressionInfo

@EnableReflectiveInstantiation
class DecisionTreeRegressionModel extends DecisionTreeRegressionModelInfo

@EnableReflectiveInstantiation
class PCAEstimator extends PCAEstimatorInfo {
  override val parameterGroups = List.empty
}

@EnableReflectiveInstantiation
class PCAModel extends PCAModelInfo {
  override val parameterGroups = List.empty
}

@EnableReflectiveInstantiation
class StandardScalerEstimator extends StandardScalerEstimatorInfo {
  override val parameterGroups = List.empty
}

@EnableReflectiveInstantiation
class StandardScalerModel extends StandardScalerModelInfo {
  override val parameterGroups = List.empty
}

@EnableReflectiveInstantiation
class MinMaxScalerEstimator extends MinMaxScalerEstimatorInfo {
  override val parameterGroups = List.empty
}

@EnableReflectiveInstantiation
class MinMaxScalerModel extends MinMaxScalerModelInfo {
  override val parameterGroups = List.empty
}

@EnableReflectiveInstantiation
class VectorIndexerEstimator extends VectorIndexerEstimatorInfo {
  override val parameterGroups = List.empty
}

@EnableReflectiveInstantiation
class VectorIndexerModel extends VectorIndexerModelInfo {
  override val parameterGroups = List.empty
}

@EnableReflectiveInstantiation
class StringIndexerEstimator extends StringIndexerEstimatorInfo

@EnableReflectiveInstantiation
class MultiColumnStringIndexerModel extends MultiColumnStringIndexerModelInfo

@EnableReflectiveInstantiation
class SingleColumnStringIndexerModelParameter extends SingleColumnStringIndexerModelParameterInfo

@EnableReflectiveInstantiation
class Word2VecEstimator extends Word2VecEstimatorInfo {
  override val parameterGroups = List.empty
}

@EnableReflectiveInstantiation
class Word2VecModel extends Word2VecModelInfo {
  override val parameterGroups = List.empty
}

@EnableReflectiveInstantiation
class CountVectorizerEstimator extends CountVectorizerEstimatorInfo {
  override val parameterGroups = List.empty
}

@EnableReflectiveInstantiation
class CountVectorizerModel extends CountVectorizerModelInfo {
  override val parameterGroups = List.empty
}

@EnableReflectiveInstantiation
class IDFEstimator extends IDFEstimatorInfo {
  override val parameterGroups = List.empty
}

@EnableReflectiveInstantiation
class IDFModel extends IDFModelInfo {
  override val parameterGroups = List.empty
}

@EnableReflectiveInstantiation
class GBTClassifier extends GBTClassifierInfo

 @EnableReflectiveInstantiation
class GBTClassificationModel extends GBTClassificationModelInfo

@EnableReflectiveInstantiation
class RandomForestClassifier extends RandomForestClassifierInfo

@EnableReflectiveInstantiation
class RandomForestClassificationModel extends RandomForestClassificationModelInfo

@EnableReflectiveInstantiation
class DecisionTreeClassifier extends DecisionTreeClassifierInfo

 @EnableReflectiveInstantiation
class DecisionTreeClassificationModel extends DecisionTreeClassificationModelInfo

@EnableReflectiveInstantiation
class MultilayerPerceptronClassifier extends MultilayerPerceptronClassifierInfo

@EnableReflectiveInstantiation
class MultilayerPerceptronClassifierModel extends MultilayerPerceptronClassifierModelInfo

@EnableReflectiveInstantiation
class QuantileDiscretizerEstimator extends QuantileDiscretizerEstimatorInfo {
  override val parameterGroups = List.empty
}

@EnableReflectiveInstantiation
class QuantileDiscretizerModel extends QuantileDiscretizerModelInfo {
  override val parameterGroups = List.empty
}

@EnableReflectiveInstantiation
class UnivariateFeatureSelectorEstimator extends UnivariateFeatureSelectorEstimatorInfo

@EnableReflectiveInstantiation
class UnivariateFeatureModel extends UnivariateFeatureSelectorModelInfo

@EnableReflectiveInstantiation
class Binarizer extends BinarizerInfo {
  override val parameterGroups = List.empty
}

@EnableReflectiveInstantiation
class DiscreteCosineTransformer extends DiscreteCosineTransformerInfo {
  override val parameterGroups = List.empty
}

@EnableReflectiveInstantiation
class NGramTransformer extends NGramTransformerInfo {
  override val parameterGroups = List.empty
}

@EnableReflectiveInstantiation
class Normalizer extends NormalizerInfo {
  override val parameterGroups = List.empty
}

@EnableReflectiveInstantiation
class PolynomialExpander extends PolynomialExpanderInfo {
  override val parameterGroups = List.empty
}

@EnableReflectiveInstantiation
class RegexTokenizer extends RegexTokenizerInfo {
  override val parameterGroups = List.empty
}

@EnableReflectiveInstantiation
class StopWordsRemover extends StopWordsRemoverInfo {
  override val parameterGroups = List.empty
}

@EnableReflectiveInstantiation
class StringTokenizer extends StringTokenizerInfo {
  override val parameterGroups = List.empty
}

@EnableReflectiveInstantiation
class VectorAssembler extends VectorAssemblerInfo

@EnableReflectiveInstantiation
class HashingTFTransformer extends HashingTFTransformerInfo {
  override val parameterGroups = List.empty
}

@EnableReflectiveInstantiation
class PythonEvaluator extends PythonEvaluatorInfo

@EnableReflectiveInstantiation
class REvaluator extends REvaluatorInfo

@EnableReflectiveInstantiation
class BinaryClassificationEvaluator extends BinaryClassificationEvaluatorInfo

@EnableReflectiveInstantiation
class MulticlassClassificationEvaluator extends MulticlassClassificationEvaluatorInfo

@EnableReflectiveInstantiation
class RegressionEvaluator extends RegressionEvaluatorInfo