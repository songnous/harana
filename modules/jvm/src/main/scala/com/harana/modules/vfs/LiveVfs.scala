package com.harana.modules.vfs

import java.io.{File, InputStream, OutputStream}
import java.time.Instant

import com.github.vfss3.S3FileProvider
import com.harana.modules.vfs.Vfs.Service
import com.harana.shared.models.HaranaFile
import net.lingala.zip4j.ZipFile
import org.apache.commons.io.IOUtils
import org.apache.commons.lang3.StringUtils
import org.apache.commons.vfs2.{AllFileSelector, FileObject, FileUtil, Selectors}
import org.apache.commons.vfs2.impl.DefaultFileSystemManager
import org.apache.commons.vfs2.provider.bzip2.Bzip2FileProvider
import org.apache.commons.vfs2.provider.ftp.FtpFileProvider
import org.apache.commons.vfs2.provider.ftps.FtpsFileProvider
import org.apache.commons.vfs2.provider.gzip.GzipFileProvider
import org.apache.commons.vfs2.provider.hdfs.HdfsFileProvider
import org.apache.commons.vfs2.provider.http.HttpFileProvider
import org.apache.commons.vfs2.provider.https.HttpsFileProvider
import org.apache.commons.vfs2.provider.jar.JarFileProvider
import org.apache.commons.vfs2.provider.local.DefaultLocalFileProvider
import org.apache.commons.vfs2.provider.ram.RamFileProvider
import org.apache.commons.vfs2.provider.res.ResourceFileProvider
import org.apache.commons.vfs2.provider.sftp.SftpFileProvider
import org.apache.commons.vfs2.provider.tar.TarFileProvider
import org.apache.commons.vfs2.provider.temp.TemporaryFileProvider
import org.apache.commons.vfs2.provider.url.UrlFileProvider
//import org.apache.commons.vfs2.provider.webdav.WebdavFileProvider
import org.apache.commons.vfs2.provider.zip.ZipFileProvider
import zio.{Task, ZLayer}


object LiveVfs {
  val layer = ZLayer.succeed(new Service {

    private val sourceManager = {
      val fsm = new DefaultFileSystemManager()
      fsm.addProvider("ftp", new FtpFileProvider)
      fsm.addProvider("ftps", new FtpsFileProvider)
      fsm.addProvider("hdfs", new HdfsFileProvider)
      fsm.addProvider("http", new HttpFileProvider)
      fsm.addProvider("https", new HttpsFileProvider)
      fsm.addProvider("local", new DefaultLocalFileProvider)
      fsm.addProvider("sftp", new SftpFileProvider)
      fsm.addProvider("s3", new S3FileProvider)
//      fsm.addProvider("webdav", new WebdavFileProvider)
      fsm.addProvider("bzip2", new Bzip2FileProvider)
      fsm.addProvider("gzip", new GzipFileProvider)
      fsm.addProvider("jar", new JarFileProvider)
      fsm.addProvider("ram", new RamFileProvider)
      fsm.addProvider("res", new ResourceFileProvider)
      fsm.addProvider("tar", new TarFileProvider)
      fsm.addProvider("tmp", new TemporaryFileProvider)
      fsm.addProvider("url", new UrlFileProvider)
      fsm.addProvider("zip", new ZipFileProvider)
      fsm.init()
      fsm
    }

    private val all = new AllFileSelector()


    def mkdir(uri: String): Task[Unit] =
      Task(file(uri).createFolder())


    def read(uri: String): Task[InputStream] =
      Task(file(uri).getContent.getInputStream)


    def read(uri: String, outputStream: OutputStream): Task[Unit] =
      Task {
        val content = file(uri).getContent
        IOUtils.copy(content.getInputStream, outputStream)
        content.close()
      }


    def readAsBytes(uri: String): Task[Array[Byte]] =
      Task(FileUtil.getContent(file(uri)))


    def write(uri: String, inputStream: InputStream): Task[Unit] =
      Task {
        val content = file(uri).getContent
        IOUtils.copy(inputStream, content.getOutputStream)
        content.close()
      }


    def copy(fromUri: String, toUri: String): Task[Unit] =
      Task(file(toUri).copyFrom(file(fromUri), Selectors.SELECT_ALL))


    def move(fromUri: String, toUri: String): Task[Unit] =
      Task(file(fromUri).moveTo(file(toUri)))


    def duplicate(uri: String): Task[Unit] =
      Task(file(duplicateName(file(uri))).copyFrom(file(uri), Selectors.SELECT_ALL))


    def delete(uri: String): Task[Unit] =
      Task(if (file(uri).exists()) file(uri).delete())


    def exists(uri: String): Task[Boolean] =
      Task(file(uri).exists())


    def info(uri: String): Task[HaranaFile] =
      Task(toDataFile(file(uri)))


    def list(uri: String): Task[List[HaranaFile]] =
      Task(file(uri).getChildren.toList.map(toDataFile))


    def search(uri: String, query: String): Task[List[HaranaFile]] = {
      val lowercaseQuery = query.toLowerCase
      Task(file(uri).findFiles(all).toList.filter(_.getName.getBaseName.toLowerCase.contains(lowercaseQuery)).map(toDataFile))
    }

    def underlyingFile(uri: String): Task[File] =
      Task(new File(file(uri).getName.getPath))


    def size(uri: String): Task[Long] =
      Task(calculateSize(file(uri)))


    def decompress(uri: String): Task[Unit] =
      Task {
        val outputDir = file(decompressName(file(uri)))
        outputDir.createFolder()
        new ZipFile(file(uri).getName.getPath).extractAll(outputDir.getName.getPath)
      }


    def compress(uri: String): Task[Unit] =
      Task {
        val inputFile = new File(file(uri).getName.getPath)
        val outputFile = new File(file(compressName(file(uri), "zip")).getName.getPath)
        if (file(uri).isFile) new ZipFile(outputFile).addFile(inputFile) else new ZipFile(outputFile).addFolder(inputFile)
      }


    def rename(uri: String, newName: String): Task[Unit] = {
      val path = s"${file(uri).getParent}/$newName"
      move(uri, s"${file(uri).getParent}/$newName").unless(file(path).exists())
    }


    private def file(uri: String) = sourceManager.resolveFile(uri)

  })
}