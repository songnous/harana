package java.security

import org.scalajs.dom

import java.util.Random
import org.scalajs.dom.crypto.GlobalCrypto.crypto

import scala.scalajs.js.typedarray._

// a simple wrapper around Crypto.getRandomValues() and java.util.Random
class SecureRandom() extends Random {

  // do nothing!
  def this(seed: Array[Byte]) = this()

  // do nothing!
  def setSeed(seed: Array[Byte]): Unit = {

  }

  override protected def next(bits: Int): Int = {
    val ar = Array(0).toTypedArray
    dom.crypto.getRandomValues(ar)
    ar(0) >>> (32 - bits)
  }

  override def nextBytes(bytes: Array[Byte]): Unit = {
    val typed = bytes.toTypedArray
    dom.crypto.getRandomValues(typed)
    var i = 0
    while (i < bytes.length) {
      bytes(i) = typed(i)
      i += 1
    }
  }

  def nextBytes(bytes: Array[Byte], params: SecureRandomParameters): Unit =
    nextBytes(bytes)

  def getProvider: Provider = null

  def getAlgorithm: String = "Crypto.getRandomValues()"

  override def toString = s"SecureRandom(Crypto.getRandomValues())"

  def getParameters: SecureRandomParameters = null

  def generateSeed(numBytes: Int): Array[Byte] = {
    if (numBytes < 0) {
      throw new IllegalArgumentException("numBytes cannot be negative")
    }
    val r = Array.fill(numBytes)(0.asInstanceOf[Byte])
    nextBytes(r)
    r
  }

  // do nothing!
  def reseed(): Unit = {

  }

  // do nothing!
  def reseed(params: SecureRandomParameters): Unit = {

  }
}

object SecureRandom {

  def getInstance(algorithm: String): SecureRandom =
    new SecureRandom()

  def getInstance(algorithm: String, provider: String): SecureRandom =
    new SecureRandom()

  def getInstance(algorithm: String, provider: Provider): SecureRandom =
    new SecureRandom()

  def getInstance(algorithm: String, params: SecureRandomParameters): SecureRandom =
    new SecureRandom()

  def getInstance(algorithm: String, params: SecureRandomParameters, provider: String): SecureRandom =
    new SecureRandom()

  def getInstance(algorithm: String, params: SecureRandomParameters, provider: Provider): SecureRandom =
    new SecureRandom()

  // MDM says that it is cryptographically strong random values, trust it!
  def getInstanceStrong: SecureRandom =
    new SecureRandom()
}