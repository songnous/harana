package com.harana.ui.components.elements

import com.harana.shared.models.HaranaFile
import com.harana.ui.components.LinkType
import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.web.html._

@react class DataFileName extends StatelessComponent {

  case class Props(HaranaFile: HaranaFile)

  def render() = {
    val categories =
      Map(
        "c"         -> "Code",
        "cpp"       -> "Code",
        "java"      -> "Code",
        "py"        -> "Code",
        "pyc"       -> "Code",
        "scala"     -> "Code",
        "sql"       -> "Code",
        "r"         -> "Code",

        "arrow"     -> "Data",
        "avro"      -> "Data",
        "csv"       -> "Data",
        "db"        -> "Data",
        "json"      -> "Data",
        "orc"       -> "Data",
        "parquet"   -> "Data",
        "xls"       -> "Data",
        "xml"       -> "Data",

        "jpg"       -> "Media",
        "png"       -> "Media",
        "mov"       -> "Media",
        "mp3"       -> "Media",
        "mp4"       -> "Media",

        "doc"       -> "Text",
        "rtf"       -> "Text",
        "txt"       -> "Text"
      )

    val imgType = if (props.HaranaFile.isFolder) "folder" else {
      props.HaranaFile.extension.flatMap(categories.get) match {
        case Some("Code")   => "blue"
        case Some("Data")   => "pink"
        case Some("Media")  => "orange"
        case Some("Text")   => "purple"
        case _              => "yellow"
      }
    }

    div(className := "HaranaFile-name")(
      img(src := s"/public/images/data/$imgType.svg"),
      h6(key := props.HaranaFile.name)(props.HaranaFile.name)
    )
  }
}