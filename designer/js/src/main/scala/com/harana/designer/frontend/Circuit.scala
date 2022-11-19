package com.harana.designer.frontend

import com.harana.designer.frontend.Router.diodeContext
import com.harana.designer.frontend.apps.item.{AppItemHandler, AppItemStore}
import com.harana.designer.frontend.apps.list.{AppListHandler, AppListStore}
import com.harana.designer.frontend.common.grid.GridStore
import com.harana.designer.frontend.common.grid.GridStore.GridState
import com.harana.designer.frontend.data.item.{DataSourceItemHandler, DataSourceItemStore}
import com.harana.designer.frontend.data.list.{DataSourceListHandler, DataSourceListStore}
import com.harana.designer.frontend.files.{FilesHandler, FilesStore}
import com.harana.designer.frontend.flows.item.{FlowItemHandler, FlowItemStore}
import com.harana.designer.frontend.flows.list.{FlowListHandler, FlowListStore}
import com.harana.designer.frontend.help.{HelpHandler, HelpStore}
import com.harana.designer.frontend.navigation.{NavigationHandler, NavigationStore}
import com.harana.designer.frontend.schedules.item.{ScheduleItemHandler, ScheduleItemStore}
import com.harana.designer.frontend.schedules.list.{ScheduleListHandler, ScheduleListStore}
import com.harana.designer.frontend.system.{SystemHandler, SystemStore}
import com.harana.designer.frontend.terminal.{TerminalHandler, TerminalStore}
import com.harana.designer.frontend.user.{UserHandler, UserStore}
import com.harana.designer.frontend.utils.DiodeUtils
import com.harana.designer.frontend.welcome.{WelcomeHandler, WelcomeStore}
import com.harana.sdk.shared.models.apps.{App => HaranaApp}
import com.harana.sdk.shared.models.data.DataSource
import com.harana.sdk.shared.models.flow.Flow
import com.harana.sdk.shared.models.schedules.Schedule
import diode._

import scala.collection.mutable.ListBuffer

case class State(appItemState: AppItemStore.State,
                 appListState: GridState[HaranaApp, AppListStore.State],
                 dataSourceItemState: DataSourceItemStore.State,
                 dataSourceListState: GridState[DataSource, DataSourceListStore.State],
                 filesState: FilesStore.State,
                 flowItemState: FlowItemStore.State,
                 flowListState: GridState[Flow, FlowListStore.State],
                 helpState: HelpStore.State,
                 navigationState: NavigationStore.State,
                 scheduleItemState: ScheduleItemStore.State,
                 scheduleListState: GridState[Schedule, ScheduleListStore.State],
                 systemState: SystemStore.State,
                 terminalState: TerminalStore.State,
                 userState: UserStore.State,
                 welcomeState: WelcomeStore.State)

object Circuit extends diode.Circuit[State] {

  def state[T](selector: ModelRW[State, T], subscribe: Boolean = true) =
    if (subscribe) DiodeUtils.use(diodeContext, selector)._1 else selector.value

  def initialModel = State(
    AppItemStore.initialState,
    GridStore.initialState(AppListStore.initialState),
    DataSourceItemStore.initialState,
    GridStore.initialState(DataSourceListStore.initialState),
    FilesStore.initialState,
    FlowItemStore.initialState,
    GridStore.initialState(FlowListStore.initialState),
    HelpStore.initialState,
    NavigationStore.initialState,
    ScheduleItemStore.initialState,
    GridStore.initialState(ScheduleListStore.initialState),
    SystemStore.initialState,
    TerminalStore.initialState,
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
    new TerminalHandler,
    new UserHandler,
    new WelcomeHandler
  )
}