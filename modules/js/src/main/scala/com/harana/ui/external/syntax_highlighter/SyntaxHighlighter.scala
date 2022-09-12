package com.harana.ui.external.syntax_highlighter

import slinky.core.ExternalComponent
import slinky.core.annotations.react
import typings.react.mod.CSSProperties

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

sealed trait HighlightStyle extends js.Object

object HighlightStyle {

  // @JSImport("react-syntax-highlighter/dist/esm/styles/hljs", "agate")
  // @js.native
  // object agate extends HighlightStyle

  // @JSImport("react-syntax-highlighter/dist/esm/styles/hljs", "androidstudio")
  // @js.native
  // object androidstudio extends HighlightStyle

  // @JSImport("react-syntax-highlighter/dist/esm/styles/hljs", "arduinoLight")
  // @js.native
  // object arduinoLight extends HighlightStyle

  // @JSImport("react-syntax-highlighter/dist/esm/styles/hljs", "arta")
  // @js.native
  // object arta extends HighlightStyle

  // @JSImport("react-syntax-highlighter/dist/esm/styles/hljs", "ascetic")
  // @js.native
  // object ascetic extends HighlightStyle

  // @JSImport("react-syntax-highlighter/dist/esm/styles/hljs", "atelierCaveDark")
  // @js.native
  // object atelierCaveDark extends HighlightStyle

  // @JSImport("react-syntax-highlighter/dist/esm/styles/hljs", "atelierCaveLight")
  // @js.native
  // object atelierCaveLight extends HighlightStyle

  // @JSImport("react-syntax-highlighter/dist/esm/styles/hljs", "atelierDuneDark")
  // @js.native
  // object atelierDuneDark extends HighlightStyle

  // @JSImport("react-syntax-highlighter/dist/esm/styles/hljs", "atelierDuneLight")
  // @js.native
  // object atelierDuneLight extends HighlightStyle

  // @JSImport("react-syntax-highlighter/dist/esm/styles/hljs", "atelierEstuaryDark")
  // @js.native
  // object atelierEstuaryDark extends HighlightStyle

  // @JSImport("react-syntax-highlighter/dist/esm/styles/hljs", "atelierEstuaryLight")
  // @js.native
  // object atelierEstuaryLight extends HighlightStyle

  // @JSImport("react-syntax-highlighter/dist/esm/styles/hljs", "atelierForestDark")
  // @js.native
  // object atelierForestDark extends HighlightStyle

  // @JSImport("react-syntax-highlighter/dist/esm/styles/hljs", "atelierForestLight")
  // @js.native
  // object atelierForestLight extends HighlightStyle

  // @JSImport("react-syntax-highlighter/dist/esm/styles/hljs", "atelierHeathDark")
  // @js.native
  // object atelierHeathDark extends HighlightStyle

  // @JSImport("react-syntax-highlighter/dist/esm/styles/hljs", "atelierHeathLight")
  // @js.native
  // object atelierHeathLight extends HighlightStyle

  // @JSImport("react-syntax-highlighter/dist/esm/styles/hljs", "atelierLakesideDark")
  // @js.native
  // object atelierLakesideDark extends HighlightStyle

  // @JSImport("react-syntax-highlighter/dist/esm/styles/hljs", "atelierLakesideLight")
  // @js.native
  // object atelierLakesideLight extends HighlightStyle

  // @JSImport("react-syntax-highlighter/dist/esm/styles/hljs", "atelierPlateauDark")
  // @js.native
  // object atelierPlateauDark extends HighlightStyle

  // @JSImport("react-syntax-highlighter/dist/esm/styles/hljs", "atelierPlateauLight")
  // @js.native
  // object atelierPlateauLight extends HighlightStyle

  // @JSImport("react-syntax-highlighter/dist/esm/styles/hljs", "atelierSavannaDark")
  // @js.native
  // object atelierSavannaDark extends HighlightStyle

  // @JSImport("react-syntax-highlighter/dist/esm/styles/hljs", "atelierSavannaLight")
  // @js.native
  // object atelierSavannaLight extends HighlightStyle

  // @JSImport("react-syntax-highlighter/dist/esm/styles/hljs", "atelierSeasideDark")
  // @js.native
  // object atelierSeasideDark extends HighlightStyle

  // @JSImport("react-syntax-highlighter/dist/esm/styles/hljs", "atelierSeasideLight")
  // @js.native
  // object atelierSeasideLight extends HighlightStyle

  // @JSImport("react-syntax-highlighter/dist/esm/styles/hljs", "atelierSulphurpoolDark")
  // @js.native
  // object atelierSulphurpoolDark extends HighlightStyle

  // @JSImport("react-syntax-highlighter/dist/esm/styles/hljs", "atelierSulphurpoolLight")
  // @js.native
  // object atelierSulphurpoolLight extends HighlightStyle

  // @JSImport("react-syntax-highlighter/dist/esm/styles/hljs", "atomOneDark")
  // @js.native
  // object atomOneDark extends HighlightStyle

  @JSImport("react-syntax-highlighter/dist/esm/styles/hljs", "atomOneLight")
  @js.native
  object atomOneLight extends HighlightStyle

  // @JSImport("react-syntax-highlighter/dist/esm/styles/hljs", "brownPaper")
  // @js.native
  // object brownPaper extends HighlightStyle

  // @JSImport("react-syntax-highlighter/dist/esm/styles/hljs", "codepenEmbed")
  // @js.native
  // object codepenEmbed extends HighlightStyle

  // @JSImport("react-syntax-highlighter/dist/esm/styles/hljs", "colorBrewer")
  // @js.native
  // object colorBrewer extends HighlightStyle

  // @JSImport("react-syntax-highlighter/dist/esm/styles/hljs", "darcula")
  // @js.native
  // object darcula extends HighlightStyle

  // @JSImport("react-syntax-highlighter/dist/esm/styles/hljs", "dark")
  // @js.native
  // object dark extends HighlightStyle

  // @JSImport("react-syntax-highlighter/dist/esm/styles/hljs", "darkula")
  // @js.native
  // object darkula extends HighlightStyle

  // @JSImport("react-syntax-highlighter/dist/esm/styles/hljs", "defaultStyle")
  // @js.native
  // object defaultStyle extends HighlightStyle

  // @JSImport("react-syntax-highlighter/dist/esm/styles/hljs", "docco")
  // @js.native
  // object docco extends HighlightStyle

  // @JSImport("react-syntax-highlighter/dist/esm/styles/hljs", "dracula")
  // @js.native
  // object dracula extends HighlightStyle

  // @JSImport("react-syntax-highlighter/dist/esm/styles/hljs", "far")
  // @js.native
  // object far extends HighlightStyle

  // @JSImport("react-syntax-highlighter/dist/esm/styles/hljs", "foundation")
  // @js.native
  // object foundation extends HighlightStyle

  // @JSImport("react-syntax-highlighter/dist/esm/styles/hljs", "githubGist")
  // @js.native
  // object githubGist extends HighlightStyle

  // @JSImport("react-syntax-highlighter/dist/esm/styles/hljs", "github")
  // @js.native
  // object github extends HighlightStyle

  // @JSImport("react-syntax-highlighter/dist/esm/styles/hljs", "googlecode")
  // @js.native
  // object googlecode extends HighlightStyle

  // @JSImport("react-syntax-highlighter/dist/esm/styles/hljs", "grayscale")
  // @js.native
  // object grayscale extends HighlightStyle

  // @JSImport("react-syntax-highlighter/dist/esm/styles/hljs", "gruvboxDark")
  // @js.native
  // object gruvboxDark extends HighlightStyle

  // @JSImport("react-syntax-highlighter/dist/esm/styles/hljs", "gruvboxLight")
  // @js.native
  // object gruvboxLight extends HighlightStyle

  // @JSImport("react-syntax-highlighter/dist/esm/styles/hljs", "hopscotch")
  // @js.native
  // object hopscotch extends HighlightStyle

  // @JSImport("react-syntax-highlighter/dist/esm/styles/hljs", "hybrid")
  // @js.native
  // object hybrid extends HighlightStyle

  // @JSImport("react-syntax-highlighter/dist/esm/styles/hljs", "idea")
  // @js.native
  // object idea extends HighlightStyle

  // @JSImport("react-syntax-highlighter/dist/esm/styles/hljs", "irBlack")
  // @js.native
  // object irBlack extends HighlightStyle

  // @JSImport("react-syntax-highlighter/dist/esm/styles/hljs", "kimbieDark")
  // @js.native
  // object kimbieDark extends HighlightStyle

  // @JSImport("react-syntax-highlighter/dist/esm/styles/hljs", "kimbieLight")
  // @js.native
  // object kimbieLight extends HighlightStyle

  // @JSImport("react-syntax-highlighter/dist/esm/styles/hljs", "magula")
  // @js.native
  // object magula extends HighlightStyle

  // @JSImport("react-syntax-highlighter/dist/esm/styles/hljs", "monoBlue")
  // @js.native
  // object monoBlue extends HighlightStyle

  // @JSImport("react-syntax-highlighter/dist/esm/styles/hljs", "monokaiSublime")
  // @js.native
  // object monokaiSublime extends HighlightStyle

  // @JSImport("react-syntax-highlighter/dist/esm/styles/hljs", "monokai")
  // @js.native
  // object monokai extends HighlightStyle

  // @JSImport("react-syntax-highlighter/dist/esm/styles/hljs", "obsidian")
  // @js.native
  // object obsidian extends HighlightStyle

  // @JSImport("react-syntax-highlighter/dist/esm/styles/hljs", "ocean")
  // @js.native
  // object ocean extends HighlightStyle

  // @JSImport("react-syntax-highlighter/dist/esm/styles/hljs", "paraisoDark")
  // @js.native
  // object paraisoDark extends HighlightStyle

  // @JSImport("react-syntax-highlighter/dist/esm/styles/hljs", "paraisoLight")
  // @js.native
  // object paraisoLight extends HighlightStyle

  // @JSImport("react-syntax-highlighter/dist/esm/styles/hljs", "pojoaque")
  // @js.native
  // object pojoaque extends HighlightStyle

  // @JSImport("react-syntax-highlighter/dist/esm/styles/hljs", "purebasic")
  // @js.native
  // object purebasic extends HighlightStyle

  // @JSImport("react-syntax-highlighter/dist/esm/styles/hljs", "qtcreatorDark")
  // @js.native
  // object qtcreatorDark extends HighlightStyle

  // @JSImport("react-syntax-highlighter/dist/esm/styles/hljs", "qtcreatorLight")
  // @js.native
  // object qtcreatorLight extends HighlightStyle

  // @JSImport("react-syntax-highlighter/dist/esm/styles/hljs", "railscasts")
  // @js.native
  // object railscasts extends HighlightStyle

  // @JSImport("react-syntax-highlighter/dist/esm/styles/hljs", "rainbow")
  // @js.native
  // object rainbow extends HighlightStyle

  // @JSImport("react-syntax-highlighter/dist/esm/styles/hljs", "routeros")
  // @js.native
  // object routeros extends HighlightStyle

  // @JSImport("react-syntax-highlighter/dist/esm/styles/hljs", "schoolBook")
  // @js.native
  // object schoolBook extends HighlightStyle

  // @JSImport("react-syntax-highlighter/dist/esm/styles/hljs", "solarizedDark")
  // @js.native
  // object solarizedDark extends HighlightStyle

  // @JSImport("react-syntax-highlighter/dist/esm/styles/hljs", "solarizedLight")
  // @js.native
  // object solarizedLight extends HighlightStyle

  // @JSImport("react-syntax-highlighter/dist/esm/styles/hljs", "sunburst")
  // @js.native
  // object sunburst extends HighlightStyle

  // @JSImport("react-syntax-highlighter/dist/esm/styles/hljs", "tomorrowNightBlue")
  // @js.native
  // object tomorrowNightBlue extends HighlightStyle

  // @JSImport("react-syntax-highlighter/dist/esm/styles/hljs", "tomorrowNightBright")
  // @js.native
  // object tomorrowNightBright extends HighlightStyle

  // @JSImport("react-syntax-highlighter/dist/esm/styles/hljs", "tomorrowNightEighties")
  // @js.native
  // object tomorrowNightEighties extends HighlightStyle

  // @JSImport("react-syntax-highlighter/dist/esm/styles/hljs", "tomorrowNight")
  // @js.native
  // object tomorrowNight extends HighlightStyle

  // @JSImport("react-syntax-highlighter/dist/esm/styles/hljs", "tomorrow")
  // @js.native
  // object tomorrow extends HighlightStyle

  // @JSImport("react-syntax-highlighter/dist/esm/styles/hljs", "vs")
  // @js.native
  // object vs extends HighlightStyle

  // @JSImport("react-syntax-highlighter/dist/esm/styles/hljs", "vs2015")
  // @js.native
  // object vs2015 extends HighlightStyle

  // @JSImport("react-syntax-highlighter/dist/esm/styles/hljs", "xcode")
  // @js.native
  // object xcode extends HighlightStyle

  // @JSImport("react-syntax-highlighter/dist/esm/styles/hljs", "xt256")
  // @js.native
  // object xt256 extends HighlightStyle

  // @JSImport("react-syntax-highlighter/dist/esm/styles/hljs", "zenburn")
  // @js.native
  // object zenburn extends HighlightStyle

//   lazy val all = List(
//     agate,
//     androidstudio,
//     arduinoLight,
//     arta,
//     ascetic,
//     atelierCaveDark,
//     atelierCaveLight,
//     atelierDuneDark,
//     atelierDuneLight,
//     atelierEstuaryDark,
//     atelierEstuaryLight,
//     atelierForestDark,
//     atelierForestLight,
//     atelierHeathDark,
//     atelierHeathLight,
//     atelierLakesideDark,
//     atelierLakesideLight,
//     atelierPlateauDark,
//     atelierPlateauLight,
//     atelierSavannaDark,
//     atelierSavannaLight,
//     atelierSeasideDark,
//     atelierSeasideLight,
//     atelierSulphurpoolDark,
//     atelierSulphurpoolLight,
//     atomOneDark,
//     atomOneLight,
//     brownPaper,
//     codepenEmbed,
//     colorBrewer,
//     darcula,
//     dark,
//     darkula,
//     defaultStyle,
//     docco,
//     dracula,
//     far,
//     foundation,
//     githubGist,
//     github,
//     googlecode,
//     grayscale,
//     gruvboxDark,
//     gruvboxLight,
//     hopscotch,
//     hybrid,
//     idea,
//     irBlack,
//     kimbieDark,
//     kimbieLight,
//     magula,
//     monoBlue,
//     monokaiSublime,
//     monokai,
//     obsidian,
//     ocean,
//     paraisoDark,
//     paraisoLight,
//     pojoaque,
//     purebasic,
//     qtcreatorDark,
//     qtcreatorLight,
//     railscasts,
//     rainbow,
//     routeros,
//     schoolBook,
//     solarizedDark,
//     solarizedLight,
//     sunburst,
//     tomorrowNightBlue,
//     tomorrowNightBright,
//     tomorrowNightEighties,
//     tomorrowNight,
//     tomorrow,
//     vs,
//     vs2015,
//     xcode,
//     xt256,
//     zenburn
//   )
}

//import SyntaxHighlighter from 'react-syntax-highlighter/dist/esm/prism-light'


@js.native
@JSImport("react-syntax-highlighter/dist/esm/languages/hljs/bash", JSImport.Default)
object Bash extends js.Object

@js.native
@JSImport("react-syntax-highlighter/dist/esm/languages/hljs/c-like", JSImport.Default)
object C extends js.Object

@js.native
@JSImport("react-syntax-highlighter/dist/esm/languages/hljs/css", JSImport.Default)
object CSS extends js.Object

@js.native
@JSImport("react-syntax-highlighter/dist/esm/languages/hljs/dockerfile", JSImport.Default)
object Dockerfile extends js.Object

@js.native
@JSImport("react-syntax-highlighter/dist/esm/languages/hljs/go", JSImport.Default)
object Go extends js.Object

@js.native
@JSImport("react-syntax-highlighter/dist/esm/languages/hljs/java", JSImport.Default)
object Java extends js.Object

@js.native
@JSImport("react-syntax-highlighter/dist/esm/languages/hljs/javascript", JSImport.Default)
object JavaScript extends js.Object

@js.native
@JSImport("react-syntax-highlighter/dist/esm/languages/hljs/json", JSImport.Default)
object JSON extends js.Object

@js.native
@JSImport("react-syntax-highlighter/dist/esm/languages/hljs/markdown", JSImport.Default)
object Markdown extends js.Object

@js.native
@JSImport("react-syntax-highlighter/dist/esm/languages/hljs/plaintext", JSImport.Default)
object PlainText extends js.Object

@js.native
@JSImport("react-syntax-highlighter/dist/esm/languages/hljs/python", JSImport.Default)
object Python extends js.Object

@js.native
@JSImport("react-syntax-highlighter/dist/esm/languages/hljs/r", JSImport.Default)
object R extends js.Object

@js.native
@JSImport("react-syntax-highlighter/dist/esm/languages/hljs/scala", JSImport.Default)
object Scala extends js.Object

@js.native
@JSImport("react-syntax-highlighter/dist/esm/languages/hljs/sql", JSImport.Default)
object SQL extends js.Object

@js.native
@JSImport("react-syntax-highlighter/dist/esm/languages/hljs/yaml", JSImport.Default)
object YAML extends js.Object

@js.native
@JSImport("react-syntax-highlighter/dist/esm/languages/hljs/xml", JSImport.Default)
object XML extends js.Object

@JSImport("react-syntax-highlighter", "Light")
@js.native
object ReactSyntaxHighlighter extends js.Object

@react object SyntaxHighlighter extends ExternalComponent {

  RegisterLanguage("bash", Bash)
  RegisterLanguage("c", C)
  RegisterLanguage("css", CSS)
  RegisterLanguage("dockerfile", Dockerfile)
  RegisterLanguage("go", Go)
  RegisterLanguage("java", Java)
  RegisterLanguage("javascript", JavaScript)
  RegisterLanguage("json", JSON)
  RegisterLanguage("markdown", Markdown)
  RegisterLanguage("plaintext", PlainText)
  RegisterLanguage("python", Python)
  RegisterLanguage("r", R)
  RegisterLanguage("scala", Scala)
  RegisterLanguage("sql", SQL)
  RegisterLanguage("yaml", YAML)
  RegisterLanguage("xml", XML)

  case class Props(language: String,
                   style: HighlightStyle,
                   wrapLines: Option[Boolean] = None,
                   showLineNumbers: Option[Boolean] = None,
                   startingLineNumbrs: Option[Int] = None,
                   customStyle: Option[CSSProperties] = None)

  override val component = ReactSyntaxHighlighter
}

@js.native
@JSImport("react-syntax-highlighter/dist/esm/light", "registerLanguage")
object RegisterLanguage extends js.Function2[String, js.Object, Unit] {
  override def apply(arg1: String, arg2: js.Object): Unit = js.native
}
