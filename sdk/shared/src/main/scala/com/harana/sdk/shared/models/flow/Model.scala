package com.harana.sdk.shared.models.flow

case class Model(isLogistic: Boolean, intercept: Double, weights: Seq[Double], means: Seq[Double], stdDevs: Seq[Double]) {

  def score(features: Seq[Double]) = {
    val centered = features.zip(means).map { case (f, m) => f - m }
    val scaled = centered.zip(stdDevs).map { case (c, sd) => if (sd == 0) 0 else c / sd }
    val dot = scaled.zip(weights).map { case (s, w) => s * w }
    val score = dot.sum + intercept
    if (isLogistic) sigmoid(score) else score
  }

  private def sigmoid(x: Double) = 1.0 / (1.0 + math.pow(math.E, -x))

}

object Model {
  type Id = utils.Id
  val Id = utils.Id
}