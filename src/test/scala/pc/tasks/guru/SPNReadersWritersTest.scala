package pc.tasks.guru

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.*
import pc.utils.MSet

import java.util.Random

class SPNReadersWritersTest extends AnyFunSuite:

  import pc.tasks.guru.SPNReadersWriters.*

  test(
    "SPN Readers and Writers should ensure that there is no more than one writer and that there are no readers and writers together."
  ) {
    val mSet = MSet(P1, P1, P5)
    val depth = 20

    val moreWriters = MSet(P7, P5)
    val writersAndReaders = MSet(P6, P7)

    toCTMC(spn)
      .newSimulationTrace(mSet, new Random)
      .take(depth)
      .map(_.state)
      .foreach { s =>
        s.matches(moreWriters) shouldBe false
        s.matches(writersAndReaders) shouldBe false
      }
  }
