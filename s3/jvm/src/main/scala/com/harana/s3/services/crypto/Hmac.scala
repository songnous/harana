package com.harana.s3.services.crypto

import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

object Hmac {

  def encode(algorithm: String, data: Array[Byte], key: Array[Byte]) =
    try {
      val mac = Mac.getInstance(algorithm)
      mac.init(new SecretKeySpec(key, algorithm))
      mac.doFinal(data)
    } catch {
      case e: Exception => throw new RuntimeException(e)
    }
}
