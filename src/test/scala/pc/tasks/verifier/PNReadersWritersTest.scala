package pc.tasks.verifier

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.*
import pc.utils.MSet

class PNReadersWritersTest extends AnyFunSuite {

  import pc.tasks.verifier.PNReadersWriters.*

  test("PN Readers and Writers should ensure that there is no more than one writer and that there are no readers and writers together.") {
    val mSet = MSet(P1, P1, P5)
    val depth = 20

    val moreWriters = MSet(P7, P7)
    val writersAndReaders = MSet(P6, P7)

    pnReadersWriters
      .paths(mSet, depth)
      .foreach(_ should contain noneOf (moreWriters, writersAndReaders))
  }
}
