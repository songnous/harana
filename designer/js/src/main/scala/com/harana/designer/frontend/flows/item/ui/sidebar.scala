package com.harana.designer.frontend.flows.item.ui

import com.harana.designer.frontend.Circuit
import com.harana.designer.frontend.flows.item.FlowItemStore.{FlowItemState, SelectActionType, UpdateIsEditingParameters, UpdateParameterValues}
import com.harana.designer.frontend.utils.DateUtils
import com.harana.designer.frontend.utils.i18nUtils._
import com.harana.sdk.shared.models.common.Parameter.ParameterName
import com.harana.sdk.shared.models.common.ParameterValue
import com.harana.sdk.shared.models.flow.execution.spark.AggregateMetric._
import com.harana.sdk.shared.models.flow.execution.spark.ExecutionStatus
import com.harana.sdk.shared.models.flow.{ActionInfo, Flow, FlowExecution}
import com.harana.ui.components.elements.{Color, Label}
import com.harana.ui.components.sidebar.ParametersSection
import com.harana.ui.external.shoelace.ProgressBar
import slinky.core.facade.{Fragment, ReactElement}
import slinky.web.html._

import scala.util.Try

package object sidebar {

  def actions(state: FlowItemState): ReactElement =
    div(className := "flow-sidebar-components")(
      state.actionTypes.map { at =>
        li(className := "flow-component-item", draggable := (!state.isRunning).toString, onDrag := (_ => Circuit.dispatch(SelectActionType(at))))(
          Fragment(
            pill(actionTypeColour(at)),
            i"actiontypes.${at.getClass.getSimpleName}"
          )
        )
      }
    )


  def parameters(flow: Option[Flow], action: Option[ActionInfo], actionId: Option[ActionInfo.Id], parameterValues: Map[ParameterName, ParameterValue], isRunning: Boolean): ReactElement =
    div(className := "flow-sidebar-components")(
      action match {
        case Some(a) =>
          Fragment(
            div(className := "category-content")(
              h6(i"actiontypes.${a.name}")
            ),
            ParametersSection(
              parameterGroups = a.pa
              i18nPrefix = "flows",
              values = parameterValues,
              onChange = Some((parameter, value) =>
                if (actionId.isDefined) Circuit.dispatch(UpdateParameterValues(actionId.get, parameterValues + (parameter.name -> value)))
              ),
              isEditable = !isRunning,
              onEditing = Some(isEditing => Circuit.dispatch(UpdateIsEditingParameters(isEditing)))
            )
          )
        case None =>
          flow match {
            case Some(flow) =>
              Fragment(
                div(className := "category-content")(
                  h6(flow.title),
                  p(flow.description)
                ))
            case None => div()
          }
      }
    )


  def runStatus(flowExecution: Option[FlowExecution]): ReactElement =
    Fragment(
      div(h6(i"flows.sidebar.statistics.status")),
      div(className := "pb-20")(
        flowExecution match {
          case Some(fe) =>
            fe.executionStatus match {
              case ExecutionStatus.None => Label(i"flows.sidebar.statistics.status.none", color = Some(Color.Grey400))
              case ExecutionStatus.Executing => ProgressBar(className = Some("flows-progressbar"), value = Some(0))
              case ExecutionStatus.Killed => Label(i"flows.sidebar.statistics.status.killed", color = Some(Color.Red400))
              case ExecutionStatus.TimedOut => Label(i"flows.sidebar.statistics.status.timed-out", color = Some(Color.Red400))
              case ExecutionStatus.Cancelled => Label(i"flows.sidebar.statistics.status.cancelled", color = Some(Color.Grey400))
              case ExecutionStatus.PendingCancellation => Label(i"flows.sidebar.statistics.status.pending-cancellation", color = Some(Color.Orange400))
              case ExecutionStatus.PendingExecution => Label(i"flows.sidebar.statistics.status.pending-execution", color = Some(Color.Orange400))
              case ExecutionStatus.Paused => Label(i"flows.sidebar.statistics.status.paused", color = Some(Color.Orange400))
              case ExecutionStatus.Succeeded => Label(i"flows.sidebar.statistics.status.succeeded", color = Some(Color.Green400))
              case ExecutionStatus.Failed => Fragment(
                Label(i"flows.sidebar.statistics.status.failed", color = Some(Color.Red400)),
                p(className := "sidebar-error-message")(fe.executionFailure.get)
              )
            }

          case None =>
            Label(i"flows.sidebar.statistics.status.none", color = Some(Color.Grey400))
        }
      )
    )


  def runTime(flowExecution: Option[FlowExecution]): ReactElement = {
    val info = flowExecution.flatMap(_.info)
    Fragment(
      div(h6(i"flows.sidebar.statistics.timing")),
      ul(className := "media-list media-list-linked pb-15")(
        row(i"flows.sidebar.statistics.timing.start", Try(DateUtils.format(info.get.startTime, includeTime = true)).toOption),
        row(i"flows.sidebar.statistics.timing.finish", Try(DateUtils.format(info.get.endTime, includeTime = true)).toOption),
        row(i"flows.sidebar.statistics.timing.duration", Try(s"${DateUtils.pretty(info.get.startTime, info.get.endTime)}").toOption)
      )
    )
  }


  def runHealth(flowExecution: Option[FlowExecution]): ReactElement =
    Fragment(
      div(h6(i"flows.sidebar.statistics.health")),
      ul(className := "media-list media-list-linked pb-15")(
        healthRow(i"flows.sidebar.statistics.health.disk-spill", i"flows.sidebar.statistics.health.disk-spill.description", flowExecution.map(_ => "success")),
        healthRow(i"flows.sidebar.statistics.health.driver-wastage", i"flows.sidebar.statistics.health.driver-wastage.description", flowExecution.map(_ => "success")),
        healthRow(i"flows.sidebar.statistics.health.executor-wastage", i"flows.sidebar.statistics.health.executor-wastage.description", flowExecution.map(_ => "success")),
        healthRow(i"flows.sidebar.statistics.health.failed-stages", i"flows.sidebar.statistics.health.failed-stages.description", flowExecution.map(_ => "success")),
        healthRow(i"flows.sidebar.statistics.health.garbage-collection", i"flows.sidebar.statistics.health.garbage-collection.description", flowExecution.map(_ => "success"))
      )
    )


  def runShuffle(flowExecution: Option[FlowExecution]): ReactElement = {
    val metrics = flowExecution.flatMap(_.metrics.map(_.metrics))
    Fragment(
      div(h6(i"flows.sidebar.statistics.shuffle")),
      ul(className := "media-list media-list-linked pb-15")(
        row(i"flows.sidebar.statistics.shuffle.read", Try(s"${metrics.get(ShuffleReadBytesRead).value / 1024} MB / ${metrics.get(ShuffleReadRecordsRead).value} records").toOption),
        row(i"flows.sidebar.statistics.shuffle.write", Try(s"${metrics.get(ShuffleWriteBytesWritten).value / 1024} MB / ${metrics.get(ShuffleWriteRecordsWritten).value} records").toOption),
      )
    )
  }


  def runResources(flowExecution: Option[FlowExecution]): ReactElement =
    Fragment(
      div(h6(i"flows.sidebar.statistics.resources")),
      ul(className := "media-list media-list-linked pb-15")(
        row(i"flows.sidebar.statistics.resources.executors", Try(s"${flowExecution.get.executorCount.get} / 30").toOption),
        row(i"flows.sidebar.statistics.resources.cores", Try(s"${flowExecution.get.coresPerExecutor.get} ${i"flows.sidebar.statistics.resources.per-executor"} / ${flowExecution.get.coresPerExecutor.get * flowExecution.get.executorCount.get } ${i"flows.sidebar.statistics.resources.total"}").toOption),
        row(i"flows.sidebar.statistics.resources.memory", Try(s"${flowExecution.get.metrics.get.metrics(AggregateMetric.PeakExecutionMemory).mean.toString} MB").toOption)
      )
    )


  private def row(title: String, value: Option[String]) = {
    val str = value.getOrElse(i"flows.sidebar.statistics.common.not-available")
    li(key := title, className := "media")(
      div(className := "media-body")(
        span(className := "media-heading text-semi-bold")(title),
        span(className := "media-annotation")(str)
      )
    )
  }


  private def healthRow(title: String, description: String, status: Option[String]) =
    li(key := title, className := "media")(
      div(className := "media-body")(
        span(className := "media-heading text-semi-bold")(title),
        span(className := "media-annotation")(description)
      ),
      div(className := "media-right media-middle")(span(className := s"status-mark bg-${status.getOrElse("grey-300")}"))
    )
}
