package com.harana.docs.generator.old

import com.harana.sdk.backend.models.flow.Action1To2
import com.harana.sdk.backend.models.flow.actionobjects.Transformer
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.backend.models.flow.actions.{EstimatorAsAction, EstimatorAsFactory, EvaluatorAsFactory, TransformerAsAction}
import com.harana.sdk.shared.models.flow.parameters.choice.{AbstractChoiceParameter, Choice, ChoiceParameter, MultipleChoiceParameter}
import com.harana.sdk.shared.models.flow.parameters._

import java.io.{File, PrintWriter}

trait PageCreator {

  def createDocPages(sparkActions: Seq[ActionWithSparkClassName], forceUpdate: Boolean) =
    sparkActions.map { case ActionWithSparkClassName(action, sparkClassName) =>
      val sparkPageFile = new File("docs/actions/" + DocUtils.underscorize(action.name) + ".md")
      if (!sparkPageFile.exists() || forceUpdate) {
        createDocPage(sparkPageFile, action, sparkClassName)
        1
      } else
        0
    }.sum

  private def createDocPage(sparkPageFile: File, action: DocumentedAction, sparkClassName: String) = {
    val writer = new PrintWriter(sparkPageFile)
    writer.println(header(action))
    writer.println(description(action))
    writer.println()
    writer.println(sparkDocLink(action, sparkClassName))
    writer.println()
    writer.println(sinceHaranaVersion(action))
    writer.println()
    writer.println(input(action))
    writer.println()
    writer.println(output(action))
    writer.println()
    writer.println(parameters(action))

    appendExamplesSectionIfNecessary(writer, action)

    writer.flush()
    writer.close()
    println("Created doc page for " + action.name)
  }

  private def header(action: DocumentedAction): String = {
    s"""---
       |layout: global
       |displayTitle: ${action.name}
       |title: ${action.name}
       |description: ${action.name}
       |usesMathJax: true
       |includeActionsMenu: true
       |---""".stripMargin
  }

  private def description(action: DocumentedAction): String =
    DocUtils.forceDotAtEnd(action.description)

  private def sparkDocLink(action: DocumentedAction, sparkClassName: String) = {
    val scalaDocUrl = SparkActionsGenerator$$.scalaDocPrefix + sparkClassName
    val additionalDocs = action.generateDocs match {
      case None => ""
      case Some(docs) => docs
    }

    s"""|This action is ported from Spark ML.
        |
        |
        |$additionalDocs
        |
        |
        |For scala docs details, see
        |<a target="_blank" href="$scalaDocUrl">$sparkClassName documentation</a>.""".stripMargin
  }

  private def sinceHaranaVersion(action: DocumentedAction) =
    s"**Since**: Harana ${action.since.humanReadable}"

  private def input(action: DocumentedAction) = {
    val inputTable = action match {
      case _: TransformerAsAction[_] => inputOutputTable(Seq(("<code><a href=\"../classes/dataframe.html\">DataFrame</a></code>", "The input <code>DataFrame</code>.")))
      case _: EstimatorAsAction[_, _] => inputOutputTable(Seq(("<code><a href=\"../classes/dataframe.html\">DataFrame</a></code>", "The input <code>DataFrame</code>.")))
      case _: EstimatorAsFactory[_] => "This action does not take any input."
      case _: EvaluatorAsFactory[_] => "This action does not take any input."
    }
    "## Input\n\n" + inputTable
  }

  private def output(action: DocumentedAction): String = {
    val outputTable = action match {
      case _: TransformerAsAction[_] =>
        inputOutputTable(
          Seq(
            ("<code><a href=\"../classes/dataframe.html\">DataFrame</a></code>", "The output <code>DataFrame</code>."),
            (
              "<code><a href=\"../classes/transformer.html\">Transformer</a></code>",
              "A <code>Transformer</code> that allows to apply the action on other" +
                " <code>DataFrames</code> using a <a href=\"transform.html\">Transform</a>."
            )
          )
        )
      case _: EstimatorAsAction[_, _] =>
        inputOutputTable(
          Seq(
            ("<code><a href=\"../classes/dataframe.html\">DataFrame</a></code>", "The output <code>DataFrame</code>."),
            (
              "<code><a href=\"../classes/transformer.html\">Transformer</a></code>",
              "A <code>Transformer</code> that allows to apply the action on other" +
                " <code>DataFrames</code> using a <a href=\"transform.html\">Transform</a>."
            )
          )
        )
      case _: EstimatorAsFactory[_] =>
        inputOutputTable(
          Seq(
            (
              "<code><a href=\"../classes/estimator.html\">Estimator</a></code>",
              "An <code>Estimator</code> that can be used in a <a href=\"fit.html\">Fit</a> action."
            )
          )
        )
      case _ =>
        inputOutputTable(
          Seq(
            (
              "<code><a href=\"../classes/evaluator.html\">Evaluator</a></code>",
              "An <code>Evaluator</code> that can be used in an <a href=\"evaluate.html\">Evaluate</a> action."
            )
          )
        )
    }
    "## Output\n\n" + outputTable
  }

  /** @param data Sequence of tuples (typeQualifier, description) */
  private def inputOutputTable(data: Seq[(String, String)]): String = {
    """
      |<table>
      |<thead>
      |<tr>
      |<th style="width:15%">Port</th>
      |<th style="width:15%">Type Qualifier</th>
      |<th style="width:70%">Description</th>
      |</tr>
      |</thead>
      |<tbody>
    """.stripMargin + tableRows(data) +
      """
        |</tbody>
        |</table>
        |""".stripMargin
  }

  private def tableRows(data: Seq[(String, String)]) =
    data.zipWithIndex
      .map { case ((typeQualifier, description), index) => s"<tr><td><code>$index</code></td><td>$typeQualifier</td><td>$description</td></tr>" }
      .reduce((s1, s2) => s1 + s2)

  private def parameters(action: DocumentedAction) =
    "## Parameters\n\n" + parametersTable(action)

  private def parametersTable(action: DocumentedAction) =
    """
      |<table class="table">
      |<thead>
      |<tr>
      |<th style="width:15%">Name</th>
      |<th style="width:15%">Type</th>
      |<th style="width:70%">Description</th>
      |</tr>
      |</thead>
      |<tbody>
      |""".stripMargin + extractParameters(action) +
      """
        |</tbody>
        |</table>
        |""".stripMargin

  private def extractParameters(action: DocumentedAction) =
    action.parameters
      .map(p =>
        ParameterDescription(
          p.name,
          sparkParameterType(p),
          p.description.map(desc => DocUtils.forceDotAtEnd(desc)).getOrElse("") + extraDescription(p)
        )
      )
      .map(paramDescription => parameterTableEntry(paramDescription))
      .reduce((s1, s2) => s1 + s2)


  private def sparkParameterType(param: Parameter[_]) =
    param match {
      case _: IOColumnsParameter => "InputOutputColumnSelector"
      case _: BooleanParameter => "Boolean"
      case _: ChoiceParameter[_] => "SingleChoice"
      case _: ColumnSelectorParameter => "MultipleColumnSelector"
      case _: NumericParameter => "Numeric"
      case _: MultipleNumericParameter => "MultipleNumeric"
      case _: MultipleChoiceParameter[_] => "MultipleChoice"
      case _: PrefixBasedColumnCreatorParameter => "String"
      case _: SingleColumnCreatorParameter => "String"
      case _: SingleColumnSelectorParameter => "SingleColumnSelector"
      case _: StringParameter => "String"
      case _ => throw new RuntimeException("Unexpected parameter of class " + param.getClass.getSimpleName)
    }

  private def parameterTableEntry(paramDescription: ParameterDescription) = {
    val paramType = paramDescription.paramType
    val anchor = paramTypeAnchor(paramType)
    s"""
       |<tr>
       |<td><code>${paramDescription.name}</code></td>
       |<td><code><a href="../parameter_types.html#$anchor">$paramType</a></code></td>
       |<td>${paramDescription.description}</td>
       |</tr>
       |""".stripMargin
  }

  private def paramTypeAnchor(paramType: String) =
    paramType.replaceAll("(.)([A-Z])", "$1-$2").toLowerCase

  private def extraDescription(param: Parameter[_]) =
    param match {
      case _: IOColumnsParameter => ""
      case p: AbstractChoiceParameter[_, _] => " Possible values: " + choiceValues(p.choiceInstances)
      case _ => ""
    }

  private def choiceValues(choices: Seq[Choice]) =
    "<code>[" + choices.map("\"" + _.name + "\"").mkString(", ") + "]</code>"

  private case class ParameterDescription(name: String, paramType: String, description: String)

  private def appendExamplesSectionIfNecessary(writer: PrintWriter, action: DocumentedAction) = {
    val createExamplesSection: Boolean = action match {
      // It is impossible to match Action1To2[DataFrame, DataFrame, Transformer] in match-case
      case op: Action1To2[_, _, _] => (op.tTagTI_0.tpe <:< typeTag[DataFrame].tpe) && (op.tTagTO_0.tpe <:< typeTag[DataFrame].tpe) && (op.tTagTO_1.tpe <:< typeTag[Transformer].tpe)
      case _ => false
    }
    if (createExamplesSection) {
      println("\t\tAdding 'Example' section for " + action.name)
      writer.println()
      writer.println(examples(action))
    }
  }

  private def examples(action: DocumentedAction) =
    "{% markdown actions/examples/" + action.getClass.getSimpleName + ".md %}"
}
