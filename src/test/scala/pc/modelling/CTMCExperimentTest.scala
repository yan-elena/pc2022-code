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
    Transition(S0, 0.001 --> S1),
    Transition(S1, 1 --> S2),
    Transition(S2, 1 --> S1)
  )

  test("All states of the trace should be globally S0") {
    simpleChannel.globally[State](_ == S0)(simpleChannel.newSimulationTrace(S0, new Random).take(50)) shouldBe true
  }

  test("The steady-state probability in the channel that the state in S1 should be about 50%") {
    channel.steadyState[State](_ == S1)(channel.newSimulationTrace(S0, new Random).take(1000)) shouldBe (0.5 +- 0.1)
  }
