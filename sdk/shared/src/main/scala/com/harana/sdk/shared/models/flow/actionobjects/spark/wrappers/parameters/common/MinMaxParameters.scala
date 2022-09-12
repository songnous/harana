package com.harana.sdk.shared.models.flow.actionobjects.spark.wrappers.parameters.common

import com.harana.sdk.shared.models.flow.parameters.{DoubleParameter, Parameters}

import scala.language.reflectiveCalls

trait MinMaxParameters extends Parameters {

  val minParameter = DoubleParameter("min", Some("The lower bound after transformation, shared by all features."))
  setDefault(minParameter, 0.0)
  def setMin(value: Double): this.type = set(minParameter, value)


  val maxParameter = DoubleParameter("max", Some("The upper bound after transformation, shared by all features."))
  setDefault(maxParameter, 1.0)
  def setMax(value: Double): this.type = set(maxParameter, value)
}