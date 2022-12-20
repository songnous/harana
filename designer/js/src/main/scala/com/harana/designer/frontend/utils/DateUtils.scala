package com.harana.designer.frontend.utils

import java.time.format.DateTimeFormatter
import java.time.{Duration, Instant, LocalDateTime, ZoneOffset}

object DateUtils {

  val dateTimeFormatter = DateTimeFormatter.ofPattern("dd MMM YYYY / hh:mm:ss a")

  def format(instant: Instant, includeTime: Boolean = false): String = {
    val date = LocalDateTime.ofInstant(instant, ZoneOffset.UTC)
    (if (includeTime) dateTimeFormatter.format(date) else dateTimeFormatter.format(date)).replace("AM", "am").replace("PM", "pm")
  }


  def pretty(duration: Duration): String = {
    if (duration.toDays > 0) return s"${duration.toDays} day${if (duration.toDays > 1) "s" else ""}"
    if (duration.toHours > 0) return s"${duration.toHours} hour${if (duration.toHours > 1) "s" else ""}"
    if (duration.toMinutes > 0) return s"${duration.toMinutes} min${if (duration.toMinutes > 1) "s" else ""}"
    if (duration.getSeconds > 0) return s"${duration.getSeconds} sec"
    ""
  }


  def pretty(instant1: Instant, instant2: Instant): String = {
    pretty(Duration.between(instant1, instant2))
  }

}