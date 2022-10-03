package com.harana.sdk.shared.models.flow.actionobjects

import com.harana.sdk.shared.models.flow.parameters.{CodeSnippetLanguage, CodeSnippetParameter}

trait PythonEvaluatorInfo extends CustomCodeEvaluatorInfo {

  val id = "1DE6C6F3-C6CC-4DDA-A09A-99CBDD00CFF6"

  val default =
    """from math import sqrt
      |from operator import add
      |
      |def evaluate(dataframe):
      |    # Example Root-Mean-Square Error implementation
      |    n = dataframe.count()
      |    row_to_sq_error = lambda row: (row['label'] - row['prediction'])**2
      |    sum_sq_error = dataframe.rdd.map(row_to_sq_error).reduce(add)
      |    rmse = sqrt(sum_sq_error / n)
      |    return rmse""".stripMargin


  val codeParameter = CodeSnippetParameter("code", default = Some(default), language = CodeSnippetLanguage.Python)

  // Creating a dataframe is a workaround. Currently we can pass to jvm DataFrames only.
  // TODO DS-3695 Fix a metric value - dataframe workaround.
  def getComposedCode(userCode: String) = {
    s"""
       |$userCode
       |
       |def transform(dataframe):
       |    result = evaluate(dataframe)
       |    try:
       |        float_result = float(result)
       |    except ValueError:
       |        raise Exception("Invalid result of `evaluate` function. " +
       |                        "Value " + str(result) + " of type " +
       |                        str(type(result)) + " cannot be converted to float.")
       |    except TypeError:
       |        raise Exception("Invalid result type of `evaluate` function. " +
       |                        "Type " + str(type(result)) +
       |                        " cannot be cast to float.")
       |    result_df = spark.createDataFrame([[float_result]])
       |    return result_df
      """.stripMargin
  }

}

object PythonEvaluatorInfo extends PythonEvaluatorInfo