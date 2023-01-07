package com.harana.s3.utils

import com.harana.s3.services.server.models.{S3ErrorCode, S3Exception}
import org.joda.time.{DateTime, DateTimeZone}
import org.joda.time.format.DateTimeFormat

import java.text.{ParseException, SimpleDateFormat}
import java.util.regex.Pattern
import java.util.{Date, Locale, TimeZone}

object DateTime {

  val maximumTimeSkew = 15 * 60

  private val iso8601DateFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ").withLocale(Locale.US).withZone(DateTimeZone.forID("GMT"))

  private val TZ_REGEX = "([+-][0-9][0-9](:?[0-9][0-9])?|Z)"
  private val SECOND_PATTERN = Pattern.compile(".*[0-2][0-9]:00")
  private val TZ_PATTERN = Pattern.compile("(.*)" + TZ_REGEX + "$")

  def trimTZ(toParse: String) = {
    var updatedParse = toParse
    val matcher = TZ_PATTERN.matcher(toParse)
    if (matcher.find) updatedParse = matcher.group(1)
    if (toParse.length == 25 && SECOND_PATTERN.matcher(toParse).matches) updatedParse = updatedParse.substring(0, updatedParse.length - 6)
    updatedParse
  }

  def findTZ(toParse: String): String = {
    val matcher = TZ_PATTERN.matcher(toParse)
    if (matcher.find) {
      // Remove ':' from the TZ string, as SimpleDateFormat can't handle it
      var tz = matcher.group(2).replace(":", "")
      // Append '00; if we only have a two digit TZ, as SimpleDateFormat
      if (tz.length == 2) tz += "00"
      // Replace Z with +0000
      if (tz == "Z") return "+0000"
      tz
    }
    else {
      // Return +0000 if no time zone
      "+0000"
    }
  }

  def iso8601DateFormat(date: Date) = {
    var parsed = iso8601DateFormatter.print(new DateTime(date))
    val tz = findTZ(parsed)
    if (tz == "+0000") parsed = trimTZ(parsed) + "Z"
    parsed
  }

  def parseIso8601(date: String) = {
    val formatter = new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'")
    formatter.setTimeZone(TimeZone.getTimeZone("UTC"))

    try formatter.parse(date).getTime / 1000
    catch {
      case pe: ParseException => throw new IllegalArgumentException(pe)
    }
  }

  def isTimeSkewed(date: Long) = {
    if (date < 0) throw new S3Exception(S3ErrorCode.ACCESS_DENIED)
    val now = System.currentTimeMillis / 1000
    if (now + maximumTimeSkew < date || now - maximumTimeSkew > date) {
      throw new S3Exception(S3ErrorCode.REQUEST_TIME_TOO_SKEWED)
    }
  }

  def formatDate(date: Long) = {
    val formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
    formatter.setTimeZone(TimeZone.getTimeZone("GMT"))
    formatter.format(date)
  }

}
