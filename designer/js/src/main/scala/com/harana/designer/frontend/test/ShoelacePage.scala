package com.harana.designer.frontend.test

import com.harana.designer.frontend.navigation.ui.Navigation
import com.harana.ui.components.ColumnSize
import com.harana.ui.components.elements.{DialogStyle, Page, Dialog => HaranaDialog}
import com.harana.ui.components.structure.Grid
import com.harana.ui.external.shoelace.{TabPanel, _}
import slinky.core.{CustomAttribute, FunctionalComponent}
import slinky.core.annotations.react
import slinky.core.facade.Fragment
import slinky.core.facade.{React, ReactElement}
import slinky.web.html._

import scala.scalajs.js

@react object ShoelacePage {
  type Props = Unit

  val component = FunctionalComponent[Unit] { _ =>

    def group(name: String, element:  ReactElement) = List[ReactElement](h5(name), element)

    val alertRef = React.createRef[Alert.Def]
    val dialogRef = React.createRef[HaranaDialog.Def]
    val drawerRef = React.createRef[Drawer.Def]

    val items = List[ReactElement](

      // Alert
      group("Alert", Fragment(
        Alert(closable = Some(true))(List(h6("Alert"))).withRef(alertRef),
        Button(label = Some("Open Alert"), onClick = Some(_ => {
          alertRef.current.toast()
        }))
      )),

      // Avatar
      group("Avatar", Avatar(initials = Some("HP"))),

      // Badge
      group("Badge", Badge(value = "5", pill = Some(true), pulse = Some(true))),

      // Button
      group("Button", Button(label = Some("Button"), onClick = Some(_ => println("Button Clicked")))),

      // Button Group
      group("Button Group", ButtonGroup(label = Some("Buttons"))(
        List(
          Button(label = Some("Button")),
          Button(label = Some("Button 2")),
          Button(label = Some("Button 3")),
          Button(label = Some("Button 4"))
        )
      )),

      // Card
      group("Card", Card(className = Some("snippets"))(List(
        div(CustomAttribute[String]("slot") := "header")("Tags", IconButton(name = "gear")),
        "The quick brown fox jumped over the lazy dog"
      ))),

      // Checkbox
      group("Checkbox", Checkbox(name = "Checked", checked = Some(true))),

      // Color Picker
      group("ColorPicker", ColorPicker(name = "ColorPicker", format = Some("hex"), onChange = Some(v => println(v)))),

      // Details
      group("Details", Details(summary = Some("Click to open"))(List("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna\n  aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat."))),

      // Drawer
      group("Drawer", Fragment(
        Drawer(label = Some("Drawer"))(
          List("The quick brown fox jumped over the lazy dog")
        ).withRef(drawerRef),
        Button(label = Some("Open Drawer"), onClick = Some(_ => {
          drawerRef.current.show()
        }))
      )),

      // Dialog (Confirm)
      group("Dialog (Confirm)", Fragment(
        // HaranaDialog("Erase File", Some(DialogStyle.Confirm("Are you sure you want to erase this file ?", "Erase", onCancel = Some(_ => println("Cancel")), onOk = _ => println("OK")))).withRef(dialogRef),
        // Button(label = Some("Open Dialog"), onClick = Some(_ => {
        //   dialogRef.current.show()
        // }))
      )),

      // Dropdown
      group("Dropdown", Dropdown(
        button = Some(Button.Props(icon = Some("icomoon","Button"), slot = Some("trigger"), caret = Some(true))),
        menu = Some(Menu.Props(items = List(
          MenuLabel("Section One"),
          MenuItem("Menu Item 1"),
          MenuItem("Menu Item 2"),
          MenuDivider(),
          MenuLabel("Section Two"),
          MenuItem("Menu Item 3"),
          MenuItem("Menu Item 4")
        ))))),

      // Icon

      group("Icon", Icon(name = "battery")),

      // IconButton
      group("IconButton", IconButton(name = "sliders")),

      // Input
      group("Input", Input(name = "Input", onChange = Some(v => { println(v) }), clearable = Some(true), placeholder = None, size = Some("small"))),

      // Menu Dropdown
      group("Menu Dropdown", Dropdown(
        button = Some(Button.Props(icon = Some("icomoon", "list"), slot = Some("trigger"), caret = Some(true))),
        menu = Some(Menu.Props(items = List(
          MenuLabel("Section One"),
          MenuItem("Menu Item 1"),
          MenuItem("Menu Item 2"),
          MenuDivider(),
          MenuLabel("Section Two"),
          MenuItem("Menu Item 3"),
          MenuItem("Menu Item 4")
        ))))),

      // Progress Bar
      group("ProgressBar", ProgressBar(value = Some(50))),

      // Progress Ring
      group("ProgressRing", ProgressRing(value = Some(30), size = Some(100), trackWidth = Some(10))),

      // Radio
      group("Radio", Radio(name = "option", checked = Some(true))),

      // Range
      group("Range", Range(name = "Range", min = Some(5), max = Some(100), step = Some(5), onChange = Some(v => println(v)))),

      // Rating
      group("Rating", Rating(max = Some(4), onChange = Some(v => println(v)))),

      // Select
      group("Select", Select(name = "Select", onChange = Some(v => println(v)), placeholder = Some("Select .."), options = List(
        MenuLabel("Section One"),
        MenuItem("Menu Item 1"),
        MenuItem("Menu Item 2"),
        MenuDivider(),
        MenuLabel("Section Two"),
        MenuItem("Menu Item 3"),
        MenuItem("Menu Item 4")
      ))),

      // Spinner
      group("Spinner", Spinner(className = Some("designer-spinner"))),

      // Switch
      group("Switch", Switch(name = "Switch", checked = Some(true), onChange = Some(x =>{
        println(s"There has been a change: $x")
      }))),

      // Tab
      group("Tab", TabGroup(placement = Some("left"))(List(
       Tab(panel = "One", label = "One"),
       Tab(panel = "Two", label = "Two"),
       Tab(panel = "Three", label = "Three"),
       Tab(panel = "Four", label = "Four"),
       TabPanel(name = "One")(List("Tab Panel 1")),
       TabPanel(name = "Two")(List("Tab Panel 2")),
       TabPanel(name = "Three")(List("Tab Panel 3")),
       TabPanel(name = "Four")(List("Tab Panel 4"))
      ))),

    // Tag
      group("Tag", Tag(label = "Tag One", variant = Some("danger"), clearable = Some(true))),

      // TextArea
      group("TextArea", TextArea(name = "TextArea", rows = Some(5), placeholder = Some("Enter something .."))),

      // Tooltip
      group("Tooltip", Tooltip(content = "This is a tooltip")(List(Avatar(initials = Some("NF")))))
    )

    Page(
      title = "Shoelace",
      navigationBar = Some(Navigation(())),
      content = Grid(
        items,
        ColumnSize.Three
      )
    )
  }
}