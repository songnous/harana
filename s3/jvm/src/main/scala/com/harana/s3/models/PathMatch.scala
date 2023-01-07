package com.harana.s3.models

sealed trait PathMatch {
  def matches(full: String): Boolean
}

object PathMatch {

  case object Any extends PathMatch {
    def matches(full: String) = true
  }

  case class Exact(part: String) extends PathMatch {
    def matches(full: String) = full == part
  }

  case class StartsWith(part: String) extends PathMatch {
    def matches(full: String) = full.startsWith(part)
  }

  case class EndsWith(part: String) extends PathMatch {
    def matches(full: String) = full.endsWith(part)
  }

  case class Contains(part: String) extends PathMatch {
    def matches(full: String) = full.contains(part)
  }

  case class Regex(regex: String) extends PathMatch {
    def matches(full: String) = full.matches(regex)
  }

}