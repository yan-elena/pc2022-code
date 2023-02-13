package pc.modelling

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.*
import pc.modelling.CTMC.Transition

import java.util.Random

class CTMCExperimentTest extends AnyFunSuite:

  enum State:
    case IDLE, DONE

  export State.*
  export pc.modelling.CTMCExperiment.*
  export pc.modelling.CTMCSimulation.*

  def simpleChannel: CTMC[State] = CTMC.ofTransitions(
    Transition(DONE, 1.0 --> DONE)
  )

  test("simpleChannel should holds global DONE") {
    simpleChannel.globally[State](_ == DONE)(simpleChannel.newSimulationTrace(DONE, new Random).take(50)) shouldBe true
  }
