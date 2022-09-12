package com.harana.sdk.backend.models.flow.actions.examples

import com.harana.sdk.backend.models.flow.actionobjects.Projector
import scala.collection.mutable
import org.apache.spark.sql.Row
import com.harana.sdk.backend.models.flow.actionobjects.dataframe.DataFrame
import com.harana.sdk.shared.models.flow.ActionInfo
import com.harana.sdk.shared.models.flow.actionobjects.SortColumnParameter
import com.harana.sdk.shared.models.flow.parameters.ParameterType
import io.circe.Json

object ExampleHtmlFormatter {

  def exampleHtml(action: Action, inputDataFrames: Seq[DataFrame], outputDataFrames: Seq[DataFrame]) = {

    s"""|## Example
        |${parametersHtml(action)}
        |### Input
        |
        |${inputHtml(inputDataFrames)}
        |
        |### Output
        |
        |${outputHtml(outputDataFrames)}
        |""".stripMargin
  }

  private def parametersHtml(action: Action) =
    ParametersHtmlFormatter.toHtml(action)

  private def inputHtml(dfs: Iterable[DataFrame]) =
    dataFramesToHtml(dfs, "Input")

  private def outputHtml(dfs: Iterable[DataFrame]) =
    dataFramesToHtml(dfs, "Output")

  private def dataFramesToHtml(dfs: Iterable[DataFrame], header: String) =
    if (dfs.size > 1) { // Print additional headers if there are many DFs
      dfs.zipWithIndex.map { case (df, idx) =>
        val dfHtml = DataFrameHtmlFormatter.toHtml(df)
        s"""
           |#### $header $idx
           |
           |$dfHtml
           |""".stripMargin
      }.mkString("\n")
    } else
      DataFrameHtmlFormatter.toHtml(dfs.head)
}

object DataFrameHtmlFormatter {

  def toHtml(dataFrame: DataFrame) = {
    val names = dataFrame.sparkDataFrame.schema.map(_.name)
    val rows  = dataFrame.sparkDataFrame.collect().toSeq.map(stringifyRow)

    s"""|<table class=\"table\">
        |  <thead>
        |${tabelarizeHeader(names)}
        |  </thead>
        |  <tbody>
        |${tabelarizeRows(rows)}
        |  </tbody>
        |</table>""".stripMargin
  }

  private def tabelarize(
      rows: Seq[Seq[String]],
      preRow: String,
      postRow: String,
      preValue: String,
      postValue: String
  ) = {

    def toHtmlValue(value: String): String = s"$preValue$value$postValue"
    def toHtmlRow(row: Seq[String])        =
      s"""|$preRow
          |${row.mkString("\n")}
          |$postRow""".stripMargin

    rows.map(r => toHtmlRow(r.map(toHtmlValue))).mkString("\n")
  }

  private def tabelarizeHeader(names: Seq[String]) = {
    val preRow    = "    <tr>"
    val preValue  = "      <th>"
    val postValue = "</th>"
    val postRow   = "    </tr>"
    tabelarize(Seq(names), preRow, postRow, preValue, postValue)
  }

  private def tabelarizeRows(rows: Seq[Seq[String]]) = {
    val preRow    = "    <tr>"
    val preValue  = "      <td>"
    val postValue = "</td>"
    val postRow   = "    </tr>"
    tabelarize(rows, preRow, postRow, preValue, postValue)
  }

  private def stringifyValue(value: Any) = Option(value) match {
    case Some(nonNullValue) =>
      nonNullValue match {
        case v: mutable.WrappedArray[_] => s"[${v.mkString(",")}]"
        case x                          => x.toString
      }
    case None => "null"
  }

  private def stringifyRow(row: Row): Seq[String] =
    row.toSeq.map(stringifyValue)

}

object ParametersHtmlFormatter {
  private val leftColumnFieldName = "left column"
  private val rightColumnFieldName = "right column"
  private val typeFieldName = "type"
  private val nameFieldName = "name"
  private val valueFieldName = "value"
  private val valuesFieldName = "values"
  private val selectionsFieldName = "selections"
  private val userDefinedMissingValues = "user-defined missing values"
  private val missingValue = "missing value"

  def toHtml(action: Action) = {
    if (action.parameterValuesToJson.fields.isEmpty)
      "\n"
    else {
      val parameterValues = extractParameterValues(action.parameterValuesToJson).toMap
      val paramTypes = extractParameterTypes(action.parametersToJson.asInstanceOf[Seq]).toMap
      val parametersOrder = extractParametersOrder(action.parametersToJson.asInstanceOf[Seq])

      val orderedValues = parametersOrder.flatMap(name => parameterValues.get(name).map(v => name -> v))
      val parametersHtml = orderedValues.map { case (name, value) => paramValueToHtml(name, value, paramTypes.get(name))}

      s"""|
          |### Parameters
          |
          |<table class="table">
          |  <thead>
          |    <tr>
          |      <th style="width:20%">Name</th>
          |      <th style="width:80%">Value</th>
          |    </tr>
          |  </thead>
          |  <tbody>
          |${parametersHtml.mkString("\n")}
          |  </tbody>
          |</table>
          |""".stripMargin
    }
  }

  private def extractParametersOrder(json: Seq): Seq[String] = {
    json.elements.flatMap { param =>
      val paramObject = param.asJson
      val subParameters =
        if (paramObject.fields.contains(typeFieldName) && paramObject.fields(typeFieldName).asInstanceOf[JsString].value == "choice") {
          val choices = paramObject.fields(valuesFieldName).asInstanceOf[Seq].elements.map(_.asJson).toSeq
          choices.flatMap(c => extractParametersOrder(c.fields("schema").asInstanceOf[Seq]))
        } else
          Seq()

      paramObject.fields(nameFieldName).asInstanceOf[JsString].value +: subParameters.distinct
    }
  }

  private def extractParameterValues(jsObject: Json): Seq[(String, String)] =
    jsObject.fields.toSeq.flatMap {
      case (name, value: Seq) if name == userDefinedMissingValues && isUserDefinedMissingValue(value) => Seq(handleUserDefinedMissingValues(name, value))
      case (name, value: Seq) if isColumnPairs(value) => Seq(handleColumnPairs(name, value))
      case (name, value: Seq) if isColumnProjection(value) => Seq(handleColumnProjection(name, value))
      case (name, value: Seq) if isSortColumnParameter(value) => Seq(handleSortColumnParameters(name, value))
      case (name, value: Json) if value.fields.size == 1 => (name, value.fields.head._1) +: extractParameterValues(value.fields.head._2.asJson)
      case (name, value: Json) if isMultipleColumnSelection(name, value) => Seq(handleMultipleColumnSelection(name, value))
      case (name, value: Json) if isSingleColumnSelection(name, value) => Seq(handleSingleColumnSelection(name, value))
      case (name, value: Json) => value match { case _) | _) | JsBoolean(_) => Seq((name, value.toString }
    }

  private def extractParameterTypes(json: Seq): Seq[(String, String)] = {
    json.elements.map {
      case obj: Json =>
        obj.fields(nameFieldName).asInstanceOf[JsString].value ->
          obj.fields(typeFieldName).asInstanceOf[JsString].value
      case x             =>
        throw new IllegalArgumentException(
          s"Expected array of jsObjects. Encountered ${x.prettyPrint} as array element."
        )
    }
  }

  private def paramValueToHtml(name: String, value: String, paramType: Option[String]) = {
    val cellValue =
      if (paramType.nonEmpty && ParameterType.CodeSnippet.toString.equals(paramType.get))
        s"<pre>${value.substring(1, value.length - 1).replace("\\n", "\n")}</pre>"
      else
        value
    s"""|  <tr>
        |    <td><code>$name</code></td>
        |    <td>$cellValue</td>
        |  </tr>""".stripMargin
  }

  private def isColumn(jsObject: Json) = {
    jsObject.fields.contains(typeFieldName) &&
    jsObject.fields(typeFieldName).isInstanceOf[JsString]
    jsObject.fields(typeFieldName).asInstanceOf[JsString].value == "column" &&
    jsObject.fields.contains(valueFieldName)
  }

  private def isColumnPairs(value: Seq) =
    value.elements.forall { v =>
      v.isInstanceOf[Json] &&
      v.asJson.fields.contains(leftColumnFieldName) &&
      v.asJson.fields.contains(rightColumnFieldName) &&
      v.asJson.fields(leftColumnFieldName).isInstanceOf[Json] &&
      v.asJson.fields(rightColumnFieldName).isInstanceOf[Json] &&
      isColumn(v.asJson.fields(leftColumnFieldName).asJson) &&
      isColumn(v.asJson.fields(rightColumnFieldName).asJson)
    }

  private def isSortColumnParameter(value: Seq) =
    value.elements.forall(v =>
      v.isInstanceOf[Json] &&
        v.asJson.fields.contains(SortColumnParameter.columnNameParameterName) &&
        v.asJson.fields.contains(SortColumnParameter.descendingFlagParameterName) &&
        v.asJson.fields(SortColumnParameter.columnNameParameterName).isInstanceOf[Json] &&
        isColumn(v.asJson.fields(SortColumnParameter.columnNameParameterName).asJson)
    )

  private def isUserDefinedMissingValue(value: Seq) =
    value.elements.forall { v =>
      v.isInstanceOf[Json] &&
      v.asJson.fields.contains(missingValue) &&
      v.asJson.fields(missingValue).isInstanceOf[JsString]
    }

  private def isColumnProjection(value: Seq) =
    value.elements.forall { v =>
      v.isInstanceOf[Json] &&
      v.asJson.fields.contains(Projector.originalColumnParameterName) &&
      v.asJson.fields(Projector.originalColumnParameterName).isInstanceOf[Json] &&
      isColumn(v.asJson.fields(Projector.originalColumnParameterName).asJson)
    }

  private def extractColumnName(v: Json, key: String) =
    v.asJson
      .fields(key)
      .asInstanceOf[Json]
      .fields(valueFieldName)
      .asInstanceOf[JsString]
      .value

  private def handleColumnPairs(name: String, pairsJs: Seq): (String, String) = {
    val pairs = pairsJs.elements.map { v =>
      val leftColumn  = extractColumnName(v, leftColumnFieldName)
      val rightColumn = extractColumnName(v, rightColumnFieldName)
      s"left.$leftColumn == right.$rightColumn"
    }.mkString(", ")

    (name, s"Join on $pairs")
  }

  private def handleUserDefinedMissingValues(name: String, missingValues: Seq): (String, String) = {
    val missingValuesFormatted =
      missingValues.elements
        .map(_.asJson)
        .map(_.fields(missingValue).asInstanceOf[JsString])
        .mkString("[", ", ", "]")
    (name, s"User-defined missing values: $missingValuesFormatted")
  }

  private def handleColumnProjection(name: String, projectionsJs: Seq): (String, String) = {
    val pairs = projectionsJs.elements.map { v =>
      val originalColumn = extractColumnName(v, Projector.originalColumnParameterName)
      val renameDesc =
        if (
          v.asJson.fields.contains(Projector.renameColumnParameterName) &&
          v.asJson.fields(Projector.renameColumnParameterName).asJson.fields.contains("Yes")
        ) {
          " (renamed to <code>" +
            v.asJson
              .fields(Projector.renameColumnParameterName)
              .asJson
              .fields("Yes")
              .asJson
              .fields(Projector.originalColumnParameterName)
              .asInstanceOf[JsString]
              .value +
            "</code>)"
        } else
          ""
      s"<code>$originalColumn</code>$renameDesc"
    }.mkString(", ")

    (name, s"Select columns: $pairs")
  }

  private def handleSortColumnParameters(name: String, sortColumnParameters: Seq): (String, String) = {
    val sortColumns = sortColumnParameters.elements.map { v =>
      val colName = extractColumnName(v, SortColumnParameter.columnNameParameterName)
      val ascDesc =
        if (v.asJson.fields(SortColumnParameter.descendingFlagParameterName).asInstanceOf[JsBoolean].value)
          "DESC"
        else
          "ASC"
      s"$colName $ascDesc"
    }
    (name, s"Sort by ${sortColumns.mkString(", ")}")
  }

  private def isMultipleColumnSelection(name: String, value: Json) =
    value.fields.size == 2 &&
    value.fields.contains(selectionsFieldName) &&
    value.fields.contains("excluding")

  private def handleMultipleColumnSelection(name: String, selectionsJs: Json): (String, String) = {
    val rawSelections = selectionsJs.fields(selectionsFieldName).asInstanceOf[Seq]
    val selections = rawSelections.elements.map(_.asJson).map {
      case s: Json if s.fields(typeFieldName).asInstanceOf[JsString].value == "columnList" =>
        val selectedColumns = s.fields(valuesFieldName).prettyPrint
        "by name: " + selectedColumns
    }
    (name, s"Selected columns: ${selections.mkString(", ")}.")
  }

  private def isSingleColumnSelection(name: String, value: Json) =
    value.fields.size == 2 &&
    value.fields.contains(valueFieldName) &&
    value.fields.contains(typeFieldName)

  private def handleSingleColumnSelection(name: String, value: Json): (String, String) =
    (name, value.fields(valueFieldName).toString)

}
