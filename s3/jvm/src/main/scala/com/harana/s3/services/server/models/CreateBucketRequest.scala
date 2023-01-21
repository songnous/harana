package com.harana.s3.services.server.models

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

case class CreateBucketRequest(@JacksonXmlProperty(localName = "LocationConstraint") locationConstraint: String)