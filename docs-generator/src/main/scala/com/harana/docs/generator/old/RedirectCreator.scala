package com.harana.docs.generator.old

import java.io.{File, PrintWriter}

trait RedirectCreator {

  /** @return number of redirects created */
  def createRedirects(sparkActions: Seq[ActionWithSparkClassName], forceUpdate: Boolean): Int =
    sparkActions.map { case ActionWithSparkClassName(action, sparkClassName) =>
      val redirectFile = new File("docs/uuid/" + action.id + ".md")
      if (!redirectFile.exists() || forceUpdate) {
        createRedirect(redirectFile, action, sparkClassName)
        1
      } else
        0
    }.sum

  private def createRedirect(redirectFile: File, action: Action, sparkClassName: String) = {
    val writer = new PrintWriter(redirectFile)
    writer.println("---")
    writer.println("layout: redirect")
    writer.println("redirect: ../actions/" + DocUtils.underscorize(action.name) + ".html")
    writer.println("---")
    writer.flush()
    writer.close()
    println("Created redirect for " + action.name)
  }
}
