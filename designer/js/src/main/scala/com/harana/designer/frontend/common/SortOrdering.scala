package com.harana.designer.frontend.common

import enumeratum._

sealed trait SortOrdering extends EnumEntry
object SortOrdering extends Enum[SortOrdering] {
  case object NameAscending extends SortOrdering
  case object NameDescending extends SortOrdering
  case object SizeAscending extends SortOrdering
  case object SizeDescending extends SortOrdering
  case object CreatedAscending extends SortOrdering
  case object CreatedDescending extends SortOrdering
  case object UpdatedAscending extends SortOrdering
  case object UpdatedDescending extends SortOrdering
  val values = findValues
}

object CaseInsensitiveOrdering extends Ordering[String] {
  def compare(x: String, y: String): Int = x.compareToIgnoreCase(y)
}