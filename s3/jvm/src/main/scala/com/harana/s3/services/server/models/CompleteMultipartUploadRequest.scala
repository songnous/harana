package com.harana.s3.services.server.models

import com.fasterxml.jackson.dataformat.xml.annotation.{JacksonXmlElementWrapper, JacksonXmlProperty}

case class CompleteMultipartUploadRequest(@JacksonXmlProperty(localName = "Part")
                                          @JacksonXmlElementWrapper(useWrapping = false)
                                          parts: List[CompletePart])

case class CompletePart(@JacksonXmlProperty(localName = "PartNumber") partNumber: Int,
                        @JacksonXmlProperty(localName = "ETag") eTag: String)