package com.harana.modules.ldap

import com.harana.modules.core.config.Config
import com.harana.modules.ldap.Ldap.Service
import com.harana.modules.core.logger.Logger
import com.unboundid.ldap.sdk._
import zio.{Task, UIO, ZLayer}

object LiveLdap {
  val layer = ZLayer.fromServices { (config: Config.Service, logger: Logger.Service) => new Service {

    def createUser(emailAddress: String, password: String): Task[Unit] =
      for {
        connection        <- getConnection
        entry             =  new Entry(dn(emailAddress)) { addAttribute("userPassword", password) }
        result            <- Task.effectAsync[Unit] { cb =>
                                connection.asyncAdd(new AddRequest(entry), new AsyncResultListener {
                                  override def ldapResultReceived(requestID: AsyncRequestID, ldapResult: LDAPResult): Unit = {
                                    if (ldapResult.getResultCode.equals(ResultCode.SUCCESS)) cb(Task(Unit))
                                    else cb(Task.fail(new Exception(ldapResult.getDiagnosticMessage)))
                                  }
                                })
        }
      } yield result


    def deleteUser(emailAddress: String): Task[Unit] =
      for {
        connection        <- getConnection
        result            <- Task.effectAsync[Unit] { cb =>
                                connection.asyncDelete(new DeleteRequest(dn(emailAddress)), new AsyncResultListener {
                                  override def ldapResultReceived(requestID: AsyncRequestID, ldapResult: LDAPResult): Unit = {
                                    if (ldapResult.getResultCode.equals(ResultCode.SUCCESS)) cb(Task(Unit))
                                    else cb(Task.fail(new Exception(ldapResult.getDiagnosticMessage)))
                                  }
                                })
                              }
      } yield result


    def setPassword(emailAddress: String, password: String): Task[Unit] =
      for {
        connection        <- getConnection
        _                 <- bind(connection)
        modifyRequest     =  new ModifyRequest(dn(emailAddress), new Modification(ModificationType.REPLACE, "userPassword", password))
        result            <- Task.effectAsync[Unit] { cb =>
                              connection.asyncModify(modifyRequest, new AsyncResultListener {
                                override def ldapResultReceived(requestID: AsyncRequestID, ldapResult: LDAPResult): Unit = {
                                  if (ldapResult.getResultCode.equals(ResultCode.SUCCESS)) cb(Task(Unit))
                                  else cb(Task.fail(new Exception(ldapResult.getDiagnosticMessage)))
                                }
                              })
                            }
      } yield ()


    private def bind(connection: LDAPConnection): Task[Unit] =
      for {
        bindUsername      <- config.secret("ldap-bind-username")
        bindPassword      <- config.secret("ldap-bind-password")
        bindTimeout       <- config.long("auth.ldap.bindTimeout")
        bindRequest       <- UIO {
                              val br = new SimpleBindRequest(s"cn=$bindUsername", bindPassword)
                              br.setResponseTimeoutMillis(bindTimeout)
                              br
                            }
        _                 <- Task(connection.bind(bindRequest))
      } yield ()


    private def getConnection: Task[LDAPConnection] =
      for {
        host              <- config.secret("ldap-bind-host")
        port              <- config.int("auth.ldap.port")
        connectTimeout    <- config.int("auth.ldap.connectTimeout")
        connection        =  new LDAPConnection()
        _                 <- Task(connection.connect(host, port, connectTimeout))
      } yield connection


    private def dn(username: String) =
      s"uid=$username,dc=harana,dc=com"
 }}
}