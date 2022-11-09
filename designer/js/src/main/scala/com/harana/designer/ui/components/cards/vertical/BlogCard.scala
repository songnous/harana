package com.harana.ui.components.cards.vertical

import com.harana.ui.components.Url
import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.web.html._

@react class BlogCard extends StatelessComponent {

  case class Props(link: Url,
                   title: String,
                   description: String,
                   date: String,
                   thumbnail: Url,
                   authorLink: Url,
                   authorTitle: String,
                   likeLink: Url,
                   likeCount: String)

  def render() =
    div(className := "panel panel-flat")(
      div(className := "panel-body")(
        div(className := "thumb content-group")(
          img(src := props.thumbnail, className := "img-responsive"),
          div(className := "caption-overflow")(
            span(
              a(href := props.link, className := "btn btn-flat border-white text-white btn-rounded btn-icon")(
                i(className := "icon-arrow-right8")
              )
            )
          )
        ),
        h5(className := "text-semibold mb-5")(
          a(href := props.link, className := "text-default")(props.title)
        ),
        ul(className := "list-inline list-inline-separate text-muted content-group")(
          li(
            "By",
            a(href := props.authorLink, className := "text-muted")("Eugene")
          ),
          li(props.date)
        ),
        props.description
      ),
      div(className := "panel-footer panel-footer-condensed")(
        div(className := "heading-elements not-collapsible")(
          ul(className := "list-inline list-inline-separate heading-text text-muted")(
            li(
              a(href := props.likeLink, className := "text-muted")(
                i(className := "icon-heart6 text-size-base text-pink position-left")(props.likeCount)
              )
            )
          ),
          a(href := props.link, className := "heading-text pull-right")(
            "Read more",
            i(className := "icon-arrow-right14 position-right")
          )
        )
      )
    )
}
