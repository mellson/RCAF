package dk.itu.rcaf.abilities

import akka.actor.Actor

trait Entity extends Actor with ContextItem {
  val id: String = self.path.name

  val getContext: Context = Context(this)

  def notifyListeners() = contextService ! NotifyListeners(self, getClass)
}
