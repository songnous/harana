package com.harana.docs.generator.docusaurus

import io.circe.Json
import io.circe.Json.JString
import io.circe.syntax.EncoderOps
import zio.ZIO

import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Paths}

object Generator {

  def saveDocument(doc: Document) =
    ZIO.from {
      val str = s"""---
         |title: ${doc.title}
         |description: ${doc.description}
         |---
         |${doc.content}
         |""".stripMargin

      Files.write(Paths.get(s"/tmp/${doc.path.mkString("/")}"), str.getBytes(StandardCharsets.UTF_8))
    }


  def saveSidebar(items: List[Either[Document, SidebarSection]]) = {
    ZIO.from {
      val str =
        s"""
           |const sidebars = {
           |${Map("docs" -> Json.fromValues(items.map(generateSidebarItem))).asJson.noSpaces}
           |};
           |
           |module.exports = sidebars;
           |""".stripMargin

      Files.write(Paths.get(s"/tmp/sidebars.js"), str.getBytes(StandardCharsets.UTF_8))
    }
  }


  private def generateSidebarItem(item: Either[Document, SidebarSection]): Json =
    item match {
      case Left(doc) => JString(doc.name)
      case Right(section) =>
        Map(
          "type"      -> Json.fromString("category"),
          "label"     -> Json.fromString(section.title),
          "collapsed" -> Json.fromBoolean(section.collapsed),
          "items"     -> Json.fromValues(section.items.map(generateSidebarItem)),
          "link"      -> (section.link match {
                            case Doc(path) =>
                              Map(
                                "type"  -> Json.fromString("doc"),
                                "id"    -> Json.fromString(path)
                              ).asJson

                            case Index(title, description, keywords, image) =>
                              Map(
                                "type"        -> Json.fromString("generated-index"),
                                "title"       -> Json.fromString(title),
                                "description" -> Json.fromString(description.getOrElse("")),
                                "keywords"    -> Json.fromValues(keywords.map(Json.fromString)),
                                "image"       -> image.map(Json.fromString).getOrElse(Json.Null)
                              ).asJson
                          }).asJson
        ).asJson
    }
}