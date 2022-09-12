package com.harana.ui.external.emoji

import com.harana.ui.external.emoji.Types._
import slinky.core.ExternalComponent
import slinky.core.annotations.react

import scala.scalajs.js
import scala.scalajs.js.annotation.{JSImport, JSName}
import scala.scalajs.js.|

@JSImport("emoji-mart/css/emoji-mart.css", JSImport.Default)
@js.native
object EmojiCSS extends js.Object

@JSImport("emoji-mart", "Picker")
@js.native
object ReactEmojiPicker extends js.Object

@react object EmojiPicker extends ExternalComponent {

  val i18n = js.Dynamic.literal(
    "search" -> "Search",
    "clear" -> "Clear",
    "notfound" -> "No Emoji Found",
    "skintext" -> "Choose your default skin tone",
    "categorieslabel" -> "Emoji categories",
    "categories" -> js.Dynamic.literal(
      "search" -> "Search Results", 
      "recent" -> "Frequently Used",
      "people" -> "Smileys & People", 
      "nature" -> "Animals & Nature",
      "foods" -> "Food & Drink", 
      "activity" -> "Activity",
      "places" -> "Travel & Places",
      "objects" -> "Objects",
      "symbols" -> "Symbols", 
      "flags" -> "Flags",
      "custom" -> "Custom"
    ).asInstanceOf[I18nCategories],
    "skintones" -> js.Dynamic.literal(
      "1" -> "Default Skin Tone",
      "2" -> "Light Skin Tone",
      "3" -> "Medium-Light Skin Tone",
      "4" -> "Medium Skin Tone",
      "5" -> "Medium-Dark Skin Tone",
      "6" -> "Dark Skin Tone",
    ).asInstanceOf[I18nSkinTones]
  ).asInstanceOf[I18n]

  val icons = js.Dynamic.literal(
    "categories" -> js.undefined,
    "search" -> js.undefined,
  ).asInstanceOf[CustomIcons]

  case class Props(autoFocus: Option[Boolean] = None,
                   color: Option[String] = None,
                   emoji: Option[Boolean] = None,
                   include: List[String] = List(),
                   exclude: List[String] = List(),
                   custom: List[EmojiData] = List(),
                   recent: List[String] = List(),
                   emojiSize: Option[Int] = None,
                   emojis: List[EmojiData] = List(),
                   onClick: Option[OnClick] = None,
                   onSelect: Option[OnSelect] = None,
                   onSkinChange: Option[OnSkinChange] = None,
                   perLine: Option[Int] = None,
                   i18n: Option[I18n] = None,
                   native: Option[Boolean] = None,
                   set: Option[String] = None,
                   sheetSize: Option[Int] = None,
                   backgroundImageFn: Option[BackgroundImageFn] = None,
                   emojisToShowFilter: Option[EmojisToShowFilter] = None,
                   showPreview: Option[Boolean] = None,
                   showSkinTones: Option[Boolean] = None,
                   emojiTooltip: Option[Boolean] = None,
                   skin: Option[Int] = None,
                   defaultSkin: Option[Int] = None,
                   skinEmoji: Option[String] = None,
                   style: Option[Boolean] = None,
                   title: Option[String] = None,
                   notFoundEmoji: Option[String] = None,
                   notFound: Option[NotFound] = None,
                   icons: Option[CustomIcons] = None)

  override val component = ReactEmojiPicker
}

@JSImport("emoji-mart", "Emoji")
@js.native
object ReactEmoji extends js.Object

@react object Emoji extends ExternalComponent {

  case class Props(emoji: String | EmojiData,
                   size: Int = 24,
                   native: Option[Boolean] = None,
                   onClick: Option[OnClick] = None,
                   onLeave: Option[OnLeave] = None,
                   onOver: Option[OnOver] = None,
                   fallback: Option[Fallback] = None,
                   set: Option[String] = None,
                   sheetSize: Option[Int] = None,
                   backgroundImageFn: Option[BackgroundImageFn] = None,
                   skin: Option[Int] = None,
                   tooltip: Option[String] = None,
                   html: Option[String] = None)

  override val component = ReactEmoji
}

object Types {
  type OnClick = (EmojiData, js.Object) => Unit
  type OnLeave = (EmojiData, js.Object) => Unit
  type OnOver = (EmojiData, js.Object) => Unit
  type Fallback = (EmojiData, js.Object) => Unit
  type BackgroundImageFn = (String, Int) => String
  type OnSelect = EmojiData => Unit
  type OnSkinChange = Int => Unit
  type EmojisToShowFilter = EmojiData => Boolean
  type NotFound = () => String
}

@js.native
trait EmojiData extends js.Object {
  val id: Option[String]
  val name: Option[String]
  val colors: Option[String]
  val emoticons: List[String]
  val skin: Option[Int]
  val native: Option[String]
  val custom: Option[String]
  val imageURL: Option[Boolean]
  val text: Option[String]
}

@js.native
trait CustomIcons extends js.Object {
  val categories: Option[CustomIconsCategories]
  val search: Option[CustomIconsSearch]
}

@js.native
trait CustomIconsCategories extends js.Object {
  val search: Option[() => String]
  val recent: Option[() => String]
  val people: Option[() => String]
  val nature: Option[() => String]
  val foods: Option[() => String]
  val activity: Option[() => String]
  val places: Option[() => String]
  val objects: Option[() => String]
  val symbols: Option[() => String]
  val flags: Option[() => String]
  val custom: Option[() => String]
}

@js.native
trait CustomIconsSearch extends js.Object {
  val search: Option[() => String]
  val delete: Option[() => String]
}

@js.native
trait I18n extends js.Object {
  val search: Option[String]
  val clear: Option[String]
  val notfound: Option[String]
  val skintext: Option[String]
  val categories: Option[I18nCategories]
  val categorieslabel: Option[String]
  val skintones: Option[I18nSkinTones]
}

@js.native
trait I18nCategories extends js.Object {
  val search: Option[String]
  val recent: Option[String]
  val people: Option[String]
  val nature: Option[String]
  val foods: Option[String]
  val activity: Option[String]
  val places: Option[String]
  val objects: Option[String]
  val symbols: Option[String]
  val flags: Option[String]
  val custom: Option[String]
} 

@js.native
trait I18nSkinTones extends js.Object {
  @JSName("1") val one: Option[String]
  @JSName("2") val two: Option[String]
  @JSName("3") val three: Option[String]
  @JSName("4") val four: Option[String]
  @JSName("5") val five: Option[String]
  @JSName("6") val six: Option[String]
}