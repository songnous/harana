package com.harana.sdk.backend.models.flow.actions

import org.apache.spark.sql.types._
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.MultiColumnParameters.MultiColumnInPlaceChoices.MultiColumnNoInPlace
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.MultiColumnParameters.SingleOrMultiColumnChoices.MultiColumnChoice
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.MultiColumnParameters.SingleOrMultiColumnChoices.SingleColumnChoice
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.SingleColumnParameters.SingleTransformInPlaceChoices.NoInPlaceChoice
import com.harana.sdk.shared.models.flow.actionobjects.multicolumn.SingleColumnParameters.SingleTransformInPlaceChoices.YesInPlaceChoice
import com.harana.sdk.backend.models.flow.{IntegratedTestSupport, Knowledge, UnitSpec}
import com.harana.sdk.backend.models.flow.actionobjects.Transformer
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.actions.spark.wrappers.estimators.StringIndexer
import com.harana.sdk.backend.models.flow.inference.InferenceWarnings
import com.harana.sdk.shared.models.designer.flow.actionobjects.spark.wrappers.models.{MultiColumnStringIndexerModel, SingleColumnStringIndexerModelParameter}
import com.harana.sdk.shared.models.flow.ActionObjectInfo
import com.harana.sdk.shared.models.flow.exceptions.FlowError
import com.harana.sdk.shared.models.flow.parameters.selections.NameSingleColumnSelection

class StringIndexerIntegSpec extends IntegratedTestSupport {

  import StringIndexerIntegSpec._

  def validateSingleColumnParameters(transformerKnowledge: Knowledge[Transformer], inputColumn: Option[String], outputColumn: Option[String]) = {
    val t = validateSingleType[SingleColumnStringIndexerModelParameter](
      transformerKnowledge.asInstanceOf[Knowledge[SingleColumnStringIndexerModelParameter]]
    )

    val choice = singleColumnStringIndexerParameters(inputColumn, outputColumn)
    val choiceParamMap = choice.extractParameterMap()
    val paramMap = t.extractParameterMap()
    paramMap.get(t.inputColumnParameter) shouldBe choiceParamMap.get(choice.inputColumnParameter)
    paramMap.get(t.singleInPlaceChoiceParameter) shouldBe choiceParamMap.get(choice.singleInPlaceChoiceParameter)
  }

  def validateMultiColumnParameters(transformerKnowledge: Knowledge[Transformer], inputColumn: Option[String], outputColumn: Option[String]) = {
    val t = validateSingleType[MultiColumnStringIndexerModel](transformerKnowledge.asInstanceOf[Knowledge[MultiColumnStringIndexerModel]])
    val choice = multiColumnStringIndexerParameters(inputColumn, outputColumn)
    val choiceParamMap = choice.extractParameterMap()
    val paramMap = t.extractParameterMap()
    paramMap.get(t.multiColumnChoice.inputColumnsParameter) shouldBe choiceParamMap.get(choice.inputColumnsParameter)
    paramMap.get(t.multiColumnChoice.inPlaceChoiceParameter) shouldBe choiceParamMap.get(choice.inPlaceChoiceParameter)
  }

  "StringIndexer" when {

    "schema is available" when {

      "parameters are set" when {

        "in single column mode" should {

          "infer SingleColumnStringIndexerModel with parameters" in {
            val in = "c1"
            val out = "out_c1"
            val indexer = singleColumnStringIndexer(Some(in), Some(out))
            val knowledge = indexer.inferKnowledgeUntyped(knownSchemaKnowledgeVector)(executionContext.inferContext)
            validate(knowledge) { case (dataFrameKnowledge, transformerKnowledge) =>
              val expectedSchema = StructType(
                Seq(
                  StructField("c1", StringType),
                  StructField("c2", DoubleType),
                  StructField("c3", StringType),
                  StructField("out_c1", DoubleType, nullable = false)
                )
              )

              validateSingleColumnParameters(transformerKnowledge, Some(in), Some(out))
              validateSchemasEqual(dataFrameKnowledge, Some(expectedSchema))
              validateTransformerInference(knownSchemaKnowledge, transformerKnowledge, Some(expectedSchema))
            }
          }
        }

        "in multi column mode" should {

          "infer StringIndexerModel with parameters" in {
            val in        = "c1"
            val out       = "out_"
            val indexer   = multiColumnStringIndexer(Some(in), Some(out))
            val knowledge =
              indexer.inferKnowledgeUntyped(knownSchemaKnowledgeVector)(executionContext.inferContext)
            validate(knowledge) { case (dataFrameKnowledge, transformerKnowledge) =>
              val expectedSchema = StructType(
                Seq(
                  StructField("c1", StringType),
                  StructField("c2", DoubleType),
                  StructField("c3", StringType),
                  StructField("out_c1", DoubleType, nullable = false)
                )
              )
              validateMultiColumnParameters(transformerKnowledge, Some(in), Some(out))
              validateSchemasEqual(dataFrameKnowledge, Some(expectedSchema))
              validateTransformerInference(knownSchemaKnowledge, transformerKnowledge, Some(expectedSchema))
            }
          }
        }
      }

      "parameters are not set" when {

        "in single column mode" should {

          "infer SingleColumnStringIndexerModel without parameters" in pendingUntilFixed {
            val indexer = singleColumnStringIndexer(None, None)
            val knowledge = indexer.inferKnowledgeUntyped(knownSchemaKnowledgeVector)(executionContext.inferContext)
            validate(knowledge) { case (dataFrameKnowledge, transformerKnowledge) =>
              validateSingleColumnParameters(transformerKnowledge, None, None)
              validateSchemasEqual(dataFrameKnowledge, None)
              validateTransformerInference(knownSchemaKnowledge, transformerKnowledge, None)
            }
          }
        }

        "in multi column mode" should {

          "infer StringIndexerModel without parameters" in pendingUntilFixed {
            val indexer = multiColumnStringIndexer(None, None)
            val knowledge = indexer.inferKnowledgeUntyped(knownSchemaKnowledgeVector)(executionContext.inferContext)
            validate(knowledge) { case (dataFrameKnowledge, transformerKnowledge) =>
              validateMultiColumnParameters(transformerKnowledge, None, None)
              validateSchemasEqual(dataFrameKnowledge, None)
              validateTransformerInference(knownSchemaKnowledge, transformerKnowledge, None)
            }
          }
        }
      }
    }

    "schema is unavailable" when {

      "parameters are set" when {

        "in single column mode" should {

          "infer SingleColumnStringIndexerModel with parameters" in {
            val in = "c1"
            val out = "out_c1"
            val indexer = singleColumnStringIndexer(Some(in), Some(out))
            val knowledge = indexer.inferKnowledgeUntyped(unknownSchemaKnowledgeVector)(executionContext.inferContext)
            validate(knowledge) { case (dataFrameKnowledge, transformerKnowledge) =>
              validateSingleColumnParameters(transformerKnowledge, Some(in), Some(out))
              validateSchemasEqual(dataFrameKnowledge, None)
              validateTransformerInference(unknownSchemaKnowledge, transformerKnowledge, None)
            }
          }
        }
        "in multi column mode" should {
          "infer StringIndexerModel with parameters" in {
            val in = "c1"
            val out = "out_"
            val indexer = multiColumnStringIndexer(Some(in), Some(out))
            val knowledge = indexer.inferKnowledgeUntyped(unknownSchemaKnowledgeVector)(executionContext.inferContext)
            validate(knowledge) { case (dataFrameKnowledge, transformerKnowledge) =>
              validateMultiColumnParameters(transformerKnowledge, Some(in), Some(out))
              validateSchemasEqual(dataFrameKnowledge, None)
              validateTransformerInference(unknownSchemaKnowledge, transformerKnowledge, None)
            }
          }
        }
      }
      "parameters are not set" when {
        "in single column mode" should {
          "throw DeepLangException" in {
            val indexer = singleColumnStringIndexer(None, None)
            a[FlowError] shouldBe thrownBy(
              indexer.inferKnowledgeUntyped(unknownSchemaKnowledgeVector)(executionContext.inferContext)
            )
          }
        }
        "in multi column mode" should {
          "throw DeepLangException" in {
            val indexer = multiColumnStringIndexer(None, None)
            a[FlowError] shouldBe thrownBy(
              indexer.inferKnowledgeUntyped(unknownSchemaKnowledgeVector)(executionContext.inferContext)
            )
          }
        }
      }
    }
  }

  def validateTransformerInference(dataFrameKnowledge: Knowledge[DataFrame],
                                   transformerKnowledge: Knowledge[Transformer],
                                   expectedSchema: Option[StructType]) = {
    val transformer = validateSingleType(transformerKnowledge)
    val knowledge = transformer.transform.infer(executionContext.inferContext)(())(dataFrameKnowledge)
    validateSchemaEqual(validateSingleType[DataFrame](knowledge._1).schema, expectedSchema)
  }
}

object StringIndexerIntegSpec extends UnitSpec {

  def validateSchemaEqual(actual: Option[StructType], expected: Option[StructType]) =
    actual.map(_.map(_.copy(metadata = Metadata.empty))) shouldBe expected

  def validateSchemasEqual(dKnowledge: Knowledge[DataFrame], expectedSchema: Option[StructType]) =
    validateSchemaEqual(validateSingleType(dKnowledge).schema, expectedSchema)

  def multiColumnStringIndexerParameters(inputColumn: Option[String], outputPrefix: Option[String]): MultiColumnChoice = {
    val choice = MultiColumnChoice().setInPlaceChoice(MultiColumnNoInPlace())
    inputColumn.foreach(ic => choice.setInputColumns(Set(ic)))
    outputPrefix.foreach(prefix => choice.setInPlaceChoice(MultiColumnNoInPlace().setColumnsPrefix(prefix)))
    choice
  }

  def multiColumnStringIndexer(inputColumn: Option[String], outputPrefix: Option[String]): StringIndexer = {
    val action = new StringIndexer()
    val choice = multiColumnStringIndexerParameters(inputColumn, outputPrefix)
    action.estimator.set(action.estimator.singleOrMultiChoiceParameter -> choice)
    action.set(action.estimator.extractParameterMap())
  }

  def singleColumnStringIndexerParameters(inputColumn: Option[String], outputColumn: Option[String]): SingleColumnChoice = {
    val choice = SingleColumnChoice().setInPlaceChoice(NoInPlaceChoice())
    inputColumn.foreach(ic => choice.setInputColumn(NameSingleColumnSelection(ic)))
    outputColumn.foreach(oc => choice.setInPlaceChoice(NoInPlaceChoice().setOutputColumn(oc)))
    choice
  }

  def singleColumnStringIndexer(inputColumn: Option[String], outputColumn: Option[String]): StringIndexer = {
    val action = new StringIndexer()
    val choice = singleColumnStringIndexerParameters(inputColumn, outputColumn)
    action.estimator.set(action.estimator.singleOrMultiChoiceParameter -> choice)
    action.set(action.estimator.extractParameterMap())
  }

  def singleColumnStringIndexerInPlace(inputColumn: Option[String]): StringIndexer = {
    val action = new StringIndexer()
    val choice = SingleColumnChoice().setInPlaceChoice(YesInPlaceChoice())
    inputColumn.foreach(ic => choice.setInputColumn(NameSingleColumnSelection(ic)))
    action.estimator.set(action.estimator.singleOrMultiChoiceParameter -> choice)
    action.set(action.estimator.extractParameterMap())
  }

  def validateSingleType[T <: ActionObjectInfo](knowledge: Knowledge[T]): T = {
    knowledge should have size 1
    knowledge.single
  }

  def validate(knowledge: (Vector[Knowledge[ActionObjectInfo]], InferenceWarnings))(f: (Knowledge[DataFrame], Knowledge[Transformer]) => Unit) = {
    val dfKnowledge = knowledge._1(0).asInstanceOf[Knowledge[DataFrame]]
    val modelKnowledge = knowledge._1(1).asInstanceOf[Knowledge[Transformer]]
    f(dfKnowledge, modelKnowledge)
  }

  def dataframeKnowledge(schema: Option[StructType]) = Knowledge(Set[DataFrame](DataFrame.forInference(schema)))

  val schema = StructType(Seq(StructField("c1", StringType), StructField("c2", DoubleType), StructField("c3", StringType)))

  val knownSchemaKnowledge = dataframeKnowledge(Some(schema))
  val unknownSchemaKnowledge = dataframeKnowledge(None)
  val knownSchemaKnowledgeVector = Vector(knownSchemaKnowledge.asInstanceOf[Knowledge[ActionObjectInfo]])
  val unknownSchemaKnowledgeVector = Vector(unknownSchemaKnowledge.asInstanceOf[Knowledge[ActionObjectInfo]])

}
