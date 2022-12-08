package com.harana.ui.external

import org.scalajs.dom.Event
import slinky.core.{CustomAttribute, CustomTag, KeyAndRefAddingStage, TagMod}
import slinky.web.html.className

import scala.collection.mutable.ListBuffer
import scala.scalajs.js

package object shoelace {

  val libraryAttr = CustomAttribute[String]("library")
  val classAttr = CustomAttribute[String]("class")
  val nameAttr = CustomAttribute[String]("name")
  val partAttr = CustomAttribute[String]("part")
  val slotAttr = CustomAttribute[String]("slot")
  val srcAttr = CustomAttribute[String]("src")
  val keyAttr = CustomAttribute[String]("key")

  type Ref[T <: js.Any] = KeyAndRefAddingStage[T]

  def icon(slot: String, parentClass: Option[String], name: (String, String)) =
    CustomTag("sl-icon")(
      partAttr := "icon",
      classAttr := parentClass.getOrElse("default"),
      libraryAttr := name._1,
      nameAttr := name._2,
      slotAttr := slot,
      keyAttr := s"${name._1}-${name._2}"
    )

  def add[A](listBuffer: ListBuffer[TagMod[_]], opt: Option[A], name: String) =
    if (opt.isDefined)
      opt.get match {
        case b: Boolean =>
          if (b) listBuffer += (CustomAttribute(name) := b)
        case x =>
          listBuffer += (CustomAttribute[A](name) := x)
      }


  def add[A](listBuffer: ListBuffer[TagMod[_]], value: A, name: String) =
    value match {
      case b: Boolean =>
        if (b) listBuffer += (CustomAttribute(name) := b)
      case x =>
        listBuffer += (CustomAttribute[A](name) := x)
    }

  def handleChecked(event: Event, callback: Boolean => Unit): Unit =
    callback(event.target.asInstanceOf[js.Dynamic].checked.asInstanceOf[Boolean])

  def handleValue[T](event: Event, callback: js.Function1[T, Unit]): Unit =
    callback(event.target.asInstanceOf[js.Dynamic].value.asInstanceOf[T])

}