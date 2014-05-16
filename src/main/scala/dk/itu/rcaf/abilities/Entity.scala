package dk.itu.rcaf.abilities

import akka.actor.Actor
import dk.itu.rcaf.SupermarketSimulator

trait Entity extends Actor with ContextItem {
  val id: String = self.path.name

  val getContext: Context = Context(this)

  def notifyListeners(event: ContextEvent, clazz: Class[_]) = contextService ! NotifyListeners(self, clazz, event)
  def notifyListeners(event: ContextEvent) = contextService ! NotifyListeners(self, getClass, event)

//  def notifyListeners(message: Any) = SupermarketSimulator.handler ! NotifyListeners(self, getClass, message) // TODO rethink this coupling
}
