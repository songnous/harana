package com.harana.sdk.shared.models.flow.parameters.validators

import com.harana.sdk.shared.models.flow.exceptions.FlowError
import com.harana.sdk.shared.models.flow.parameters.exceptions.{OutOfRangeError, OutOfRangeWithStepError}

import scala.language.reflectiveCalls

case class RangeValidator[T](begin: T, end: T, beginIncluded: Boolean = true, endIncluded: Boolean = true, step: Option[T] = None)(implicit n: Numeric[T]) extends Validator[T] {

  import RangeValidator.Epsilon
  import n._

  require(begin <= end)
  step.foreach(s => require(s.toDouble > 0))
  step.foreach(s => require(math.abs(takeSteps(countStepsTo(end, s), s.toDouble) - end.toDouble) < Epsilon, "Length of range should be divisible by step."))

  val validatorType = ValidatorType.Range

  def validate(name: String, parameter: T): Vector[FlowError] = {
    val beginComparison: (T, T) => Boolean = if (beginIncluded) (_ >= _) else (_ > _)
    val endComparison: (T, T) => Boolean   = if (endIncluded) (_ <= _) else (_ < _)
    if (!(beginComparison(parameter, begin) && endComparison(parameter, end)))
      Vector(OutOfRangeError(name, parameter, begin, end))
    else
      validateStep(name, parameter)
  }

  private def validateStep(name: String, value: T): Vector[FlowError] = {
    step.foreach { s =>
      if (math.abs(takeSteps(countStepsTo(value, s), s.toDouble) - value.toDouble) >= Epsilon)
        return Vector(OutOfRangeWithStepError(name, value, begin, end, s))
    }
    Vector.empty
  }

  private def countStepsTo(value: T, step: T) = ((value.toDouble - begin.toDouble) / step.toDouble).floor.toLong
  private def takeSteps(count: Long, step: Double) = begin.toDouble + step * count

  override def toHumanReadable(parameterName: String) = {
    val beginConstraint = Begin()
    val endConstraint = End()
    val (isIntStep, mappedStep) = step match {
      case Some(s) =>
        val isInt = s.toDouble == s.toDouble.round
        (isInt, Some(if (isInt) s.toDouble.round.toString else s.toString))
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

  case class Begin() extends Constraint(begin.toDouble) {
    val included = beginIncluded

    val limits = List(
      Int.MinValue.toDouble,
      Long.MinValue.toDouble,
      Float.MinValue.toDouble,
      Double.MinValue,
      Double.NegativeInfinity
    )

    def buildConstraint(oneOfLimits: Boolean, limitRepresentation: String) =
      if (oneOfLimits) None else Some(limitRepresentation.concat(if (included) " <= " else " < "))
  }

  case class End() extends Constraint(end.toDouble) {
    val included = endIncluded

    val limits = List(
      Int.MaxValue.toDouble,
      Long.MaxValue.toDouble,
      Float.MaxValue.toDouble,
      Double.MaxValue,
      Double.PositiveInfinity
    )

    def buildConstraint(oneOfLimits: Boolean, limitRepresentation: String) =
      if (oneOfLimits) None else Some((if (included) " <= " else " < ").concat(limitRepresentation))
  }
}

object RangeValidator {
  val Epsilon = 1e-10

  def allDouble = RangeValidator(begin = Double.MinValue, end = Double.MaxValue)
  def allFloat = RangeValidator(begin = Float.MinValue, end = Float.MaxValue)
  def allInt = RangeValidator(begin = Int.MinValue, end = Int.MaxValue)
  def allLong = RangeValidator(begin = Long.MinValue, end = Long.MaxValue)

  def positiveIntegers = RangeValidator(begin = 0, end = Int.MaxValue, step = Some(1))
}