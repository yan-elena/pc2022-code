package pc.tasks.designer

import pc.modelling

object PNRReadersWritersVariation:

  export pc.tasks.verifier.PNReadersWriters.Place
  export pc.tasks.verifier.PNReadersWriters.Place.*
  export pc.modelling.PetriNet
  export pc.modelling.PetriNet.*
  export pc.modelling.SystemAnalysis.*
  export pc.utils.MSet

  def pnRReadersWritersVariation = PetriNet[Place](
    MSet(P1) ~~> MSet(P2),
    MSet(P2) ~~> MSet(P3),
    MSet(P2) ~~> MSet(P4),
    MSet(P3, P5) ~~> MSet(P5, P6),
    MSet(P4, P5) ~~> MSet(P7) ^^^ MSet(P3, P6), //if a process wants to read, it eventually does.
    MSet(P6) ~~> MSet(P1),
    MSet(P7) ~~> MSet(P1, P5)
  ).toSystem

@main def mainPNReadersWritersVariation =
  import PNRReadersWritersVariation.*
  println(pnRReadersWritersVariation.paths(MSet(P1, P1, P5), 10).toList.mkString("\n"))
