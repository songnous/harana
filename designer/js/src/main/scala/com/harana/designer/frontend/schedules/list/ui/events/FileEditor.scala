package com.harana.designer.frontend.schedules.list.ui.events

import com.harana.designer.frontend.Circuit
import com.harana.designer.frontend.Circuit.zoomTo
import com.harana.designer.frontend.files.ui.dialogs
import com.harana.designer.frontend.schedules.list.ui.events.CalendarIntervalEditor.dialogRef
import com.harana.sdk.shared.models.schedules.Event
import com.harana.ui.components.elements.Dialog
import com.harana.ui.external.shoelace.{Button, Input}
import slinky.core.FunctionalComponent
import slinky.core.annotations.react
import slinky.core.facade.{Fragment, React}
import slinky.web.html.{div, table, td, tr}

@react object FileEditor {

  case class Props(rowIndex: Int, event: Event)

  val filesState = Circuit.state(zoomTo(_.filesState))
  val dialogRef = React.createRef[Dialog.Def]

  if (dialogRef.current != null)
    dialogs.updateSelect(dialogRef, Circuit.state(zoomTo(_.filesState), false), () => {}, width = Some("720px"))


  val component = FunctionalComponent[Props] { props =>
    Fragment(
      Dialog().withRef(dialogRef),
      table(
        tr(
          td(
            Input(
              name = s"${getClass.getSimpleName}-${props.rowIndex}",
              size = Some("large"),
              value = Some("/")
            )
          ),
          td(
            Button(
              icon = Some("icomoon", "folder6"),
              iconClassName = Some("schedule-action-options"),
              onClick = Some(_ => dialogs.select(dialogRef, filesState, () => println("hi"), width = Some("720px")))
            )
          )
        )
      )
    )
  }
}