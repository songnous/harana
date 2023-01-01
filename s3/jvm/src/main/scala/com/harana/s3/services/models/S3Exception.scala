package com.harana.s3.services.models

import com.google.common.collect.ImmutableMap

import java.util
import java.util.Objects.requireNonNull


final class S3Exception(error: Nothing, message: String, cause: Throwable, elements: util.Map[String, String]) extends Exception(requireNonNull(message), cause) {
  this.error = requireNonNull(error)
  this.elements = ImmutableMap.copyOf(elements)
  final private var error: Nothing = null
  final private var elements: util.Map[String, String] = null

  def this(error: Nothing) {
    this(error, error.getMessage, null.asInstanceOf[Throwable], ImmutableMap.of[String, String])
  }

  def this(error: Nothing, message: String) {
    this(error, message, null.asInstanceOf[Throwable], ImmutableMap.of[String, String])
  }

  def this(error: Nothing, cause: Throwable) {
    this(error, error.getMessage, cause, ImmutableMap.of[String, String])
  }

  def this(error: Nothing, message: String, cause: Throwable) {
    this(error, message, cause, ImmutableMap.of[String, String])
  }

  private[s3proxy] def getError = error

  private[s3proxy] def getElements = elements

  override def getMessage = {
    val builder = new lang.StringBuilder().append(super.getMessage)
    if (!elements.isEmpty) builder.append(" ").append(elements)
    builder.toString
  }
}