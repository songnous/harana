package com.harana.designer.frontend.schedules.ui.events

import com.harana.designer.frontend.Circuit
import com.harana.designer.frontend.Circuit.zoomTo
import com.harana.designer.frontend.files.ui.dialogs
import com.harana.sdk.shared.models.schedules.Event.{CalendarInterval, CalendarSchedule}
import com.harana.ui.components.elements.Dialog
import com.harana.ui.external.shoelace.{Button, Input}
import slinky.core.FunctionalComponent
import slinky.core.annotations.react
import slinky.core.facade.{Fragment, React}
import slinky.web.html._

@react object CalendarScheduleEditor {

  case class Props(rowIndex: Int, event: CalendarSchedule)

  val dialogRef = React.createRef[Dialog.Def]

  def onOk() = {
  }

  val component = FunctionalComponent[Props] { props =>
    val filesState = Circuit.state(zoomTo(_.filesState))

    if (dialogRef.current != null)
      dialogs.updateSelect(dialogRef, Circuit.state(zoomTo(_.filesState), false), onOk, width = Some("720px"))

    Fragment(
      Dialog().withRef(dialogRef),
      table(
        tr(
          td(
            Input(
              name = s"${getClass.getSimpleName}-${props.rowIndex}",
              placeholder = Some("0 0 * * *"),
              size = Some("large")
            )
          )
        )
      )
    )
  }
}