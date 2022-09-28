package com.harana.sdk.shared.models.flow

import com.harana.sdk.shared.models.flow.ActionInfo.ReportParameter.{Extended, Metadata}
import com.harana.sdk.shared.models.flow.ActionInfo.{ReportParameter, ReportType}
import com.harana.sdk.shared.models.flow.Gravity.{GravitateLeft, GravitateRight}
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory
import com.harana.sdk.shared.models.flow.graph.GraphAction
import com.harana.sdk.shared.models.flow.parameters.choice.Choice.ChoiceOption
import com.harana.sdk.shared.models.flow.parameters.choice.{Choice, ChoiceParameter}
import com.harana.sdk.shared.models.flow.parameters.{Parameter, ParameterGroup, Parameters}
import com.harana.sdk.shared.models.flow.utils.CollectionExtensions._

import java.util.UUID
import scala.reflect.runtime.{universe => ru}

@SerialVersionUID(1L)
trait ActionInfo extends GraphAction with Serializable with Parameters {

  val inArity: Int
  val outArity: Int

  val id: ActionInfo.Id
  val name: String
  val category: ActionCategory
  val position: Option[(Int, Int)] = None
  val overrideColor: Option[String] = None

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
      case 1 => List(PortPosition.Center)
      case 2 =>
        gravity match {
          case GravitateLeft  => List(PortPosition.Left, PortPosition.Center)
          case GravitateRight => List(PortPosition.Center, PortPosition.Right)
        }
      case 3 => List(PortPosition.Left, PortPosition.Center, PortPosition.Right)
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

  val reportTypeParameter: ChoiceParameter[ReportType] = ChoiceParameter[ReportType]("report type", default = Some(ReportParameter.Extended()))
  def getReportType = $(reportTypeParameter)
  def setReportType(value: ReportType): this.type = set(reportTypeParameter, value)

}

object ActionInfo {

  type Id = utils.Id
  val Id = utils.Id

  object ReportParameter {

    case class Metadata() extends ReportType {
      val name = "Metadata report"
      val parameterGroups = List.empty[ParameterGroup]
    }

    case class Extended() extends ReportType {
      val name = "Extended report"
      val parameterGroups = List.empty[ParameterGroup]
    }
  }

  sealed trait ReportType extends Choice {
    val choiceOrder: List[ChoiceOption] = List(classOf[Metadata], classOf[Extended])
  }
}