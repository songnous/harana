package com.harana.docs.generator.old

import com.harana.sdk.backend.models.flow.actionobjects.stringindexingwrapper.StringIndexingEstimatorWrapper
import com.harana.sdk.backend.models.flow.actionobjects._
import com.harana.sdk.backend.models.flow.actions.{EstimatorAsAction, EstimatorAsFactory, EvaluatorAsFactory, TransformerAsAction}

trait SparkActionsExtractor {

  def sparkActions() =
    catalog.actions.keys
      .map(actionId => catalog.createAction(actionId))
      .flatMap(action => sparkClassName(action).map(ActionWithSparkClassName(action.asInstanceOf[DocumentedAction], _)))
      .toSeq

  private def sparkClassName(action: Action) =
    action match {
      case t: TransformerAsAction[_] => t.transformer match {
        case st: SparkTransformerWrapper[_] => Some(st.sparkTransformer.getClass.getCanonicalName)
        case st: SparkTransformerAsMultiColumnTransformer[_] => Some(st.sparkTransformer.getClass.getCanonicalName)
        case _ => None
      }
      case e: EstimatorAsFactory[_] => e.estimator match {
        case se: SparkEstimatorWrapper[_, _, _] => Some(se.sparkEstimator.getClass.getCanonicalName)
        case se: SparkMultiColumnEstimatorWrapper[_, _, _, _, _, _] => Some(se.sparkEstimatorWrapper.sparkEstimator.getClass.getCanonicalName)
        case siw: StringIndexingEstimatorWrapper[_, _, _, _] => Some(siw.sparkClassCanonicalName)
        case _ => None
      }

      case ev: EvaluatorAsFactory[_] => ev.evaluator match {
        case sev: SparkEvaluatorWrapper[_] => Some(sev.sparkEvaluator.getClass.getCanonicalName)
        case _ => None
      }

      case es: EstimatorAsAction[_, _] => es.estimator match {
        case ses: SparkMultiColumnEstimatorWrapper[_, _, _, _, _, _] => Some(ses.sparkEstimatorWrapper.sparkEstimator.getClass.getCanonicalName)
        case ses: SparkEstimatorWrapper[_, _, _] => Some(ses.sparkEstimator.getClass.getCanonicalName)
        case siw: StringIndexingEstimatorWrapper[_, _, _, _] => Some(siw.sparkClassCanonicalName)
        case _ => None
      }

      case _ => None
    }

  def mapByCategory(actions: Seq[ActionWithSparkClassName]) = {
    val actionsWithCategories = actions.map { actionWithName =>
      val category = catalog.actions(actionWithName.op.id).category
      ActionWithCategory(category, actionWithName)
    }

    val categories = actionsWithCategories.map(_.category).toSet
    Map(categories.toList.sortBy(_.name).map(category => category -> actionsWithCategories.filter(_.category == category).map(_.op)): _*)
  }

  private case class ActionWithCategory(category: ActionCategory, op: ActionWithSparkClassName)
}
