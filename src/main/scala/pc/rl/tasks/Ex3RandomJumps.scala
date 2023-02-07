package pc.rl.tasks

import pc.rl.model.QMatrix

import scala.util.Random

object Ex3RandomJumps extends App:

  import pc.rl.model.QMatrix.*
  import pc.rl.model.QMatrix.Action.*

  val rand = Random
  val jumpPos = Set((0, 2), (1, 0), (3, 1), (3, 2), (5, 0), (5, 1), (7, 1))
  val rl: QMatrix.Facade = Facade(
    width = 8,
    height = 3,
    initial = (0, 0),
    terminal = { case (7, 2) => true; case _ => false },
    reward = {
      case ((7, 2), _) => 100;
      case _ => -1
    },
    jumps = { case (x, _) if jumpPos.contains(x) => (rand.nextInt(8), rand.nextInt(3)) },
    gamma = 0.9,
    alpha = 0.5,
    epsilon = 0.4,
    v0 = 1
  )

  println("Jumps: ")
  println(rl.show((a, b) => if jumpPos.contains((a, b)) then "*" else "-", "%2s"))

  val q0 = rl.qFunction
  println(rl.show(q0.vFunction, "%2.1f"))
  val q1 = rl.makeLearningInstance().learn(2000, 100, q0)
  println(rl.show(q1.vFunction, "%3.1f"))
  println(rl.show(s => actionToString(q1.bestPolicy(s)), "%7s"))
