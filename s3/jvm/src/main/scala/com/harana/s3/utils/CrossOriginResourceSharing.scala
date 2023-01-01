package com.harana.s3.utils

import com.google.common.base.{Splitter, Strings}
import com.harana.s3.utils.CrossOriginResourceSharing.{ALLOW_ANY_HEADER, ALLOW_ANY_ORIGIN, HEADER_VALUE_SEPARATOR}

import scala.jdk.CollectionConverters._
import java.util.regex.Pattern
import scala.collection.mutable
import scala.util.control.Breaks.{break, breakable}

case class CrossOriginResourceSharing(allowedMethodsRaw: Option[String] = None,
                                      allowedHeadersRaw: Option[String] = None,
                                      anyOriginAllowed: Boolean = true,
                                      allowedOrigins: Set[Pattern] = Set.empty,
                                      allowedMethods: Set[String] = Set.empty,
                                      allowedHeaders: Set[String] = Set.empty) {

  def apply(allowedOrigins: Set[String],
            allowedMethods: Set[String],
            allowedHeaders: Set[String]) = {

    var anyOriginAllowed = false
    val allowedPattern = mutable.HashSet.empty[Pattern]

    if (allowedOrigins.contains(ALLOW_ANY_ORIGIN))
      anyOriginAllowed = true
    else
      allowedOrigins.foreach(o => allowedPattern.add(Pattern.compile(o, Pattern.CASE_INSENSITIVE)))

    this.anyOriginAllowed = anyOriginAllowed
    this.allowedOrigins = ImmutableSet.copyOf(allowedPattern)

    if (allowedMethods == null) this.allowedMethods = ImmutableSet.of
    else this.allowedMethods = ImmutableSet.copyOf(allowedMethods)
    this.allowedMethodsRaw = Joiner.on(HEADER_VALUE_SEPARATOR).join(this.allowedMethods)

    if (allowedHeaders == null) this.allowedHeaders = ImmutableSet.of
    else this.allowedHeaders = ImmutableSet.copyOf(allowedHeaders)
    this.allowedHeadersRaw = Joiner.on(HEADER_VALUE_SEPARATOR).join(this.allowedHeaders)

  }

  def isOriginAllowed(origin: String): Boolean =
    if (anyOriginAllowed)
      true
    else {
      for (pattern <- allowedOrigins) {
        if (pattern.matcher(origin).matches)
          return true
      }
      false
    }

  def isMethodAllowed(method: String): Boolean =
    allowedMethods.contains(method)

  def isEveryHeaderAllowed(headers: String) = {
    var result = false
    if (!Strings.isNullOrEmpty(headers))
      if (this.allowedHeadersRaw.equals(ALLOW_ANY_HEADER))
        result = true
      else {
        breakable {
          for (header <- Splitter.on(HEADER_VALUE_SEPARATOR).split(headers).asScala) {
            result = this.allowedHeaders.contains(header)
            if (!result) {
              // First not matching header breaks
              break
            }
          }
        }
      }
    result
  }

  override def equals(obj: Any): Boolean = {
    if (this == obj)  return true
    if (obj == null || !obj.isInstanceOf[CrossOriginResourceSharing]) return false

    val that = obj.asInstanceOf[CrossOriginResourceSharing]
    this.allowedOrigins.equals(that.allowedOrigins) &&
      this.allowedMethodsRaw.equals(that.allowedMethodsRaw) &&
      this.allowedHeadersRaw.equals(that.allowedHeadersRaw)
  }
}

object CrossOriginResourceSharing {
  val SUPPORTED_METHODS = List("GET", "HEAD", "PUT", "POST")
  val HEADER_VALUE_SEPARATOR = ", "
  val ALLOW_ANY_ORIGIN = "*"
  val ALLOW_ANY_HEADER = "*"
}