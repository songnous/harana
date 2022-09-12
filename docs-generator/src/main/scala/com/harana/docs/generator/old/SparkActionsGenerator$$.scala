package com.harana.docs.generator.old

import java.util.Scanner

/** This app generates Harana documentation for Spark ported actions. It is capable of generating redirects and
 * documentation pages automatically. To achieve that, it uses CatalogRecorder and reflection.
 *
 * Link generation for action menu and action subpage is semi-automatic. The app prints links to System.out and
 * the user is responsible for inserting them into proper places in actions.md and actionsmenu.html files.
 *
 * The typical use case consists of:
 *   1. Running the app in link generation mode for new actions. 2. Manually inserting generated links to
 *      actions.md and actionsmenu.html files. 3. Running the app in documentation page & redirect creation mode
 *      (for new actinos).
 *
 * Order of actions is important, because link generation recognizes new actions by non-existence of their
 * documentation pages.
 */
object SparkActionsGenerator$$
  extends PageCreator
    with SparkActionsExtractor
    with RedirectCreator
    with LinkPrinter {

  val sparkVersion = org.apache.spark.SPARK_VERSION

  val scalaDocPrefix = s"https://spark.apache.org/docs/$sparkVersion/api/scala/index.html#"

  // scalastyle:off println
  def main(args: Array[String]): Unit = {

    val sc = new Scanner(System.in)

    println("==========================")
    println("= Harana doc generator =")
    println("==========================")
    println
    println("What do you want to do?")
    println("[P]rint links to new Spark actions")
    println("[C]reate documentation pages and redirects for Spark actions")
    print("> ")

    sc.nextLine().toLowerCase match {
      case "p" =>
        println("Do you want to print [A]ll links or only links to [N]ew actions?")
        print("> ")
        sc.nextLine().toLowerCase match {
          case "a" => printLinks(true)
          case "n" => printLinks(false)
          case _ => wrongInputExit()
        }
      case "c" =>
        println("Do you want to [R]ecreate all pages and redirects or [U]pdate for new actions?")
        print("> ")
        sc.nextLine().toLowerCase match {
          case "r" => createDocPagesAndRedirects(true)
          case "u" => createDocPagesAndRedirects(false)
          case _ => wrongInputExit()
        }
      case _ => wrongInputExit()
    }
  }

  private def wrongInputExit(): Unit = {
    println("Unexpected input. Exiting...")
    System.exit(1)
  }

  private def printLinks(printAll: Boolean): Unit = {
    val sparkActionsByCategory = mapByCategory(sparkActions())
    printActionSiteLinks(sparkActionsByCategory, printAll)
    printActionMenuLinks(sparkActionsByCategory, printAll)
  }

  private def createDocPagesAndRedirects(forceUpdate: Boolean): Unit = {
    val actions = sparkActions()
    val redirectCount = createRedirects(actions, forceUpdate)
    val pageCount = createDocPages(actions, forceUpdate)

    if (redirectCount == 0) println("No redirects updated.") else println(s"Updated $redirectCount redirects.")
    if (pageCount == 0) println("No pages updated.") else println(s"Updated $pageCount pages.")
  }
}
