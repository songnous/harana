

package com.harana.executor.spark.metrics.analyzer

import com.harana.executor.spark.metrics.{common, helper}

import scala.collection.mutable

/*
 * Created by rohitk on 21/09/17.
 */
class EfficiencyStatisticsAnalyzer extends AppAnalyzer {

  def analyze(appContext: common.AppContext, startTime: Long, endTime: Long): String = {
    val ac = appContext.filterByStartAndEndTime(startTime, endTime)
    val out = new mutable.StringBuilder()
    // wall clock time, appEnd - appStart
    val appTotalTime = endTime - startTime
    // wall clock time per Job. Aggregated
    val jobTime = helper.JobOverlapHelper.estimatedTimeSpentInJobs(ac)
    /* sum of cores in all the executors:
     * There are executors coming up and going down.
     * We are taking the max-number of executors running at any point of time, and
     * multiplying it by num-cores per executor (assuming homogenous cluster)
     */
    val maxExecutors = common.AppContext.getMaxConcurrent(ac.executorMap, ac)
    val executorCores = common.AppContext.getExecutorCores(ac)
    val totalCores = executorCores * maxExecutors

    // total compute millis available to the application
    val appComputeMillisAvailable = totalCores * appTotalTime
    val computeMillisFromExecutorLifetime = ac.executorMap.map( x => {
        val ecores = x._2.cores
        val estartTime = Math.max(startTime, x._2.startTime)
        val eendTime  = if (x._2.isFinished()) {
          Math.min(endTime, x._2.endTime)
        }else {
          endTime
        }
        ecores * (eendTime - estartTime)
      }).sum

    // some of the compute millis are lost when driver is doing some work
    // and has not assigned any work to the executors
    // We assume executors are only busy when one of the job is in progress
    val inJobComputeMillisAvailable = totalCores * jobTime
    // Minimum time required to run a job even when we have infinite number
    // of executors, essentially the max time taken by any task in the stage.
    // which is in the critical path. Note that some stages can run in parallel
    // we cannot reduce the job time to less than this number.
    // Aggregating over all jobs, to get the lower bound on this time.
    val criticalPathTime = helper.JobOverlapHelper.criticalPathForAllJobs(ac)

    //sum of millis used by all tasks of all jobs
    val inJobComputeMillisUsed  = ac.jobMap.values
      .filter(x => x.endTime > 0)
      .filter(x => x.jobMetrics.map.isDefinedAt(common.AggregateMetrics.executorRuntime))
      .map(x => x.jobMetrics.map(common.AggregateMetrics.executorRuntime).value)
      .sum

    val perfectJobTime  = inJobComputeMillisUsed/totalCores
    //Enough variables lets print some

    val driverTimeJobBased = appTotalTime - jobTime
    val driverComputeMillisWastedJobBased  = driverTimeJobBased * totalCores

    out.println(f""" Time spent in Driver vs Executors
              | Driver WallClock Time    ${pd(driverTimeJobBased)}   ${driverTimeJobBased*100/appTotalTime.toFloat}%3.2f%%
              | Executor WallClock Time  ${pd(jobTime)}   ${jobTime*100/appTotalTime.toFloat}%3.2f%%
              | Total WallClock Time     ${pd(appTotalTime)}
      """.stripMargin)


    out.println (
      s"""
         |
         |Minimum possible time for the app based on the critical path (with infinite resources)   ${pd(driverTimeJobBased + criticalPathTime)}
         |Minimum possible time for the app with same executors, perfect parallelism and zero skew ${pd(driverTimeJobBased + perfectJobTime)}
         |If we were to run this app with single executor and single core                          ${pcm(driverTimeJobBased + inJobComputeMillisUsed)}
         |
       """.stripMargin)

    out.println (s" Total cores available to the app ${totalCores}")
    val executorUsedPercent = inJobComputeMillisUsed*100/inJobComputeMillisAvailable.toFloat
    val executorWastedPercent = (inJobComputeMillisAvailable - inJobComputeMillisUsed)*100/inJobComputeMillisAvailable.toFloat

    val driverWastedPercentOverAll = driverComputeMillisWastedJobBased*100/appComputeMillisAvailable.toFloat
    val executorWastedPercentOverAll = (inJobComputeMillisAvailable - inJobComputeMillisUsed)*100 / appComputeMillisAvailable.toFloat
    out.println (
      f"""
         | OneCoreComputeHours: Measure of total compute power available from cluster. One core in the executor, running
         |                      for one hour, counts as one OneCoreComputeHour. Executors with 4 cores, will have 4 times
         |                      the OneCoreComputeHours compared to one with just one core. Similarly, one core executor
         |                      running for 4 hours will OnCoreComputeHours equal to 4 core executor running for 1 hour.
         |
         | Driver Utilization (Cluster idle because of driver)
         |
         | Total OneCoreComputeHours available                     ${pcm(appComputeMillisAvailable)}%15s
         | Total OneCoreComputeHours available (AutoScale Aware)   ${pcm(computeMillisFromExecutorLifetime)}%15s
         | OneCoreComputeHours wasted by driver                    ${pcm(driverComputeMillisWastedJobBased)}%15s
         |
         | AutoScale Aware: Most of the calculations by this tool will assume that all executors are available throughout
         |                  the runtime of the application. The number above is printed to show possible caution to be
         |                  taken in interpreting the efficiency metrics.
         |
         | Cluster Utilization (Executors idle because of lack of tasks or skew)
         |
         | Executor OneCoreComputeHours available          ${pcm(inJobComputeMillisAvailable)}%15s
         | Executor OneCoreComputeHours used               ${pcm(inJobComputeMillisUsed)}%15s        ${executorUsedPercent}%3.2f%%
         | OneCoreComputeHours wasted                      ${pcm(inJobComputeMillisAvailable - inJobComputeMillisUsed)}%15s        ${executorWastedPercent}%3.2f%%
         |
         | App Level Wastage Metrics (Driver + Executor)
         |
         | OneCoreComputeHours wasted Driver               ${driverWastedPercentOverAll}%3.2f%%
         | OneCoreComputeHours wasted Executor             ${executorWastedPercentOverAll}%3.2f%%
         | OneCoreComputeHours wasted Total                ${driverWastedPercentOverAll+executorWastedPercentOverAll}%3.2f%%
         |
       """.stripMargin)

    out.toString()
  }
}
