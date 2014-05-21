package dk.itu.rcaf.contextservice

import akka.actor.{Props, ActorRef, Actor}
import dk.itu.rcaf.abilities.GetEntity

class ContextServiceHandler extends Actor {
  var clientToHandlers: Map[ActorRef, ActorRef] = Map.empty

  override def receive: Receive = {
    case GetEntity(name) =>
      sender ! clientToHandlers.filter(_._1.path.name == name)

    case msg =>
      // If we don't already have a child, ClientHandler, for this sender add one.
      if (clientToHandlers get sender equals None)
        clientToHandlers = clientToHandlers ++ Map(sender -> context.actorOf(Props[ClientHandler]))

      // Forward the msg to all children.
      context.children.foreach(_ forward msg)
  }
}