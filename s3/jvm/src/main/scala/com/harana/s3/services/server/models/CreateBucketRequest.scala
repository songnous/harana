package com.harana.s3.services.s3_server.models

import com.fasterxml.jackson.dataformat.xml.annotation.{JacksonXmlElementWrapper, JacksonXmlProperty}

case class CreateBucketRequest(@JacksonXmlProperty(localName = "LocationConstraint")
                               locationConstraint: String)