package com.harana.designer.frontend.schedules.list.ui.events

import com.harana.designer.frontend.Circuit
import com.harana.designer.frontend.Circuit.zoomTo
import com.harana.designer.frontend.files.ui.dialogs
import com.harana.sdk.shared.models.schedules.Event
import com.harana.ui.components.elements.Dialog
import com.harana.ui.external.shoelace.{Button, Input, Select}
import slinky.core.FunctionalComponent
import slinky.core.annotations.react
import slinky.core.facade.{Fragment, React}
import slinky.web.html._

@react object CalendarIntervalEditor {

  case class Props(rowIndex: Int, event: Event.CalendarInterval)

  val dialogRef = React.createRef[Dialog.Def]

  def onOk() = {
  }

  val component = FunctionalComponent[Props] { props =>
    val filesState = Circuit.state(zoomTo(_.filesState))

    if (dialogRef.current != null)
      dialogs.updateSelect(dialogRef, Circuit.state(zoomTo(_.filesState), false), onOk, width = Some("720px"))

    Fragment(
      Dialog().withRef(dialogRef),
      td(
        Input(
          name = s"${getClass.getSimpleName}-${props.rowIndex}",
          placeholder = None,
          size = Some("large"),
          maxLength = Some(3),
          length = Some(3),
          numbersOnly = Some(true),
          `type` = Some("number")
        )
      ),
      td(
        Select(
          hoist = Some(true),
          name = s"${getClass.getSimpleName}-${props.rowIndex}",
          onChange = Some(id => {}),
          placeholder = Some("Select .."),
          options = List("seconds", "minutes", "days", "weeks", "months"),
          size = Some("large")
        )
      ),
      td(
        Button(
          icon = Some("icomoon", "cog3"),
          iconClassName = Some("schedule-action-options"),
          onClick = Some(_ => dialogs.select(dialogRef, filesState, () => println("hi"), width = Some("720px")))
        )
      )
    )
  }
}