package com.harana.sdk.shared.utils

import java.security.SecureRandom
import Base64._

object Random {

  private val random = new SecureRandom()

  def short = {
    val buffer = new Array[Byte](5)
    random.nextBytes(buffer)
    buffer.toBase64.replace("+", "x").replace("/", "x").replace("=", "0")
  }

  def long = {
    val buffer = new Array[Byte](15)
    random.nextBytes(buffer)
    buffer.toBase64.replace("+", "x").replace("/", "x").replace("=", "0")
  }
}