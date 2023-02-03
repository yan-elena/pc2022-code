package pc.rl.tasks

import pc.rl.model.QMatrix

object Ex2Labyrinth extends App:

  import pc.rl.model.QMatrix.Action.*
  import pc.rl.model.QMatrix.*

  val obstacles =
    Set((1, 1), (1, 2), (2, 2), (2, 3), (2, 4), (3, 0), (4, 1), (4, 2), (4, 3), (6, 1), (6, 2), (6, 3), (7, 1))
  val rl: QMatrix.Facade = Facade(
    width = 8,
    height = 5,
    initial = (0, 0),
    terminal = { case (7, 0) => true; case _ => false },
    reward = {
      case ((7, 0), _) => 100;
      case (x, _) if obstacles.contains(x) => -100;
      case _ => -1
    },
    jumps = PartialFunction.empty,
    gamma = 0.9,
    alpha = 0.5,
    epsilon = 0.4,
    v0 = 1
  )

  val q0 = rl.qFunction
  println(rl.show(q0.vFunction, "%2.1f"))
  val q1 = rl.makeLearningInstance().learn(2000, 100, q0)
  println(rl.show(q1.vFunction, "%3.1f"))
  println(rl.show(s => actionToString(q1.bestPolicy(s)), "%7s"))
