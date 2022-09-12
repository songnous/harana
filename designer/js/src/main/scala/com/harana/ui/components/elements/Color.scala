package com.harana.ui.components.elements

import enumeratum.values.{StringCirceEnum, StringEnum, StringEnumEntry}

sealed abstract class Color(val value: String, val html: String) extends StringEnumEntry
case object Color extends StringEnum[Color] with StringCirceEnum[Color] {

  case object White extends Color("default-400", "#")

  case object DarkBlue800 extends Color("primary-800", "#1565C0")
  case object DarkBlue700 extends Color("primary-700", "#1976D2")
  case object DarkBlue600 extends Color("primary-600", "#1E88E5")
  case object DarkBlue500 extends Color("primary-500", "#2196F3")
  case object DarkBlue400 extends Color("primary-400", "#42A5F5")
  case object DarkBlue300 extends Color("primary-300", "#64B5F6")

  case object Blue800 extends Color("blue-800", "#0277BD")
  case object Blue700 extends Color("blue-700", "#0288D1")
  case object Blue600 extends Color("blue-600", "#039BE5")
  case object Blue500 extends Color("blue-500", "#03A9F4")
  case object Blue400 extends Color("blue-400", "#29B6F6")
  case object Blue300 extends Color("blue-300", "#4FC3F7")

  case object Red800 extends Color("danger-800", "#C62828")
  case object Red700 extends Color("danger-700", "#D32F2F")
  case object Red600 extends Color("danger-600", "#E53935")
  case object Red500 extends Color("danger-500", "#F44336")
  case object Red400 extends Color("danger-400", "#EF5350")
  case object Red300 extends Color("danger-300", "#E57373")

  case object Orange800 extends Color("orange-800", "#EF6C00")
  case object Orange700 extends Color("orange-700", "#F57C00")
  case object Orange600 extends Color("orange-600", "#FB8C00")
  case object Orange500 extends Color("orange-500", "#FF9800")
  case object Orange400 extends Color("orange-400", "#FFA726")
  case object Orange300 extends Color("orange-300", "#FFA726")

  case object Teal800 extends Color("teal-800", "#00695C")
  case object Teal700 extends Color("teal-700", "#00796B")
  case object Teal600 extends Color("teal-600", "#00897B")
  case object Teal500 extends Color("teal-500", "#009688")
  case object Teal400 extends Color("teal-400", "#26A69A")
  case object Teal300 extends Color("teal-300", "#4DB6AC")

  case object Pink800 extends Color("pink-800", "#AD1457")
  case object Pink700 extends Color("pink-700", "#C2185B")
  case object Pink600 extends Color("pink-600", "#D81B60")
  case object Pink500 extends Color("pink-500", "#E91E63")
  case object Pink400 extends Color("pink-400", "#EC407A")
  case object Pink300 extends Color("pink-300", "#F06292")

  case object Indigo800 extends Color("indigo-800", "#283593")
  case object Indigo700 extends Color("indigo-700", "#303F9F")
  case object Indigo600 extends Color("indigo-600", "#3949AB")
  case object Indigo500 extends Color("indigo-500", "#3F51B5")
  case object Indigo400 extends Color("indigo-400", "#5C6BC0")
  case object Indigo300 extends Color("indigo-300", "#7986CB")

  case object Turquoise800 extends Color("info-800", "#00838F")
  case object Turquoise700 extends Color("info-700", "#0097A7")
  case object Turquoise600 extends Color("info-600", "#00ACC1")
  case object Turquoise500 extends Color("info-500", "#00BCD4")
  case object Turquoise400 extends Color("info-400", "#26C6DA")
  case object Turquoise300 extends Color("info-300", "#4DD0E1")

  case object Vermillion800 extends Color("warning-800", "#D84315")
  case object Vermillion700 extends Color("warning-700", "#E64A19")
  case object Vermillion600 extends Color("warning-600", "#F4511E")
  case object Vermillion500 extends Color("warning-500", "#FF5722")
  case object Vermillion400 extends Color("warning-400", "#FF7043")
  case object Vermillion300 extends Color("warning-300", "#FF8A65")

  case object Violet800 extends Color("violet-800", "#6A1B9A")
  case object Violet700 extends Color("violet-700", "#7B1FA2")
  case object Violet600 extends Color("violet-600", "#8E24AA")
  case object Violet500 extends Color("violet-500", "#9C27B0")
  case object Violet400 extends Color("violet-400", "#AB47BC")
  case object Violet300 extends Color("violet-300", "#BA68C8")

  case object Purple800 extends Color("purple-800", "#4527A0")
  case object Purple700 extends Color("purple-700", "#512DA8")
  case object Purple600 extends Color("purple-600", "#5E35B1")
  case object Purple500 extends Color("purple-500", "#673AB7")
  case object Purple400 extends Color("purple-400", "#7E57C2")
  case object Purple300 extends Color("purple-300", "#9575CD")

  case object Green800 extends Color("success-800", "#2E7D32")
  case object Green700 extends Color("success-700", "#388E3C")
  case object Green600 extends Color("success-600", "#43A047")
  case object Green500 extends Color("success-500", "#4CAF50")
  case object Green400 extends Color("success-400", "#66BB6A")
  case object Green300 extends Color("success-300", "#81C784")

  case object LimeGreen800 extends Color("green-800", "#558B2F")
  case object LimeGreen700 extends Color("green-700", "#689F38")
  case object LimeGreen600 extends Color("green-600", "#7CB342")
  case object LimeGreen500 extends Color("green-500", "#8BC34A")
  case object LimeGreen400 extends Color("green-400", "#9CCC65")
  case object LimeGreen300 extends Color("green-300", "#AED581")

  case object Brown800 extends Color("brown-800", "#4E342E")
  case object Brown700 extends Color("brown-700", "#5D4037")
  case object Brown600 extends Color("brown-600", "#6D4C41")
  case object Brown500 extends Color("brown-500", "#795548")
  case object Brown400 extends Color("brown-400", "#8D6E63")
  case object Brown300 extends Color("brown-300", "#A1887F")

  case object Grey800 extends Color("grey-800", "#444444")
  case object Grey700 extends Color("grey-700", "#5555555")
  case object Grey600 extends Color("grey-600", "#666666")
  case object Grey500 extends Color("grey-500", "#777777")
  case object Grey400 extends Color("grey-400", "#888888")
  case object Grey300 extends Color("grey-300", "#999999")

  case object Slate800 extends Color("slate-800", "#37474F")
  case object Slate700 extends Color("slate-700", "#455A64")
  case object Slate600 extends Color("slate-600", "#546E7A")
  case object Slate500 extends Color("slate-500", "#607D8B")
  case object Slate400 extends Color("slate-400", "#78909C")
  case object Slate300 extends Color("slate-300", "#90A4AE")

  val values = findValues
}