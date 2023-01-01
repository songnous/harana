package com.harana.s3.services.models

import com.fasterxml.jackson.dataformat.xml.annotation.{JacksonXmlElementWrapper, JacksonXmlProperty}

case class DeleteMultipleObjectsRequest(@JacksonXmlProperty(localName = "Quiet") quiet: Boolean,
                                        @JacksonXmlProperty(localName = "Object") @JacksonXmlElementWrapper(useWrapping = false) objects: List[S3Object])

case class S3Object(@JacksonXmlProperty(localName = "Key") key: String,
                    @JacksonXmlProperty(localName = "VersionID") versionId: String)