package com.harana.sdk.backend.models.flow.utils.aggregators

case class HistogramAggregator(buckets: Array[Double], evenBuckets: Boolean) extends Aggregator[Array[Long], Double] {

  private def basicBucketFunction(e: Double): Option[Int] = {
    val location = java.util.Arrays.binarySearch(buckets, e)
    if (location < 0) {
      val insertionPoint = -location - 1
      if (insertionPoint > 0 && insertionPoint < buckets.length) Some(insertionPoint - 1) else None
    } else if (location < buckets.length - 1)
      Some(location)
    else
      Some(location - 1)
  }

  private def fastBucketFunction(min: Double, max: Double, count: Int)(e: Double): Option[Int] = {
    if (e.isNaN || e < min || e > max)
      None
    else {
      val bucketNumber = (((e - min) / (max - min)) * count).toInt
      Some(math.min(bucketNumber, count - 1))
    }
  }

  private val bucketFunction =
    if (evenBuckets)
      fastBucketFunction(buckets.head, buckets.last, buckets.length - 1) _
    else
      basicBucketFunction _

  def mergeValue(r: Array[Long], t: Double) = {
    bucketFunction(t) match {
      case Some(x: Int) => r(x) += 1
      case _  =>
    }
    r
  }

  def mergeCombiners(a1: Array[Long], a2: Array[Long]) = {
    a1.indices.foreach(i => a1(i) += a2(i))
    a1
  }

  def initialElement: Array[Long] = new Array[Long](buckets.length - 1)

}
