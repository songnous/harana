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

import java.util.UUID
import scala.reflect.runtime.{universe => ru}

@SerialVersionUID(1L)
trait ActionInfo extends GraphAction with Serializable with Parameters {

  val inArity: Int
  val outArity: Int

  val id: ActionInfo.Id
  val name: String
  val category: ActionCategory

  val parameters: Array[Parameter[_]]
  lazy val reportParameters: Option[Parameter[_]] = Option(reportTypeParameter).filter(_ => outArity != 0)

  def hasDocumentation: Boolean = false

  def inputPorts: List[ru.TypeTag[_]]
  def inputPortsLayout: List[PortPosition] = defaultPortLayout(inputPorts, GravitateLeft)
  def outputPorts: List[ru.TypeTag[_]]
  def outputPortsLayout: List[PortPosition] = defaultPortLayout(outputPorts, GravitateRight)

  def getDatasourcesIds: Set[UUID] = Set[UUID]()

  private def defaultPortLayout(portTypes: List[ru.TypeTag[_]], gravity: Gravity): List[PortPosition] = {
    portTypes.size match {
      case 0 => List.empty[PortPosition]
      case 1 => List(Center)
      case 2 =>
        gravity match {
          case GravitateLeft  => List(Left, Center)
          case GravitateRight => List(Center, Right)
        }
      case 3 => List(Left, Center, Right)
      case other => throw new IllegalStateException(s"Unsupported number of output ports: $other")
    }
  }

  def validate() = {
    require(outputPortsLayout.size == outputPorts.size, "Every output port must be laid out")
    require(!outputPortsLayout.hasDuplicates, "Output port positions must be unique")
    require(inputPortsLayout.size == inputPorts.size, "Every input port must be laid out")
    require(!inputPortsLayout.hasDuplicates, "Input port positions must be unique")
    require(inputPortsLayout.isSorted, "Input ports must be laid out from left to right")
    require(outputPortsLayout.isSorted, "Output ports must be laid out from left to right")
  }

  def typeTag[T: ru.TypeTag]: ru.TypeTag[T] = ru.typeTag[T]

  val reportTypeParameter: ChoiceParameter[ReportType] = ChoiceParameter[ReportType]("report type")
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