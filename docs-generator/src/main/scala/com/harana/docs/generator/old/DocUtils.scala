package com.harana.docs.generator.old

object DocUtils {

  def underscorize(input: String) = input.toLowerCase.replace(' ', '_')

  def forceDotAtEnd(s: String) = if (s.charAt(s.length - 1) != '.') s + "." else s

}
