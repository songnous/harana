package com.harana.sdk.shared.models.flow

import com.harana.sdk.shared.models.flow.ActionTypeInfo.ReportParameter.{Extended, Metadata}
import com.harana.sdk.shared.models.flow.ActionTypeInfo.{ReportParameter, ReportType}
import com.harana.sdk.shared.models.flow.Gravity.{GravitateLeft, GravitateRight}
import com.harana.sdk.shared.models.flow.catalog.Catalog
import com.harana.sdk.shared.models.flow.catalogs.ActionCategory
import com.harana.sdk.shared.models.flow.graph.GraphAction
import com.harana.sdk.shared.models.flow.parameters.choice.Choice.ChoiceOption
import com.harana.sdk.shared.models.flow.parameters.choice.{Choice, ChoiceParameter}
import com.harana.sdk.shared.models.flow.parameters.{Parameter, ParameterGroup, Parameters}
import com.harana.sdk.shared.models.flow.utils.CollectionExtensions._
import io.circe.{Decoder, Encoder}
import izumi.reflect.Tag

import java.util.UUID
import scala.reflect.runtime.{universe => ru}

@SerialVersionUID(1L)
trait ActionTypeInfo extends GraphAction with Serializable with Parameters {

  val inArity: Int
  val outArity: Int

  val id: ActionTypeInfo.Id
  val name: String
  val category: ActionCategory

  lazy val reportParameters: Option[Parameter[_]] = Option(reportTypeParameter).filter(_ => outArity != 0)

  def hasDocumentation: Boolean = false

  def inputPorts: List[Tag[_]]
  def inputPortsLayout: List[PortPosition] = defaultPortLayout(inputPorts, GravitateLeft)
  def outputPorts: List[Tag[_]]
  def outputPortsLayout: List[PortPosition] = defaultPortLayout(outputPorts, GravitateRight)

  def getDatasourcesIds: Set[UUID] = Set[UUID]()

  private def defaultPortLayout(portTypes: List[Tag[_]], gravity: Gravity): List[PortPosition] = {
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

  def typeTag[T: Tag]: Tag[T] = Tag[T]

  val reportTypeParameter: ChoiceParameter[ReportType] = ChoiceParameter[ReportType]("report-type", default = Some(ReportParameter.Extended()))
  def getReportType = $(reportTypeParameter)
  def setReportType(value: ReportType): this.type = set(reportTypeParameter, value)

}

object ActionTypeInfo {

  type Id = utils.Id
  val Id = utils.Id

  implicit def decoder[T <: ActionTypeInfo]: Decoder[T] =
    Decoder.decodeString.emap { id => Catalog.actionsByIdMap.get(id).toRight("No ActionTypeInfo found with id").map(_.asInstanceOf[T]) }

  implicit def encoder[T <: ActionTypeInfo]: Encoder[T] =
    Encoder.encodeString.contramap[T](_.id.toString)

  object ReportParameter {

    case class Metadata() extends ReportType {
      val name = "metadata-report"
      override val parameterGroups = List.empty[ParameterGroup]
    }

    case class Extended() extends ReportType {
      val name = "extended-report"
      override val parameterGroups = List.empty[ParameterGroup]
    }
  }

  sealed trait ReportType extends Choice {
    val choiceOrder: List[ChoiceOption] = List(classOf[Metadata], classOf[Extended])
  }
}