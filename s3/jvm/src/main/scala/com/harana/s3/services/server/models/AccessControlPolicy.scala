package com.harana.s3.services.server.models

import com.fasterxml.jackson.dataformat.xml.annotation.{JacksonXmlElementWrapper, JacksonXmlProperty}

case class AccessControlPolicy(
    @JacksonXmlProperty(localName = "Owner") owner: Owner,
    @JacksonXmlProperty(localName = "AccessControlList") aclList: AccessControlList
)

case class Owner(
  @JacksonXmlProperty(localName = "ID") id: String,
  @JacksonXmlProperty(localName = "DisplayName") displayName: String
)

case class AccessControlList(
  @JacksonXmlProperty(localName = "Grant") @JacksonXmlElementWrapper(useWrapping = false) grants: List[Grant]
)

case class Grant(
    @JacksonXmlProperty(localName = "Grantee") grantee: Grantee,
    @JacksonXmlProperty(localName = "Permission") permission: String
)

case class Grantee(
    @JacksonXmlProperty(namespace = "xsi", localName = "type", isAttribute = true) `type`: String,
    @JacksonXmlProperty(localName = "ID") id: String,
    @JacksonXmlProperty(localName = "DisplayName") displayName: String,
    @JacksonXmlProperty(localName = "EmailAddress") emailAddress: String,
    @JacksonXmlProperty(localName = "URI") uri: String
)