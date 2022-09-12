package com.harana.docs.generator.old

import java.io.File

trait LinkPrinter {

  def printActionSiteLinks(actionsByCategory: Map[ActionCategory, Seq[ActionWithSparkClassName]], printAll: Boolean) = {
    println("==== Links for actions.md ====")
    printLinksByCategory(actionsByCategory, (url: String, opName: String) => s"* [$opName]($url)", printAll)
  }

  def printActionMenuLinks(actionsByCategory: Map[ActionCategory, Seq[ActionWithSparkClassName]], printAll: Boolean) = {
    println("==== Links for actionsmenu.html ====")
    printLinksByCategory(
      actionsByCategory,
      (url: String, opName: String) => s"""<li><a href="{{base}}/$url">$opName</a></li>""",
      printAll
    )
  }

  private def printLinksByCategory(sparkActionsByCategory: Map[ActionCategory, Seq[ActionWithSparkClassName]],
                                   createLink: (String, String) => String,
                                   printAll: Boolean) =
    sparkActionsByCategory.foreach { case (category, opList) =>
      val linksForCategory =
        opList.toList.sortBy(_.op.name).flatMap { case ActionWithSparkClassName(op, _) =>
          val underscoredName = DocUtils.underscorize(op.name)
          val url = s"actions/$underscoredName.html"
          val mdFile = new File(s"docs/actions/$underscoredName.md")
          if (!mdFile.exists() || printAll) Some(createLink(url, op.name)) else None
        }
      if (linksForCategory.nonEmpty) {
        println(category.name)
        linksForCategory.foreach(println(_))
        println()
      }
    }
}
