
package com.harana.executor.spark.metrics.scheduler

/*
 * Created by rohitk on 21/09/17.
 */
trait TaskScheduler {
  def schedule(taskTime: Int, stageID: Int = -1): Unit
  def wallClockTime(): Long
  def runTillStageCompletion(): Int
  def isStageComplete(stageID: Int): Boolean
  def onStageFinished(stageID: Int): Unit = ???
}
