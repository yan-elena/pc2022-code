package pc.tasks.artist

object PNWithPrioritiesMutualExclusion:

  enum Place:
    case N, T, C

  export Place.*
  export pc.tasks.artist.PetriNetWithPriorities.*
  export pc.modelling.SystemAnalysis.*
  export pc.utils.MSet

  def pnME = PetriNetWithPriorities[Place](
    MSet(N) ~ 1 ~> MSet(T),
    MSet(T) ~ 2 ~> MSet(C) ^^^ MSet(C),
    MSet(C) ~ 1 ~> MSet()
  ).toSystem

@main def mainPNPriorMutualExclusion =
  import PNWithPrioritiesMutualExclusion.*
  println(pnME.paths(MSet(N, N), 7).toList.mkString("\n"))
