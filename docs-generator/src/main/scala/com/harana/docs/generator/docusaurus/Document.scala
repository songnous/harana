package com.harana.docs.generator.docusaurus

case class Document(name: String,
                    title: String,
                    description: String,
                    path: List[String],
                    content: String)