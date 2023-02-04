package com.harana.s3.utils

import com.google.common.hash.Hashing
import com.google.common.io.{BaseEncoding, Files}
import zio.{Task, UIO, ZIO}

import java.nio.file.Path

object AwsFile {

  def eTag(path: List[Path]): Task[String] =
    for {
      md5s        <- ZIO.foreachPar(path)(p => {
                      Task(Files.asByteSource(p.toFile).hash(Hashing.md5()).toString)
                     })
      rawBytes    <- Task(BaseEncoding.base16().decode(md5s.mkString.toUpperCase))
      hasher      <- UIO(Hashing.md5().newHasher())
      _           <- Task(hasher.putBytes(rawBytes))
      eTag        <- UIO(s"${hasher.hash.toString}-${md5s.size}")
    } yield eTag


  def md5(path: Path): String =
    Files.asByteSource(path.toFile).hash(Hashing.md5()).toString

}