package com.harana.designer.frontend.test

import com.harana.designer.frontend.navigation.ui.Navigation
import com.harana.sdk.shared.models.common.Parameter
import com.harana.ui.components.elements.{Page, ParameterItem}
import slinky.core.FunctionalComponent
import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.web.html._

@react object ParametersPage {
  type Props = Unit

  val component = FunctionalComponent[Unit] { _ =>
    Page(
      title = "Parameters",
      navigationBar = Some(Navigation(())),
      content =
        List[ReactElement](
          h5("Boolean"),
//          ParameterItem(Parameter.Boolean("Boolean", "Boolean", "Boolean parameter")),
//          h5("Code"),
////          ParameterItem(Parameter.Code("Code", "Code", "Code parameter")),
//          h5("Color"),
////          ParameterItem(Parameter.Color("Color", "Color", "Color parameter")),
//          h5("Connection"),
////          ParameterItem(Parameter.Connection("Connection", "Connection", "Connection parameter")),
//          h5("Country"),
////        ParameterItem(Parameter.Country("Country", "Country", "Country parameter")),
//          h5("DataTable"),
//          //      ParameterItem(Parameter.DataTable("DataTable", "DataTable", "DataTable parameter")),
//          h5("Date"),
//          ParameterItem(Parameter.Date("Date", "Date", "Date parameter")),
//          h5("DateRange"),
//          ParameterItem(Parameter.DateRange("DateRange", "DateRange", "DateRange parameter")),
//          h5("DateTime"),
////          ParameterItem(Parameter.DateTime("DateTime", "DateTime", "DateTime parameter")),
//          h5("Decimal"),
//          ParameterItem(Parameter.Decimal("Decimal", "Decimal", "Decimal parameter")),
//          h5("DecimalRange"),
//          ParameterItem(Parameter.DecimalRange("DecimalRange", "DecimalRange", "DecimalRange parameter")),
//          h5("Email"),
//          ParameterItem(Parameter.Email("Email", "Email", "Email parameter")),
//          h5("Emoji"),
////          ParameterItem(Parameter.Emoji("Emoji", "Emoji", "Emoji parameter")),
//          h5("File"),
////          ParameterItem(Parameter.File("File", "File", "File parameter")),
//          h5("GeoAddress"),
//          ParameterItem(Parameter.GeoAddress("GeoAddress", "GeoAddress", "GeoAddress parameter")),
//          h5("GeoCoordinate"),
////          ParameterItem(Parameter.GeoCoordinate("GeoCoordinate", "GeoCoordinate", "GeoCoordinate parameter")),
//          h5("GeoLocation"),
////          ParameterItem(Parameter.GeoLocation("GeoLocation", "GeoLocation", "GeoLocation parameter")),
//          h5("Html"),
//          ParameterItem(Parameter.Html("Html", "Html", "Html parameter")),
//          h5("Image"),
//          ParameterItem(Parameter.Image("Image", "Image", "Image parameter")),
//          h5("Integer"),
//          ParameterItem(Parameter.Integer("Integer", "Integer", "Integer parameter")),
//          h5("Integer Range"),
//          ParameterItem(Parameter.IntegerRange("IntegerRange", "IntegerRange", "IntegerRange parameter")),
//          h5("IPAddress"),
//          ParameterItem(Parameter.IPAddress("IPAddress", "IPAddress", "IPAddress parameter")),
//          h5("Json"),
//          ParameterItem(Parameter.Json("Json", "Json", "Json parameter")),
//          h5("Markdown"),
//          ParameterItem(Parameter.Markdown("Markdown", "Markdown", "Markdown parameter")),
//          h5("Money"),
//          ParameterItem(Parameter.Money("Money", "Money", "Money parameter")),
//          h5("NewPassword"),
////          ParameterItem(Parameter.NewPassword("NewPassword", "NewPassword", "NewPassword parameter")),
//          h5("Page"),
////          ParameterItem(Parameter.Page("Page", "Page", "Page parameter")),
//          h5("ParameterList"),
////          ParameterItem(Parameter.ParameterList("ParameterList", "ParameterList", "ParameterList parameter")),
//          h5("Password"),
////          ParameterItem(Parameter.Password("Password", "Password", "Password parameter")),
//          h5("SearchQuery"),
//          ParameterItem(Parameter.SearchQuery("SearchQuery", "SearchQuery", "SearchQuery parameter")),
//          h5("String"),
//          ParameterItem(Parameter.String("String", "String", "String parameter")),
//          h5("Tags"),
//          ParameterItem(Parameter.Tags("Tags", "Tags", "Tags parameter")),
//          h5("Time"),
//          ParameterItem(Parameter.Time("Time", "Time", "Time parameter")),
//          h5("TimeZone"),
//          ParameterItem(Parameter.TimeZone("TimeZone", "TimeZone", "TimeZone parameter")),
//          h5("Uri"),
//          ParameterItem(Parameter.Uri("Uri", "Uri", "Uri parameter")),
//          h5("User"),
//          ParameterItem(Parameter.User("User", "User", "User parameter")),
//          h5("Video"),
//          ParameterItem(Parameter.Video("Video", "Video", "Video parameter")),
        )
    )
  }
}