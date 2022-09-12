package com.harana.ui.components.panels

import slinky.core.StatelessComponent
import slinky.core.annotations.react
import slinky.web.html._

@react class LoginPanel extends StatelessComponent {

  case class Props(id: String)

  def render() =
    div(id := props.id, className := "modal fade", tabIndex := -1)(
      div(className := "modal-dialog")(
        div(className := "modal-content login-form width-400")(
          form(action := "index.html")(
            ul(className := "nav nav-tabs nav-justified")(
              li(className := "active")(
                a(href := s"$id-tab1", data-"toggle" := "tab")(
                  h6(className := "text-semibold")(
                    i(className := "icon-checkmark3 position-left"), "Login"
                  )
                )
              ),
              li(
                a(href := "#basic-tab2", data-"toggle" := "tab")(
                  h6(className := "text-semibold")(
                    i(className := "icon-plus3 position-left"), "Register"
                  )
                )
              )
            ),
            div(className := "tab-content modal-body")(
              div(className := "tab-pane fade in active", id := s"$id-tab1")(
                div(className := "text-center")(
                  div(className := "icon-object border-slate-300 text-slate-300")(
                    i(className := "icon-reading")
                  ),
                  h5(className := "content-group")(
                    "Login to your account", small(className := "display-block")("Your credentials")
                  )
                ),
                div(className := "form-group has-feedback has-feedback-left")(
                  input(`type` := "text", className := "form-control", placeholder := "Email", name := "email", required := true),
                  div(className := "form-control-feedback")(
                    i(className := "icon-user text-muted")
                  )
                ),
                div(className := "form-group has-feedback has-feedback-left")(
                  input(`type` := "password", className := "form-control", placeholder := "Password", name := "password", required := true),
                  div(className := "form-control-feedback")(
                    i(className := "icon-lock2 text-muted")
                  )
                ),
                div(className := "form-group login-options")(
                  div(className := "row")(
                    div(className := "col-sm-6")(
                      label(className := "checkbox-inline")(
                        input(`type` := "checkbox", className := "styled", checked := true), "Remember"
                      )
                    ),
                    div(className := "col-sm-6")(
                      a(href := "recover-password.html")("Forgot password ?")
                    )
                  )
                ),
                div(className := "form-group")(
                  button(`type`:= "submit", className := "btn bg-pink-400 btn-block")("Login", i(className := "icon-arrow-right14 position-right"))
                ),
                div(className := "content-divider text-muted form-group")(span("or sign in with")),
                ul(className := "list-inline form-group list-inline-condensed text-center")(
                  li(a(href := "#", className := "btn border-indigo text-indigo btn-flat btn-icon btn-rounded")(i(className := "icon-facebook"))),
                  li(a(href := "#", className := "btn border-pink-300 text-pink-300 btn-flat btn-icon btn-rounded")(i(className := "icon-dribbble3"))),
                  li(a(href := "#", className := "btn border-slate-600 text-slate-600 btn-flat btn-icon btn-rounded")(i(className := "icon-github"))),
                  li(a(href := "#", className := "btn border-info text-info btn-flat btn-icon btn-rounded")(i(className := "icon-twitter")))
                ),
                span(className := "help-block text-center no-margin")("By continuing, you're confirming that you've read our", a(href := "#")("Terms & Conditions"), " and ", a(href := "#")("Cookie Policy"))
              ),
              div(className := "tab-pane fade", id := s"$id-tab2")(
                div(className := "text-center")(
                  div(className := "icon-object border-success text-success")(i(className := "icon-plus3")),
                  h5(className := "content-group")("Create new account", small(className := "display-block")("All fields are required"))
                )
              ),
              div(className := "form-group has-feedback has-feedback-left")(
                input(`type` := "text", className := "form-control", placeholder := "Email")
              ),
              div(className := "form-group has-feedback has-feedback-left")(
                input(`type` := "text", className := "form-control", placeholder := "Password")
              ),
              div(className := "content-divider text-muted form-group")(span("Additions")),
              div(className := "form-group")(
                div(className := "checkbox")(
                  label(
                    "Subscribe to monthly newsletter",
                    input(`type` := "checkbox", className := "styled", checked := true)
                  )
                ),
                div(className := "checkbox")(
                  label(
                    "Accept terms of service",
                    input(`type` := "checkbox", className := "styled")
                  )
                )
              ),
              button(`type` := "submit", className := "btn bg-indigo-400 btn-block")("Register ", i(className := "icon-circle-right2 position-right")),
              button(`type` := "submit", className := "bbtn btn-default btn-block")("Cancel")
            )
          )
        )
      )
    )
}