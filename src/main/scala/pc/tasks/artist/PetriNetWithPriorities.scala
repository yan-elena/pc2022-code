package pc.tasks.artist

import pc.modelling.System
import pc.utils.MSet

object PetriNetWithPriorities:

  // pre-conditions, effects, priority, inhibition
  case class Trn[P](cond: MSet[P], eff: MSet[P], prior: MSet[P] => Int, inh: MSet[P])
  type PetriNetWithPriorities[P] = Set[Trn[P]]
  type Marking[P] = MSet[P]

  def apply[P](transitions: Trn[P]*): PetriNetWithPriorities[P] = transitions.toSet

  extension [P](pn: PetriNetWithPriorities[P])
    def toSystem: System[Marking[P]] = m =>
//      val pnSorted = pn.toSeq.sortBy(_.prior)
      for
        Trn(cond, eff, prior, inh) <- pn //Sorted
        if m disjoined inh
        p = prior(m)
        out <- m extract cond
      yield (p, out union eff)
