package com.harana.ui.external.modal_video

import slinky.core.ExternalComponent
import slinky.core.annotations.react

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport

@JSImport("react-modal-video", JSImport.Default)
@js.native
object ReactModalVideo extends js.Object

@react object ModalVideo extends ExternalComponent {

  case class Props(channel: Option[String] = None,
                   isOpen: Option[Boolean] = None,
                   youtube: Option[Youtube] = None,
                   ratio: Option[String] = None,
                   vimeo: Option[Vimeo] = None,
                   youku: Option[Youku] = None,
                   allowFullScreen: Option[Boolean] = None,
                   animationSpeed: Option[Int] = None,
                   classNames: Option[ClassNames] = None,
                   aria: Option[Aria] = None)

  override val component = ReactModalVideo
}

@js.native
trait Aria extends js.Object {
  val openMessage: Option[String] = js.native
  val dismissBtnMessage: Option[String] = js.native
}

@js.native
trait ClassNames extends js.Object {
  val modalVideoEffect: Option[String] = js.native
  val modalVideoString: Option[String] = js.native
  val modalVideoClose: Option[String] = js.native
  val modalVideoBody: Option[String] = js.native
  val modalVideoInner: Option[String] = js.native
  val modalVideoIframeWrap: Option[String] = js.native
  val modalVideoCloseBtn: Option[String] = js.native
}

@js.native
trait Vimeo extends js.Object {
  val api: Option[Boolean] = js.native
  val autopause: Option[Boolean] = js.native
  val autoplay: Option[Boolean] = js.native
  val byline: Option[Boolean] = js.native
  val callback: Option[() => Unit] = js.native
  val color: Option[String] = js.native
  val height: Option[Int] = js.native
  val loop: Option[Boolean] = js.native
  val maxheight: Option[Int] = js.native
  val maxwidth: Option[Int] = js.native
  val player_id: Option[String] = js.native
  val portrait: Option[Boolean] = js.native
  val title: Option[Boolean] = js.native
  val width: Option[Int] = js.native
  val xhtml: Option[Boolean] = js.native
}

@js.native
trait Youku extends js.Object {
  val autoPlay: Option[Int] = js.native
  val show_related: Option[Int] = js.native
}

@js.native
trait Youtube extends js.Object {
  val autoplay: Option[Int] = js.native
  val cc_load_policy: Option[Int] = js.native
  val color: Option[String] = js.native
  val controls: Option[Int] = js.native
  val disablekb: Option[Int] = js.native
  val enablejsapi: Option[Int] = js.native
  val end: Option[String] = js.native
  val fs: Option[Int] = js.native
  val h1: Option[String] = js.native
  val iv_load_policy: Option[Int] = js.native
  val list: Option[String] = js.native
  val listType: Option[String] = js.native
  val loop: Option[Int] = js.native
  val modestbranding: Option[Boolean] = js.native
  val origin: Option[String] = js.native
  val playlist: Option[String] = js.native
  val playsinline: Option[Boolean] = js.native
  val rel: Option[Int] = js.native
  val showinfo: Option[Int] = js.native
  val start: Option[Int] = js.native
  val wmode: Option[String] = js.native
  val theme: Option[String] = js.native
}