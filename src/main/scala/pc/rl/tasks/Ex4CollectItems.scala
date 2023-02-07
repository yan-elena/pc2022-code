package pc.rl.tasks

import pc.rl.model.QMatrix

object Ex4CollectItems extends App:

  import pc.rl.model.QMatrix.*
  import pc.rl.model.QMatrix.Action.*

  val items = Set((1, 0), (2, 1), (3, 1), (4, 0), (5, 2), (6, 2), (7, 1))
  var collected: Set[(Int, Int)] = Set()
  val rl: QMatrix.Facade = Facade(
    width = 8,
    height = 3,
    initial = (0, 0),
    terminal = { case _ => items.size == collected.size },
    reward = {
      case ((0, 0), _) => collected = Set(); -1
      case (x, _) if items.contains(x) && (!collected.contains(x)) => collected = collected + x; 100
      case _ => -1
    },
    jumps = PartialFunction.empty,
    gamma = 0.5,
    alpha = 0.5,
    epsilon = 0.3,
    v0 = 1
  )

  println("Items: ")
  println(rl.show((a, b) => if items.contains((a, b)) then "*" else "-", "%2s"))

  val q0 = rl.qFunction
  println(rl.show(q0.vFunction, "%2.1f"))
  val q1 = rl.makeLearningInstance().learn(2000, 100, q0)
  println(rl.show(q1.vFunction, "%3.1f"))
  println(rl.show(s => actionToString(q1.bestPolicy(s)), "%7s"))

  println("Collected items: " + collected)
