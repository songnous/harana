package com.harana.sdk.backend.models.flow.actions

import com.harana.sdk.backend.models.flow.actionobjects.MultiColumnEstimator

trait MultiColumnEstimatorParametersForwarder[E <: MultiColumnEstimator[_, _, _]] { self: EstimatorAsAction[E, _] =>

  def setSingleColumn(inputColumnName: String, outputColumnName: String): this.type = {
    estimator.setSingleColumn(inputColumnName, outputColumnName)
    set(estimator.extractParameterMap())
    this
  }

  def setMultipleColumn(inputColumnNames: Set[String], outputColumnPrefix: String): this.type = {
    estimator.setMultipleColumn(inputColumnNames, outputColumnPrefix)
    set(estimator.extractParameterMap())
    this
  }
}
