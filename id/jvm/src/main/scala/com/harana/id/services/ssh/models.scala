package com.harana.id.services.ssh

import io.circe.generic.JsonCodec

@JsonCodec
case class ConfigurationRequest(username: String,
                                remoteAddress: String,
                                connectionId: String)

@JsonCodec
case class Configuration(ArgsEscaped: Option[Boolean] = None,
                         AttachStderr: Option[Boolean] = None,
                         AttachStdin: Option[Boolean] = None,
                         AttachStdout: Option[Boolean] = None,
                         Cmd: List[String] = List(),
                         Domainname: Option[String] = None,
                         Entrypoint: List[String] = List(),
                         Env: List[String] = List(),
                         ExposedPorts: Map[String, String] = Map(),
                         Healthcheck: Option[Healthcheck] = None,
                         Hostname: Option[String] = None,
                         Image: Option[String] = None,
                         Labels: Map[String, String] = Map(),
                         MacAddress: Option[String] = None,
                         NetworkDisabled: Option[Boolean] = None,
                         OnBuild: List[String] = List(),
                         OpenStdin: Option[Boolean] = None,
                         Shell: List[String] = List(),
                         StdinOnce: Option[Boolean] = None,
                         StopSignal: Option[String] = None,
                         StopTimeout: Option[Int] = None,
                         Tty: Option[Boolean] = None,
                         User: Option[String] = None,
                         Volumes: Map[String, String] = Map(),
                         WorkingDir: Option[String] = None)

@JsonCodec
case class Healthcheck(Interval: Option[Int] = None,
                       Retries: Option[Int] = None,
                       StartPeriod: Option[Int] = None,
                       Test: List[String] = List(),
                       Timeout: Option[Int] = None)
@JsonCodec
case class PasswordRequest(username: String,
                           remoteAddress: String,
                           connectionId: String,
                           passwordBase64: String)

@JsonCodec
case class PublicKeyRequest(username: String,
                            remoteAddress: String,
                            connectionId: String,
                            publicKey: String)