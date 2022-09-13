package com.harana.sdk.backend.models.flow.utils.aggregators

import com.harana.sdk.backend.models.flow.utils.aggregators.Aggregator.TransformedInputAggregator
import org.apache.spark.rdd.RDD

import scala.reflect.ClassTag

trait Aggregator[U, T] { self: Serializable =>
  def execute(rdd: RDD[T])(implicit clazz: ClassTag[U]): U = rdd.treeAggregate(initialElement)(mergeValue, mergeCombiners)
  def initialElement: U
  def mergeValue(acc: U, elem: T): U
  def mergeCombiners(left: U, right: U): U
  def mapInput[I](f: I => T): Aggregator[U, I] = TransformedInputAggregator(this, f)
}

object Aggregator {
  case class TransformedInputAggregator[I, U, T](aggregator: Aggregator[U, T], inputTransform: I => T) extends Aggregator[U, I] {
    def initialElement: U = aggregator.initialElement
    def mergeValue(acc: U, elem: I): U = aggregator.mergeValue(acc, inputTransform(elem))
    def mergeCombiners(left: U, right: U): U = aggregator.mergeCombiners(left, right)
  }
}