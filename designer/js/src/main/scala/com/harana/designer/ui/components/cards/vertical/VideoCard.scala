package com.harana.ui.components.cards.vertical

import com.harana.ui.components.when
import slinky.core.annotations.react
import slinky.core.{CustomAttribute, StatelessComponent}
import slinky.web.html._

@react class VideoCard extends StatelessComponent {

  case class Props(url: String,
                   title: Option[String],
                   description: Option[String])

  def render() =
    div(className := "thumbnail")(
      div(className := "video-container")(
        iframe(src := "asdf", new CustomAttribute[String]("frameborder") := "0")
      ),
      div(className := "caption")(
        h6(className := "no-margin-top text-semibold")(
          when(props.title, p(className := "text-default")(props.title.get)),
          when(props.description,
            p(className := "text-muted")(
              i(className := "icon-download pull-right"),
              props.description.get
            )
          )
        )
      )
    )
}
