package com.harana.sdk.shared.models.flow.parameters.validators

import com.harana.sdk.shared.models.common.ParameterValue
import com.harana.sdk.shared.models.flow.exceptions.FlowError
import com.harana.sdk.shared.models.flow.parameters.exceptions.{OutOfRangeError, OutOfRangeWithStepError}
import io.circe.{Decoder, Encoder, HCursor, Json}
import io.circe.generic.JsonCodec

import java.net.URI
import scala.language.reflectiveCalls

@JsonCodec
case class RangeValidator[T](begin: Double,
                             end: Double,
                             beginIncluded: Boolean = true,
                             endIncluded: Boolean = true,
                             step: Option[Double] = None) extends Validator[T] {

  import RangeValidator.Epsilon

  require(begin <= end)
  step.foreach(s => require(s > 0))
  step.foreach(s => require(math.abs(takeSteps(countStepsTo(end, s), s) - end) < Epsilon, "Length of range should be divisible by step."))

  def validate(name: String, parameter: T): List[FlowError] =
    parameter match {
      case d: Double => validateDouble(name, d)
      case f: Float => validateDouble(name, f)
      case i: Int => validateDouble(name, i)
      case s: String => validateDouble(name, s.toDouble)
      case _ => List.empty
    }

  private def validateDouble(name: String, parameter: Double) = {
    val beginComparison: (Double, Double) => Boolean = if (beginIncluded) (_ >= _) else (_ > _)
    val endComparison: (Double, Double) => Boolean = if (endIncluded) (_ <= _) else (_ < _)
    if (!(beginComparison(parameter, begin) && endComparison(parameter, end)))
      List(OutOfRangeError(name, parameter, begin, end))
    else
      validateStep(name, parameter)
  }

  private def validateStep(name: String, value: Double): List[FlowError] = {
    step.foreach { s =>
      if (math.abs(takeSteps(countStepsTo(value, s), s) - value) >= Epsilon)
        return List(OutOfRangeWithStepError(name, value, begin, end, s))
    }
    List.empty
  }

  private def countStepsTo(value: Double, step: Double) = ((value - begin) / step).floor.toLong
  private def takeSteps(count: Long, step: Double) = begin + step * count

  override def toHumanReadable(parameterName: String) = {
    val beginConstraint = Begin()
    val endConstraint = End()
    val (isIntStep, mappedStep) = step match {
      case Some(s) =>
        val isInt = s == s.round
        (isInt, Some(if (isInt) s.round.toString else s.toString))
      case None => (false, None)
    }
    val isIntRange = beginConstraint.mapsToInt && endConstraint.mapsToInt && isIntStep
    val rangeDescription =
      if (beginConstraint.isOneOfLimits && endConstraint.isOneOfLimits) ""
      else beginConstraint.create + s"`$parameterName`" + endConstraint.create + " and "
    val stepDescription = {
      def beginSum = {
        val strBegin = beginConstraint.mappedLimit
        if (strBegin == "0") "" else strBegin + " + "
      }
      (isIntRange, mappedStep) match {
        case (true, Some("1")) => s"`$parameterName` is an integer."
        case (_, Some(s)) => s"`$parameterName` = ${beginSum}k*$s, where k is an integer."
        case (_, None) => s"`$parameterName` is a floating point number."
      }
    }
    "Range constraints: " + rangeDescription + stepDescription
  }

  abstract class Constraint(limit: Double) {
    val limits: List[Double]
    def buildConstraint(oneOfLimits: Boolean, limitRepresentation: String): Option[String]
    lazy val isOneOfLimits = limits.contains(limit)
    val mapsToInt = limit.round == limit
    val mappedLimit = if (mapsToInt) limit.round.toString else limit.toString
    def create = buildConstraint(isOneOfLimits, mappedLimit).getOrElse("")
  }

  case class Begin() extends Constraint(begin) {
    val included = beginIncluded

    val limits = List(
      Int.MinValue,
      Long.MinValue,
      Float.MinValue,
      Double.MinValue,
      Double.NegativeInfinity
    )

    def buildConstraint(oneOfLimits: Boolean, limitRepresentation: String) =
      if (oneOfLimits) None else Some(limitRepresentation.concat(if (included) " <= " else " < "))
  }

  case class End() extends Constraint(end) {
    val included = endIncluded

    val limits = List(
      Int.MaxValue,
      Long.MaxValue,
      Float.MaxValue,
      Double.MaxValue,
      Double.PositiveInfinity
    )

    def buildConstraint(oneOfLimits: Boolean, limitRepresentation: String) =
      if (oneOfLimits) None else Some((if (included) " <= " else " < ").concat(limitRepresentation))
  }
}

object RangeValidator {
  val Epsilon = 1e-10

  def allDouble = RangeValidator[Double](begin = Double.MinValue, end = Double.MaxValue)
  def allFloat = RangeValidator[Float](begin = Float.MinValue, end = Float.MaxValue)
  def allInt = RangeValidator[Int](begin = Int.MinValue, end = Int.MaxValue)
  def allLong = RangeValidator[Long](begin = Long.MinValue, end = Long.MaxValue)
  def positiveIntegers = RangeValidator[Int](begin = 0, end = Int.MaxValue, step = Some(1))
}