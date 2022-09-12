package com.harana.modules.git

import java.io.File

import org.eclipse.jgit.api.{ Git => JGit }
import org.eclipse.jgit.lib.Ref
import zio.macros.accessible
import zio.{Has, Task}

@accessible
object Git {
  type Git = Has[Git.Service]

  trait Service {
    def clone(uri: String,
              localDirectory: File,
              branch: Option[String] = None,
              username: Option[String] = None,
              password: Option[String] = None,
              oauthToken: Option[String] = None): Task[JGit]

    def checkout(git: JGit, branchTagOrCommit: String): Task[Ref]

    def branch(git: JGit,
               branch: String,
               track: Boolean = true): Task[Ref]

    def refresh(git: JGit): Task[Unit]

    def hasChanged(git: JGit): Task[Boolean]

    def mostRecentCommitHash(git: JGit): Task[Option[String]]

    def filesForCommit(git: JGit, hash: String): Task[List[File]]

    def latestFiles(git: JGit): Task[List[File]]
  }
}