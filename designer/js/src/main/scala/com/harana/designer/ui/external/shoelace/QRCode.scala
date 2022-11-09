package com.harana.ui.external.shoelace

import slinky.core.annotations.react
import slinky.core.facade.ReactElement
import slinky.core.{CustomTag, StatelessComponent, TagMod}

import scala.collection.mutable.ListBuffer
import scala.scalajs.js

@react class QRCode extends StatelessComponent {

  case class Props(background: Option[String] = None,
                   errorCorrection: Option[String] = None,
                   fill: Option[String] = None,
                   label: Option[String] = None,
                   lang: Option[String] = None,
                   radius: Option[String] = None,
                   size: Option[Int] = None,
                   value: Option[Int] = None)

  def render() = {
    val attrs = new ListBuffer[TagMod[_]]()
    add(attrs, props.background, "background")
    add(attrs, props.errorCorrection, "error-correction")
    add(attrs, props.fill, "fill")
    add(attrs, props.label, "label")
    add(attrs, props.lang, "lang")
    add(attrs, props.radius, "radius")
    add(attrs, props.size, "size")
    add(attrs, props.value, "value")

    val children = new ListBuffer[ReactElement]()
    if (props.label.isDefined) children += props.label.get

    CustomTag("sl-qr-code")(attrs.toSeq: _*)(children.toSeq: _*)
  }
}