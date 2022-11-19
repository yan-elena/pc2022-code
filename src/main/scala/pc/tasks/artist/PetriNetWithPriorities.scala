package pc.tasks.artist

import pc.modelling.System
import pc.utils.MSet

object PetriNetWithPriorities:

  // pre-conditions, effects, priority, inhibition
  case class Trn[P](cond: MSet[P], eff: MSet[P], prior: Int, inh: MSet[P])
  type PetriNetWithPriorities[P] = Set[Trn[P]]
  type Marking[P] = MSet[P]

  def apply[P](transitions: Trn[P]*): PetriNetWithPriorities[P] = transitions.toSet

  extension [P](pn: PetriNetWithPriorities[P])
    def toSystem: System[Marking[P]] = m =>
      (for
        Trn(cond, eff, prior, inh) <- pn
        if m disjoined inh
        out <- m extract cond
      yield (prior, out union eff)).groupBy(_._1).maxBy(_._1)._2.map(_._2)

  extension [P](self: Marking[P]) def ~(p: Int) = Trn(self, MSet(), p, MSet())
  extension [P](self: Trn[P]) def ~>(y: Marking[P]) = self.copy(eff = y)
  extension [P](self: Trn[P]) def ^^^(z: Marking[P]) = self.copy(inh = z)
