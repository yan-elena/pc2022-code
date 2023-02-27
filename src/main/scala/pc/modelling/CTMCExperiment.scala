package pc.modelling

import java.util.Random

object CTMCExperiment:

  import CTMCSimulation.*

  type Property[A] = Trace[A] => Boolean
  type Probability[A] = Trace[A] => Double

  extension [S](self: CTMC[S])
    // globally is simply achieved by equivalence not G x= F not x
    def eventually[A](filt: A => Boolean): Property[A] =
      trace => trace exists (e => filt(e.state))

    // takes a property and makes it time bounded by the magics of streams
    def bounded[A](timeBound: Double)(prop: Property[A]): Property[A] =
      trace => prop(trace takeWhile (_.time <= timeBound))

    // a PRISM-like experiment, giving a statistical result (in [0,1])
    def experiment(runs: Int = 10000, prop: Property[S], rnd: Random = new Random, s0: S, timeBound: Double): Double =
      (0 until runs).count(i => bounded(timeBound)(prop)(self.newSimulationTrace(s0, rnd))).toDouble / runs

    // globally: in all the path, the property holds
    def globally[A](filt: A => Boolean): Property[A] =
      trace => trace forall (e => filt(e.state))

    // steady-state
    def steadyState[A](filt: A => Boolean): Probability[A] =
      trace =>
        trace
          .foldLeft((0.0, 0.0)) {
            case ((a, t0), Event(t1, s)) if filt(s) => (a + (t1 - t0), t1)
            case ((a, _), e) => (a, e.time)
          }
          ._1 / trace.last.time
