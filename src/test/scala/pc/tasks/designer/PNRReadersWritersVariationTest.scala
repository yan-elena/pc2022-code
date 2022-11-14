package pc.tasks.designer

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.*
import pc.tasks.verifier.PNReadersWriters.Place

class PNRReadersWritersVariationTest extends AnyFunSuite:

  import pc.tasks.designer.PNRReadersWritersVariation.*

  test("if a process says it wants to read, it eventually (surely) does so") {
    val mSet = MSet(P1, P1, P5)
    val depth = 20

    val wantToRead: MSet[Place] => Boolean = _.matches(MSet(P3))
    val read: MSet[Place] => Boolean = _.matches(MSet(P6))

    pnRReadersWritersVariation.paths(mSet, depth).foreach { p =>
      // if the process wants to read
      if p.exists(wantToRead) && !wantToRead(p.last) then
        // it eventually (surely) does so
        p.reverse.takeWhile(!wantToRead(_)).reverse.exists(read) shouldBe true
    }
  }
