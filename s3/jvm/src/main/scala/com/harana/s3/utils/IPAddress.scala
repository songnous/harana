package com.harana.s3.utils

import com.google.common.base.Splitter
import scala.jdk.CollectionConverters._

object IPAddress {

  def validate(string: String): Boolean = {
    val parts = Splitter.on('.').splitToList(string).asScala
    if (parts.size ne 4) return false
    for (part <- parts) {
      try {
        val num = part.toInt
        if (num < 0 || num > 255) return false
      } catch {
        case nfe: NumberFormatException => return false
      }
    }
    true
  }

}
