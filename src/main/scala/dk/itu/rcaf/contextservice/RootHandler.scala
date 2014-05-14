package dk.itu.rcaf.contextservice

import akka.actor.{Props, ActorRef, Actor}

class RootHandler extends Actor {
  var clientToHandlers: Map[ActorRef, ActorRef] = Map.empty

  override def receive: Receive = {
    case msg =>
      // If we don't already have a child, ClientHandler, for this sender add one.
      if (clientToHandlers get sender equals None)
        clientToHandlers = clientToHandlers ++ Map(sender -> context.actorOf(Props[ClientHandler]))

      // Forward the msg to all children.
      context.children.foreach(_ forward msg)
  }
}
