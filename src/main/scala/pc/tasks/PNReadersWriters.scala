package pc.tasks

export pc.modelling.PetriNet
import pc.utils.MSet

object PNReadersWriters:

  enum Place:
    case P1, P2, P3, P4, P5, P6, P7

  export Place.*
  export pc.modelling.PetriNet.*
  export pc.modelling.SystemAnalysis.*
  export pc.utils.MSet

  def pnReadersWriters = PetriNet[Place](
    MSet(P1) ~~> MSet(P2),
    MSet(P2) ~~> MSet(P3),
    MSet(P2) ~~> MSet(P4),
    MSet(P3, P5) ~~> MSet(P5, P6),
    MSet(P4, P5) ~~> MSet(P7) ^^^ MSet(P6),
    MSet(P6) ~~> MSet(P1),
    MSet(P7) ~~> MSet(P1, P5)
  ).toSystem

@main def mainPNReadersWriters =
  import PNReadersWriters.*
  println(pnReadersWriters.paths(MSet(P1, P1, P5), 10).toList.mkString("\n"))

