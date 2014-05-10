package dk.itu.rcaf.abilities

import akka.actor.Actor
import dk.itu.rcaf.Simulator

trait Entity extends Actor with ContextItem {
  val id: String = self.path.name

  val getContext: Context = Context(this)

//  def notifyListeners() = contextService ! NotifyListeners(self, getClass)
  def notifyListeners() = Simulator.handler ! NotifyListeners(self, getClass) // TODO rethink this coupling
}
