package com.harana.s3.utils

import java.io.{FilterInputStream, InputStream}

class UncloseableInputStream(is: InputStream) extends FilterInputStream(is) {
  override def close() = {}
}
