package com.harana.id.utils

import com.desmondyeung.hashing.XxHash64

object HashUtils {

  def stringToLong(s: String): Long = {
    if (s == null) return 0
    var hash = 0L
    for (c <- s.toCharArray) hash = 31L * hash + c
    hash
  }

  def hash(phrase: String, seed: String): Long =
    XxHash64.hashByteArray(phrase.getBytes, stringToLong(seed))
}
