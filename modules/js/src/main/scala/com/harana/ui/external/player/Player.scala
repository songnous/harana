package com.harana.ui.external.player

import slinky.core.ExternalComponent
import slinky.core.annotations.react

import scala.scalajs.js
import scala.scalajs.js.annotation.JSImport
import scala.scalajs.js.|

@JSImport("react-player", JSImport.Default)
@js.native
object ReactPlayer extends js.Object {
  //def addCustomPlayer(player: ReactPlayer): Unit = js.native
  def canEnablePIP(url: String): Boolean = js.native
  def canPlay(url: String): Boolean = js.native
  def removeCustomPlayers(): Unit = js.native
  def getCurrentTime(): Double = js.native
  def getDuration(): Double = js.native
  def getInternalPlayer(): js.Object = js.native
  def getInternalPlayer(key: String): js.Object = js.native
  def seekTo(fraction: Double): Unit = js.native
}

@react object Player extends ExternalComponent {

  case class Props(config: Option[Config] = None,
                   controls: Option[Boolean] = None,
                   dailymotionConfig: Option[DailyMotionConfig] = None,
                   facebookConfig: Option[FacebookConfig] = None,
                   fileConfig: Option[FileConfig] = None,
                   height: Option[String | Double] = None,
                   light: Option[Boolean | String] = None,
                   loop: Option[Boolean] = None,
                   muted: Option[Boolean] = None,
                   onBuffer: Option[() => Unit] = None,
                   onDisablePIP: Option[() => Unit] = None,
                   onDuration: Option[Double => Unit] = None,
                   onEnablePIP: Option[() => Unit] = None,
                   onEnded: Option[() => Unit] = None,
                   onError: Option[js.Any => Unit] = None,
                   onPause: Option[() => Unit] = None,
                   onPlay: Option[() => Unit] = None,
                   onProgress: Option[Loaded => Unit] = None,
                   onReady: Option[() => Unit] = None,
                   onSeek: Option[Double => Unit] = None,
                   onStart: Option[() => Unit] = None,
                   pip: Option[Boolean] = None,
                   playbackRate: Option[Double] = None,
                   playing: Option[Boolean] = None,
                   playsinline: Option[Boolean] = None,
                   progressInterval: Option[Double] = None,
                   soundcloudConfig: Option[SoundCloudConfig] = None,
                   style: Option[js.Object] = None,
                   url: Option[String | List[String] | List[SourceProps]] = None,
                   vimeoConfig: Option[VimeoConfig] = None,
                   volume: Option[Double] = None,
                   width: Option[String | Double] = None,
                   wistiaConfig: Option[WistiaConfig] = None,
                   wrapper: Option[js.Any] = None,
                   youtubeConfig: Option[YouTubeConfig] = None)

  override val component = ReactPlayer
}

object Types {
  type Formatter = (Int, String, String, Int) => Unit
}

case class Config(dailymotion: Option[DailyMotionConfig],
                  facebook: Option[FacebookConfig],
                  file: Option[FileConfig],
                  mixcloud: Option[MixcloudConfig],
                  soundcloud: Option[SoundCloudConfig],
                  vimeo: Option[VimeoConfig],
                  wistia: Option[WistiaConfig],
                  youtube: Option[YouTubeConfig])

case class DailyMotionConfig(params: Option[js.Object], preload: Option[Boolean])
case class FacebookConfig(appId: String)

case class FileConfig(
  attributes: Option[js.Object],
  dashVersion: Option[String],
  forceAudio: Option[Boolean],
  forceDASH: Option[Boolean],
  forceHLS: Option[Boolean],
  forceVideo: Option[Boolean],
  hlsOptions: Option[js.Object],
  hlsVersion: Option[String],
  tracks: List[TrackProps])

case class Loaded(loaded: Double, loadedSeconds: Double, played: Double, playedSeconds: Double)
case class MixcloudConfig(options: Option[js.Object])
case class SoundCloudConfig(options: Option[js.Object])
case class SourceProps(src: String, `type`: String)
case class TrackProps(defaults: Option[Boolean], kind: String, label: String, src: String, srcLang: String)
case class VimeoConfig(playerOptions: Option[js.Object], preload: Option[Boolean])
case class WistiaConfig(options: Option[js.Object])
case class YouTubeConfig(embedOptions: Option[js.Object], playervals: Option[js.Object], preload: Option[Boolean])