package com.harana.designer.frontend

import com.harana.designer.frontend.Router.diodeContext
import com.harana.designer.frontend.apps.item.AppItemStore.AppItemState
import com.harana.designer.frontend.apps.item.{AppItemHandler, AppItemStore}
import com.harana.designer.frontend.apps.list.AppListHandler
import com.harana.designer.frontend.apps.list.AppListStore.AppListEditState
import com.harana.designer.frontend.common.grid.GridStore
import com.harana.designer.frontend.common.grid.GridStore.GridState
import com.harana.designer.frontend.data.item.DataSourceItemStore.DataSourceItemState
import com.harana.designer.frontend.data.item.{DataSourceItemHandler, DataSourceItemStore}
import com.harana.designer.frontend.data.list.DataSourceListHandler
import com.harana.designer.frontend.data.list.DataSourceListStore.DataSourceEditState
import com.harana.designer.frontend.files.FilesStore.FilesState
import com.harana.designer.frontend.files.{FilesHandler, FilesStore}
import com.harana.designer.frontend.flows.item.FlowItemStore.FlowItemState
import com.harana.designer.frontend.flows.item.{FlowItemHandler, FlowItemStore}
import com.harana.designer.frontend.flows.list.FlowListHandler
import com.harana.designer.frontend.flows.list.FlowListStore.FlowEditState
import com.harana.designer.frontend.help.HelpStore.HelpState
import com.harana.designer.frontend.help.{HelpHandler, HelpStore}
import com.harana.designer.frontend.navigation.NavigationStore.NavigationState
import com.harana.designer.frontend.navigation.{NavigationHandler, NavigationStore}
import com.harana.designer.frontend.schedules.item.ScheduleItemStore.ScheduleItemState
import com.harana.designer.frontend.schedules.item.{ScheduleItemHandler, ScheduleItemStore}
import com.harana.designer.frontend.schedules.list.ScheduleListHandler
import com.harana.designer.frontend.schedules.list.ScheduleListStore.ScheduleEditState
import com.harana.designer.frontend.system.SystemStore.SystemState
import com.harana.designer.frontend.system.{SystemHandler, SystemStore}
import com.harana.designer.frontend.user.{UserHandler, UserStore}
import com.harana.designer.frontend.user.UserStore.UserState
import com.harana.designer.frontend.utils.DiodeUtils
import com.harana.designer.frontend.welcome.WelcomeStore.WelcomeState
import com.harana.designer.frontend.welcome.{WelcomeHandler, WelcomeStore}
import com.harana.sdk.backend.models.designer.flow.Flow
import com.harana.sdk.shared.models.apps.{App => HaranaApp}
import com.harana.sdk.shared.models.data.DataSource
import com.harana.sdk.shared.models.schedules.Schedule
import diode._

import scala.collection.mutable.ListBuffer

case class State(appItemState: AppItemState,
                 appListState: GridState[HaranaApp, AppListEditState],
                 dataSourceItemState: DataSourceItemState,
                 dataSourceListState: GridState[DataSource, DataSourceEditState],
                 filesState: FilesState,
                 flowItemState: FlowItemState,
                 flowListState: GridState[Flow, FlowEditState],
                 helpState: HelpState,
                 navigationState: NavigationState,
                 scheduleItemState: ScheduleItemState,
                 scheduleListState: GridState[Schedule, ScheduleEditState],
                 systemState: SystemState,
                 userState: UserState,
                 welcomeState: WelcomeState)

object Circuit extends diode.Circuit[State] {

  def state[T](selector: ModelRW[State, T], subscribe: Boolean = true) =
    if (subscribe) DiodeUtils.use(diodeContext, selector)._1 else selector.value

  def initialModel = State(
    AppItemStore.initialState,
    GridStore.initialState(AppListEditState()),
    DataSourceItemStore.initialState,
    GridStore.initialState(DataSourceEditState(Map())),
    FilesStore.initialState,
    FlowItemStore.initialState,
    GridStore.initialState(FlowEditState()),
    HelpStore.initialState,
    NavigationStore.initialState,
    ScheduleItemStore.initialState,
    GridStore.initialState(ScheduleEditState(List(), List(), None, ListBuffer.empty, ListBuffer.empty)),
    SystemStore.initialState,
    UserStore.initialState,
    WelcomeStore.initialState
  )

  override val actionHandler = foldHandlers(
    new AppItemHandler,
    new AppListHandler,
    new DataSourceItemHandler,
    new DataSourceListHandler,
    new FilesHandler,
    new FlowItemHandler,
    new FlowListHandler,
    new HelpHandler,
    new NavigationHandler,
    new ScheduleItemHandler,
    new ScheduleListHandler,
    new SystemHandler,
    new UserHandler,
    new WelcomeHandler
  )
}