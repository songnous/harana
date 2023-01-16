package com.harana.s3.services.server.models

import com.fasterxml.jackson.dataformat.xml.annotation.{JacksonXmlElementWrapper, JacksonXmlProperty}

case class DeleteMultipleObjectsRequest(@JacksonXmlProperty(localName = "Quiet") quiet: Boolean,
                                        @JacksonXmlProperty(localName = "Object") @JacksonXmlElementWrapper(useWrapping = false) objects: List[DeleteS3Object])

case class DeleteS3Object(@JacksonXmlProperty(localName = "Key") key: String,
                          @JacksonXmlProperty(localName = "VersionID") versionId: String)