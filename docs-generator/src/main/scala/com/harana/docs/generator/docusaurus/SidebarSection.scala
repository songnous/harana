package com.harana.docs.generator.docusaurus

sealed trait SidebarLink
case class Doc(path: String) extends SidebarLink
case class Index(title: String,
                 description: Option[String] = None,
                 keywords: List[String] = List(),
                 image: Option[String] = None) extends SidebarLink

case class SidebarSection(title: String,
                          link: SidebarLink,
                          collapsed: Boolean = false,
                          items: List[Either[Document, SidebarSection]])