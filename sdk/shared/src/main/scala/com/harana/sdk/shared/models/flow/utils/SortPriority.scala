package com.harana.sdk.shared.models.flow.utils

import io.circe.generic.JsonCodec

/**
 * Type used to declare a priority based ordering.
 */

@JsonCodec
case class SortPriority(value: Int) extends Ordered[SortPriority]  { self =>

  /** Construct the next `SortPriority` `skip` values away from this one. */
  def next(skip: Int): SortPriority = SortPriority(value + skip)

  /** Construct the next core priority from this one */
  def nextCore: SortPriority = next(100)

  /** Constructs an iterator generating a sequence of `SortPriority` with `skip` distance between them,
   * starting with this one. */
  def inSequence(skip: Int): Iterator[SortPriority] = new Iterator[SortPriority]() {
    private var curr = self
    override def hasNext: Boolean = true
    override def next(): SortPriority = {
      val retval = curr
      curr = curr.next(skip)
      retval
    }
  }

  override def compare(o: SortPriority): Int = {
    value.compare(o.value)
  }
}

object SortPriority {
  /** Lowest possible priority. */
  def lowerBound = SortPriority(Int.MinValue)
  /** Default priority value for "don't care" cases. */
  def coreDefault = SortPriority(0)
  /** Priority for items collected by the CatalogScanner. */
  def sdkDefault = SortPriority(1 << 20)
  /** Highest possible priority. */
  def upperBound = SortPriority(Int.MaxValue)

  /** returns generator for core operations and categories priorities */
  def coreInSequence: Iterator[SortPriority] = coreDefault.inSequence(100)

  /** returns generator for SDK operations loaded using @Register annotation */
  def sdkInSequence: Iterator[SortPriority] = sdkDefault.inSequence(10)

  /** Typeclass for sorting/ordering priority specifications. */
  implicit val sortPriorityOrdering: Ordering[SortPriority] = Ordering.by[SortPriority, Int](_.value)
}