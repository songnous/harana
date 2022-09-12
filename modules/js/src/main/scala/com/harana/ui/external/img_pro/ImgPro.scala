package com.harana.ui.external.img_pro

import slinky.core.ExternalComponent
import slinky.core.annotations.react

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("react-imgpro", JSImport.Default)
@js.native
object ReactImgPro extends js.Object {
  def onProcessFinish(): Unit = js.native
}

@react object ImgPro extends ExternalComponent {

  case class Props(blur: Option[Double] = None,
                   brightness: Option[Double] = None,
                   colors: Option[Colors] = None,
                   contain: Option[Size] = None,
                   contrast: Option[Double] = None,
                   cover: Option[Size] = None,
                   customCdn: Option[String] = None,
                   disableRerender: Option[Boolean] = None,
                   disableWebWorker: Option[Boolean] = None,
                   dither565: Option[Boolean] = None,
                   fade: Option[Double] = None,
                   flip: Option[Orientation] = None,
                   greyscale: Option[Boolean] = None,
                   image: String,
                   invert: Option[Boolean] = None,
                   normalize: Option[Boolean] = None,
                   opacity: Option[Double] = None,
                   opaque: Option[Boolean] = None,
                   posterize: Option[Double] = None,
                   quality: Option[Double] = None,
                   resize: Option[Shape] = None,
                   rotate: Option[Degree] = None,
                   scale: Option[Boolean] = None,
                   scaleToFitImage: Option[Size] = None,
                   sepia: Option[Boolean] = None,
                   storage: Option[Boolean] = None)

  override val component = ReactImgPro
}

case class Amount(amount: Option[Double], color: Option[String])

case class Colors(blue: Option[Double],
                  brighten: Option[Double],
                  darken: Option[Double],
                  desaturate: Option[Double],
                  green: Option[Double],
                  greyscale: Option[Double],
                  lighten: Option[Double],
                  mix: Option[Amount],
                  red: Option[Double],
                  saturate: Option[Double],
                  shade: Option[Double],
                  spin: Option[Double],
                  tint: Option[Double],
                  xor: Option[Double])

case class Contain(height: Double, mode: String, width: Double)
case class Degree(degree: Double, mode: String)
case class Orientation(horizontal: Option[Boolean], vertical: Option[Boolean])
case class Shape(height: Option[Double], mode: Option[String], width: Option[Double])
case class Size(height: Double, width: Double)