package com.harana.modules.git

import java.io.File

import com.harana.modules.git.Git.Service
import com.harana.modules.core.logger.Logger
import org.eclipse.jgit.api.{CreateBranchCommand, Git => JGit}
import org.eclipse.jgit.lib.{ObjectId, Ref}
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider
import org.eclipse.jgit.treewalk.TreeWalk
import zio.{Task, UIO, ZLayer}

import scala.collection.mutable

object LiveGit {
  val layer = ZLayer.fromService { (logger: Logger.Service) => new Service {

    def clone(uri: String,
              localDirectory: File,
              branch: Option[String] = None,
              username: Option[String] = None,
              password: Option[String] = None,
              oauthToken: Option[String] = None): Task[JGit] =
      for {
        git <- UIO {
          val cloneCommand = JGit.cloneRepository().setDirectory(localDirectory).setURI(uri)
          if (branch.nonEmpty) cloneCommand.setBranch(branch.get)
          if (username.nonEmpty && password.nonEmpty) cloneCommand.setCredentialsProvider(new UsernamePasswordCredentialsProvider(username.get, password.get))
          if (oauthToken.nonEmpty) cloneCommand.setCredentialsProvider(new UsernamePasswordCredentialsProvider(oauthToken.get, ""))
          cloneCommand.call()
        }
      } yield git

    def checkout(git: JGit, branchTagOrCommit: String): Task[Ref] =
      for {
        ref <- Task(git.checkout().setName(branchTagOrCommit).call())
      } yield ref

    def branch(git: JGit, branch: String, track: Boolean = true): Task[Ref] =
      Task {
        git
          .checkout
          .setCreateBranch(true)
          .setName(branch)
          .setUpstreamMode(CreateBranchCommand.SetupUpstreamMode.TRACK)
          .setStartPoint("origin/" + branch)
          .call()
      }

    def refresh(git: JGit): Task[Unit] =
      for {
        _ <- Task(git.pull().call())
      } yield ()

    def hasChanged(git: JGit): Task[Boolean] =
      for {
        prevCommit  <- mostRecentCommitHash(git)
        _           <- refresh(git)
        newCommit   <- mostRecentCommitHash(git)
        changed     =  prevCommit.isEmpty && newCommit.nonEmpty || prevCommit.nonEmpty && newCommit.nonEmpty && prevCommit != newCommit
      } yield changed

    def mostRecentCommitHash(git: JGit): Task[Option[String]] =
      for {
        it    <- Task(git.log.setMaxCount(1).call.iterator()).option
        hash  = if (it.nonEmpty && it.get.hasNext) Some(it.get.next().getName) else None
      } yield hash

    def filesForCommit(git: JGit, hash: String): Task[List[File]] = {
      Task {
        val treeWalk = new TreeWalk(git.getRepository)
        treeWalk.reset(ObjectId.fromString(hash))

        val paths = mutable.ListBuffer[File]()

        while (treeWalk.next) paths += new File(treeWalk.getPathString)
        if (treeWalk != null) treeWalk.close()

        paths.toList
      }
    }

    def latestFiles(git: JGit): Task[List[File]] =
      for {
        hash    <- mostRecentCommitHash(git)
        files   <- Task.ifM(UIO(hash.nonEmpty))(filesForCommit(git, hash.get), Task(List()))
        _       <- logger.debug(s"Latest files end")
      } yield files
  }}
}