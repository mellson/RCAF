package dk.itu.rcaf.abilities

import akka.actor.{ActorRef, Actor}
import dk.itu.rcaf.SupermarketSimulator

trait Entity extends Actor with ContextItem {
  val id: String = self.path.name

  /**
   * Notify all listeners.
   * Standard method where it is the senders class that is used.
   * @param msg
   */
  def notifyListeners(msg: Any) = contextService ! NotifyListeners(self, getClass, msg)

  /**
   * Notify all listeners.
   * Overloaded method where you add the class whose listeners needs to be notified.
   * @param msg
   * @param clazz
   */
  def notifyListeners(msg: Any, clazz: Class[_]) = contextService ! NotifyListeners(self, clazz, msg)

  /**
   * Notify all listeners.
   * Overload where it is the senders class that is used, but where you provide the context service manually.
   * @param msg
   */
  def notifyListeners(msg: Any, contextService: ActorRef) = contextService ! NotifyListeners(self, getClass, msg)
}