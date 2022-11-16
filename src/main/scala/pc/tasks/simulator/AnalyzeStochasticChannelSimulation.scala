package pc.tasks.simulator

import pc.utils.Time
import java.util.Random
import pc.examples.StochasticChannel.*

@main def mainAnalyzeStochasticChannelSimulation =
  val N = 10

  val doneTime = (1 to N)
    .map(_ => stocChannel.newSimulationTrace(IDLE, new Random))
    .map(t =>
      (
        t.find(_.state == DONE).map(_.time).get,
        t.takeWhile(_.state != DONE).findLast(_.state == FAIL).map(_.time).getOrElse(0.0)
      )
    )

  val avgDoneTime = doneTime.map(_._1).sum / N
  val failPercent = (doneTime.map(_._2).sum / doneTime.map(_._1).sum) * 100

  println(s"The average time at which communication is done across $N runs is: $avgDoneTime")
  println(
    s"The percentage that the system is in fail state until communication is done across $N runs is: $failPercent%"
  )
