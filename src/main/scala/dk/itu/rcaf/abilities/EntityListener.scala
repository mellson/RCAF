package dk.itu.rcaf.abilities

import akka.actor.Actor

trait EntityListener extends Actor with ContextItem {
  import context._

  def receive: Receive = {
    case StartListening => become(listen)
  }

  def listen: Receive
}
