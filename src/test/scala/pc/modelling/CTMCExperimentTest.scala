package pc.modelling

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.*
import pc.modelling.CTMC.Transition

import java.util.Random

class CTMCExperimentTest extends AnyFunSuite:

  enum State:
    case S0, S1, S2

  export State.*
  export pc.modelling.CTMCExperiment.*
  export pc.modelling.CTMCSimulation.*

  def simpleChannel: CTMC[State] = CTMC.ofTransitions(
    Transition(S0, 1.0 --> S0)
  )

  def channel: CTMC[State] = CTMC.ofTransitions(
    Transition(S0, 1 --> S1),
    Transition(S1, 1 --> S2),
    Transition(S2, 2 --> S1)
  )

  test("All states of the trace should be globally S0") {
    simpleChannel.globally[State](_ == S0)(simpleChannel.newSimulationTrace(S0, new Random).take(50)) shouldBe true
  }

  test("Testing the steady-state probability for each state in the channel") {
    val ch = channel.newSimulationTrace(S0, new Random).take(1000)

    channel.steadyState[State](_ == S0)(ch) shouldBe (0.0 +- 0.1)
    channel.steadyState[State](_ == S0)(ch) shouldBe (0.6 +- 0.1)
    channel.steadyState[State](_ == S0)(ch) shouldBe (0.3 +- 0.1)
  }
