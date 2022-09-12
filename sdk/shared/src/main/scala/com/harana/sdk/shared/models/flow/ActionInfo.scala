package com.harana.sdk.shared.models.flow

import ActionInfo.ReportParameter.{Extended, Metadata}
import ActionInfo.{ReportParameter, ReportType}
import Gravity.{GravitateLeft, GravitateRight}
import PortPosition._
import com.harana.sdk.shared.models.flow.parameters.choice.Choice.ChoiceOption
import com.harana.sdk.shared.models.flow.utils.CollectionExtensions._
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory
import com.harana.sdk.shared.models.flow.graph.GraphAction
import com.harana.sdk.shared.models.flow.parameters.choice.{Choice, ChoiceParameter}
import com.harana.sdk.shared.models.flow.parameters.{Parameter, Parameters}
import com.harana.sdk.shared.models.flow.utils.Identifiable

import java.util.UUID
import scala.reflect.runtime.{universe => ru}

@SerialVersionUID(1L)
trait ActionInfo extends GraphAction with Serializable with Parameters {

  val inArity: Int
  val outArity: Int

  val id: ActionInfo.Id
  val name: String
  val description: String
  val category: ActionCategory

  val parameters: Array[Parameter[_]]
  lazy val reportParameters: Option[Parameter[_]] = Option(reportTypeParameter).filter(_ => outArity != 0)

  def hasDocumentation: Boolean = false

  def inPortTypes: Vector[ru.TypeTag[_]]
  def inPortsLayout: Vector[PortPosition] = defaultPortLayout(inPortTypes, GravitateLeft)
  def outPortTypes: Vector[ru.TypeTag[_]]
  def outPortsLayout: Vector[PortPosition] = defaultPortLayout(outPortTypes, GravitateRight)

  def getDatasourcesIds: Set[UUID] = Set[UUID]()

  private def defaultPortLayout(portTypes: Vector[ru.TypeTag[_]], gravity: Gravity): Vector[PortPosition] = {
    portTypes.size match {
      case 0 => Vector.empty[PortPosition]
      case 1 => Vector(Center)
      case 2 =>
        gravity match {
          case GravitateLeft  => Vector(Left, Center)
          case GravitateRight => Vector(Center, Right)
        }
      case 3 => Vector(Left, Center, Right)
      case other => throw new IllegalStateException(s"Unsupported number of output ports: $other")
    }
  }

  def validate() = {
    require(outPortsLayout.size == outPortTypes.size, "Every output port must be laid out")
    require(!outPortsLayout.hasDuplicates, "Output port positions must be unique")
    require(inPortsLayout.size == inPortTypes.size, "Every input port must be laid out")
    require(!inPortsLayout.hasDuplicates, "Input port positions must be unique")
    require(inPortsLayout.isSorted, "Input ports must be laid out from left to right")
    require(outPortsLayout.isSorted, "Output ports must be laid out from left to right")
  }

  def typeTag[T: ru.TypeTag]: ru.TypeTag[T] = ru.typeTag[T]

  val reportTypeParameter: ChoiceParameter[ReportType] = ChoiceParameter[ReportType]("report type", Some("Output entities report type. Computing extended report can be time consuming."))
  setDefault(reportTypeParameter, ReportParameter.Extended())
  def getReportType = $(reportTypeParameter)
  def setReportType(value: ReportType): this.type = set(reportTypeParameter, value)

}

object ActionInfo {

  type Id = utils.Id
  val Id = utils.Id

  object ReportParameter {

    case class Metadata() extends ReportType {
      val name = "Metadata report"
      val parameters = Array.empty[Parameter[_]]
    }

    case class Extended() extends ReportType {
      val name = "Extended report"
      val parameters = Array.empty[Parameter[_]]
    }
  }

  sealed trait ReportType extends Choice {
    val choiceOrder: List[ChoiceOption] = List(classOf[Metadata], classOf[Extended])
  }
}