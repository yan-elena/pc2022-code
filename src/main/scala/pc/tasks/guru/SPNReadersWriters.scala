package pc.tasks.guru

import pc.modelling.{CTMC, SPN}
import pc.utils.MSet
import java.util.Random

object SPNReadersWriters extends App:

  enum Place:
    case P1, P2, P3, P4, P5, P6, P7

  export Place.*
  export pc.modelling.CTMCSimulation.*
  export pc.modelling.SPN.*

  val spn = SPN[Place](
    Trn(MSet(P1), m => 1.0, MSet(P2), MSet()), //t1
    Trn(MSet(P2), m => 200000, MSet(P3), MSet()), //t2
    Trn(MSet(P2), m => 100000, MSet(P4), MSet()), //t3
    Trn(MSet(P3, P5), m => 100000, MSet(P5, P6), MSet()), //t4
    Trn(MSet(P4, P5), m => 100000, MSet(P7), MSet(P6)), //t5
    Trn(MSet(P6), m => 0.1 * m(P6), MSet(P1), MSet()), //t6
    Trn(MSet(P7), m => 0.2, MSet(P1, P5), MSet()) //t7
  )

  println(
    toCTMC(spn)
      .newSimulationTrace(MSet(P1, P1, P1, P5), new Random)
      .take(20)
      .toList
      .mkString("\n")
  )
