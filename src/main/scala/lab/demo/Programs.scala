package lab.demo

import it.unibo.scafi.incarnations.BasicAbstractIncarnation
import it.unibo.scafi.simulation.s2.frontend.incarnation.scafi.bridge.ExportEvaluation.EXPORT_EVALUATION
import it.unibo.scafi.simulation.s2.frontend.incarnation.scafi.bridge.ScafiWorldIncarnation.EXPORT
import it.unibo.scafi.simulation.s2.frontend.incarnation.scafi.bridge.SimulationInfo
import it.unibo.scafi.simulation.s2.frontend.incarnation.scafi.configuration.{
  ScafiProgramBuilder,
  ScafiWorldInformation
}
import it.unibo.scafi.simulation.s2.frontend.incarnation.scafi.world.ScafiWorldInitializer.Random
import it.unibo.scafi.simulation.s2.frontend.view.{ViewSetting, WindowConfiguration}
import it.unibo.scafi.space.graphics2D.BasicShape2D.Circle
import lab.gui.patch.RadiusLikeSimulation

import scala.reflect._

object Incarnation extends BasicAbstractIncarnation
import lab.demo.Incarnation._ //import all stuff from an incarnation

class Simulation[R: ClassTag] extends App {

  val formatter_evaluation: EXPORT_EVALUATION[Any] = (e: EXPORT) => formatter(e.root[Any]())

  val formatter: Any => Any = e =>
    e match {
      case (a, b) => (formatter(a), formatter(b))
      case (a, b, c) => (formatter(a), formatter(b), formatter(c))
      case (a, b, c, d) => (formatter(a), formatter(b), formatter(c), formatter(d))
      case l: Iterable[_] => l.map(formatter(_)).toString
      case i: java.lang.Number if i.doubleValue() > 100000 => "Inf"
      case i: java.lang.Number if -i.doubleValue() > 100000 => "-Inf"
      case i: java.lang.Double => f"${i.doubleValue()}%1.2f"
      case x => x.toString
    }

  val nodes = 100
  val neighbourRange = 200
  val (width, height) = (1500, 800)

  ViewSetting.windowConfiguration = WindowConfiguration(width, height)
  ScafiProgramBuilder(
    Random(nodes, width, height),
    SimulationInfo(implicitly[ClassTag[R]].runtimeClass, exportEvaluations = List(formatter_evaluation)),
    RadiusLikeSimulation(neighbourRange),
    ScafiWorldInformation(shape = Some(Circle(5, 5))),
    neighbourRender = true
  ).launch()
}

abstract class AggregateProgramSkeleton extends AggregateProgram with StandardSensors {
  def sense1 = sense[Boolean]("sens1")
  def sense2 = sense[Boolean]("sens2")
  def sense3 = sense[Boolean]("sens3")
  def boolToInt(b: Boolean) = mux(b)(1)(0)
}

class NbrLive extends AggregateProgramSkeleton {
//  override def main(): Boolean = foldhood(true)(_ && _)(nbr{sense1})
//  override def main(): Int = foldhood(0)(_ + _)(nbr(1)) //associated to each device the size of the neighbor
//  override def main(): Int = foldhoodPlus(0)(_ + _)(nbr(1)) // plus means excluding the "self" device
//  override def main(): Int = foldhoodPlus(0)(_ + _)(nbr {boolToInt(sense1)}) // the number of neighbor which have the sensing true
  override def main(): Double =
    foldhoodPlus(0)(_ + _)(nbr(boolToInt(sense1))).toDouble / foldhoodPlus(0)(_ + _)(nbr(1))
}
object DemoNbrLive extends Simulation[NbrLive]

class Main1 extends AggregateProgramSkeleton {
  override def main() = 1
}
object Demo1 extends Simulation[Main1]

class Main2 extends AggregateProgramSkeleton {
  override def main() = 2 + 3
}
object Demo2 extends Simulation[Main2]

class Main3 extends AggregateProgramSkeleton {
  override def main() = (10, 20)
}
object Demo3 extends Simulation[Main3]

class Main4 extends AggregateProgramSkeleton {
  override def main() = Math.random()
}
object Demo4 extends Simulation[Main4]

class Main5 extends AggregateProgramSkeleton {
  override def main() = sense1
}
object Demo5 extends Simulation[Main5]

class Main6 extends AggregateProgramSkeleton {
  override def main() = if (sense1) 10 else 20
}
object Demo6 extends Simulation[Main6]

class Main7 extends AggregateProgramSkeleton {
  override def main() = mid() // id of the device
}
object Demo7 extends Simulation[Main7]

class Main8 extends AggregateProgramSkeleton {
//  override def main() = minHoodPlus(nbrRange) // nbrRange maps each device to each neighbor the distance of the neighbor to him, form of gradient
//  override def main() = foldhood(Set[Double]())(_ ++ _)(Set(nbrRange)) // creating a set
//  override def main() = foldhood(0.0)(_ max _)(nbrRange) // max distance
//  override def main() = foldhood(Double.MaxValue)(_ min _)(nbrRange) // min distance
  override def main() = minHoodPlus(nbrRange)
}
object Demo8 extends Simulation[Main8]

class Main9 extends AggregateProgramSkeleton {
  override def main() = rep(0)(_ + 1)
}
object Demo9 extends Simulation[Main9]

class Main10 extends AggregateProgramSkeleton {
  override def main() = rep(Math.random())(x => x)
}
object Demo10 extends Simulation[Main10]

class Main11 extends AggregateProgramSkeleton {
  override def main() = rep[Double](0.0)(x => x + rep(Math.random())(y => y))
}
object Demo11 extends Simulation[Main11]

class Main12 extends AggregateProgramSkeleton {
  import Builtins.Bounded.of_i

  override def main() = maxHoodPlus(boolToInt(nbr(sense1)))
}
object Demo12 extends Simulation[Main12]

class Main13 extends AggregateProgramSkeleton {
  override def main() = foldhoodPlus(0)(_ + _)(nbr(1))
}
object Demo13 extends Simulation[Main13]

class Main14 extends AggregateProgramSkeleton {
  import Builtins.Bounded.of_i

  override def main() = rep(0)(x => boolToInt(sense1) max maxHoodPlus(nbr(x))) //gossip
}
object Demo14 extends Simulation[Main14]

class Main15 extends AggregateProgramSkeleton {
  override def main() = rep(Double.MaxValue)(d => mux[Double](sense1)(0.0)(minHoodPlus(nbr(d) + 1.0)))
}
object Demo15 extends Simulation[Main15]

class Main16 extends AggregateProgramSkeleton {
  def gradient(src: Boolean): Double =
    rep(Double.MaxValue)(d => mux[Double](sense1)(0.0)(minHoodPlus(nbr(d) + nbrRange)))
  override def main() =
    //branch(condition)(true)(false): when the condition is true, I want the gradient, otherwise 0
    branch(sense2)(0.0)(gradient(sense1))
}
object Demo16 extends Simulation[Main16]

// Tasks

// where sense1 is active count from 0 to 1000 and then stay freezed at 1000, otherwise 0
class Case9 extends AggregateProgramSkeleton {
  override def main() = branch[Int](sense1)(rep(0)(x => if (x < 1000) x + 1 else x))(0)
}
object DemoCase9 extends Simulation[Case9]

// gather in each node the set of neighbours’ IDs
class Case12 extends AggregateProgramSkeleton {
  override def main() = foldhood(Set[ID]())(_ ++ _)(Set(nbr(mid())))
}
object DemoCase12 extends Simulation[Case12]

//have in each node the ID of the closest neighbour
//⇒ used minHoodPlus, construct a pair of distance (nbrRange) and id (idnbrm), note minHoodPlus
//correctly works on pairs
class Case8 extends AggregateProgramSkeleton {
  override def main() = minHoodPlus((nbrRange, nbr(mid())))
}
object DemoCase8 extends Simulation[Case8]

// gossip the maximum value of ID (type Int)
// note a problem: it won't correctly repair upon network changes
// => use max and maxHoodPlus smoothly
class Case14 extends AggregateProgramSkeleton {
  import Builtins.Bounded.of_i

  override def main() = rep(mid())(x => x max maxHoodPlus(nbr(mid())))
}
object DemoCase14 extends Simulation[Case14]
