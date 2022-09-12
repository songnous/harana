package com.harana.sdk.backend.models.flow.actionobjects

import com.harana.sdk.backend.models.flow.parameters.wrappers.SparkParamWrapper
import com.harana.sdk.shared.models.flow.parameters.Parameters
import org.apache.spark.ml
import org.apache.spark.sql.types.StructType

trait ParametersWithSparkWrappers extends Parameters {

  lazy val sparkParamWrappers: Array[SparkParamWrapper[_, _, _]] = parameters.collect {
    case wrapper: SparkParamWrapper[_, _, _] => wrapper +: wrapper.nestedWrappers
  }.flatten

  protected def validateSparkEstimatorParameters(sparkEntity: ml.param.Params, maybeSchema: Option[StructType]): Unit = {
    maybeSchema.foreach(schema => sparkParamMap(sparkEntity, schema))
  }

  /**
   * This method extracts Spark parameters from SparkParamWrappers that are:
   * - declared directly in class which mixes this trait in
   * - declared in values of parameters (i.e. ChoiceParam, MultipleChoiceParam)
   */
  def sparkParamMap(sparkEntity: ml.param.Params, schema: StructType): ml.param.ParamMap = {
    val directParamMap = ml.param.ParamMap(
      sparkParamWrappers.flatMap(wrapper =>
        getOrDefaultOption(wrapper).map(value => {
          val convertedValue = wrapper.convertAny(value)(schema)
          ml.param.ParamPair(
            wrapper.sparkParam(sparkEntity).asInstanceOf[ml.param.Param[Any]], convertedValue)
        })
      ): _*)

    val paramsNestedInParamValues = parameters.flatMap(param => {
      get(param) match {
        case Some(nestedParams: ParametersWithSparkWrappers) =>
          Some(nestedParams.sparkParamMap(sparkEntity, schema))
        case _ => None
      }
    }).foldLeft(ml.param.ParamMap())((map1, map2) => map1 ++ map2)

    directParamMap ++ paramsNestedInParamValues
  }
}