package com.harana.modules.ldap

import zio.{Has, Task}
import zio.macros.accessible

@accessible
object Ldap {
  type Ldap = Has[Ldap.Service]

  trait Service {
    def createUser(emailAddress: String, password: String): Task[Unit]
    def deleteUser(emailAddress: String): Task[Unit]

    def setPassword(emailAddress: String, password: String): Task[Unit]
  }
}