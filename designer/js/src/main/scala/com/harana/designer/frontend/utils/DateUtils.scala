package com.harana.designer.frontend.utils

import java.time.Instant
import java.time.LocalDate
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatter
import java.time.{Duration, Instant, ZoneId}


object DateUtils {

  private val dateFormatter = DateTimeFormatter.ofPattern("d LLL YYYY", java.util.Locale.ENGLISH).withZone(ZoneId.of("Australia/Sydney"))
  private val dateTimeFormatter = DateTimeFormatter.ofPattern("d LLL YYYY / hh:mm:ss a", java.util.Locale.ENGLISH).withZone(ZoneId.of("Australia/Sydney"))

  def format(instant: Instant, includeTime: Boolean = false): String =
    (if (includeTime) dateTimeFormatter.format(instant) else dateFormatter.format(instant)).replace("AM", "am").replace("PM", "pm")


  def pretty(duration: Duration): String = {
    if (duration.toDays > 0) return s"${duration.toDays} days"
    if (duration.toHours > 0) return s"${duration.toHours} hours"
    if (duration.toMinutes > 0) return s"${duration.toMinutes} mins"
    if (duration.getSeconds > 0) return s"${duration.getSeconds} s"
    if (duration.toMillis > 0) return s"${duration.toMillis} ms"
    ""
  }


  def pretty(instant1: Instant, instant2: Instant): String = {
    pretty(Duration.between(instant1, instant2))
  }


//  def pretty(instant: Instant): String = {
//      // Compare the date-only value of incoming date-time to date-only of today and yesterday.
//      val localDateIncoming = zdt.toLocalDate
//      val instant = Instant.now
//      val now = ZonedDateTime.now(zdt.getZone) // Get current date-time in same zone as incoming ZonedDateTime.
//      val localDateToday = now.toLocalDate
//      val localDateYesterday = localDateToday.minusDays(1)
//      var formatter = null
//      if (localDateIncoming.isEqual(localDateToday)) formatter = DateTimeFormatter.ofPattern("'Today' hh:mma", locale) // FIXME: Localize "Today".
//      else if (localDateIncoming.isEqual(localDateYesterday)) formatter = DateTimeFormatter.ofPattern("'Yesterday' hh:mma", locale) // FIXME: Localize "Yesterday".
//      else formatter = DateTimeFormatter.ofPattern("EEE hh:mma MMM d, uuuu", locale)
//      val output = zdt.format(formatter)
//      output // FIXME: Check for null.
//
//    }
//  }
}