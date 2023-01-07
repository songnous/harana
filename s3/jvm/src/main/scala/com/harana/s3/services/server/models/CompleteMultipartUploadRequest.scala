package com.harana.s3.services.server.models

import com.fasterxml.jackson.dataformat.xml.annotation.{JacksonXmlElementWrapper, JacksonXmlProperty}
import com.harana.modules.aws_s3.models.Part

case class CompleteMultipartUploadRequest(@JacksonXmlProperty(localName = "Part")
                                          @JacksonXmlElementWrapper(useWrapping = false)
                                          parts: List[Part])

case class Part(@JacksonXmlProperty(localName = "PartNumber") partNumber: Int,
                @JacksonXmlProperty(localName = "ETag") eTag: String)