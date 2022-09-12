package com.harana.ui.external.daterangepicker

import slinky.core.ExternalComponent
import slinky.core.annotations.react

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("react-bootstrap-daterangepicker.js", "DateRangePicker")
@js.native
object ReactDateRangePicker extends js.Object

@react object DateRangePicker extends ExternalComponent {

  case class Props(`<input>`: Option[js.Any] = None,
                   alwaysShowCalendars: Option[Boolean] = None,
                   applyClass: Option[String] = None,
                   autoApply: Option[Boolean] = None,
                   autoUpdateInput: Option[Boolean] = None,
                   buttonClasses: List[String] = List(),
                   cancelClass: Option[String] = None,
                   dateLimit: Option[js.Object] = None,
                   drops: Option[String] = None,
                   endDate: Option[String] = None,
                   isCustomDate: Option[String => Boolean] = None,
                   isInvalidDate: Option[String => Boolean] = None,
                   linkedCalendars: Option[Boolean] = None,
//                   locale: Option[js.Object] = None,
                   maxDate: Option[String] = None,
                   minDate: Option[String] = None,
                   opens: Option[String] = None,
                   parentEl: Option[js.Any] = None,
//                   ranges: Option[js.Object] = None,
                   showCustomRangeLabel: Option[Boolean] = None,
                   showDropdowns: Option[Boolean] = None,
                   showISOWeekNumbers: Option[Boolean] = None,
                   showWeekNumbers: Option[Boolean] = None,
                   singleDatePicker: Option[Boolean] = None,
                   startDate: String,
                   template: Option[js.Any] = None,
                   timePicker: Option[Boolean] = None,
                   timePicker24Hour: Option[Boolean] = None,
                   timePickerIncrement: Option[Int] = None,
                   timePickerSeconds: Option[Boolean] = None,
                   containerStyles: Option[js.Object] = None,
                   containerClass: Option[String] = None,
                   onShow: Option[(Event, js.Object)] = None,
                   onHide: Option[(Event, js.Object)] = None,
                   onShowCalendar: Option[(Event, js.Object) => Unit] = None,
                   onHideCalendar: Option[(Event, js.Object) => Unit] = None,
                   onApply: Option[(js.Object, js.Object) => Unit] = None,
                   onCancel: Option[(Event, js.Object) => Unit] = None,
                   onEvent: Option[(js.Object, js.Object) => Unit] = None,
                   onSelect: Option[(String, String, String) => Unit] = None)

  override val component = ReactDateRangePicker
}

trait Event extends js.Object {

}
