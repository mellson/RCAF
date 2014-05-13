package dk.itu.rcaf.contextservice

import akka.actor.{Props, ActorRef, Actor}

class RootHandler extends Actor {
  var clientToHandlers: Map[ActorRef, ActorRef] = Map.empty

  override def receive: Receive = {

    case msg =>
      clientToHandlers get sender match {
        case None => clientToHandlers = clientToHandlers ++ Map(sender -> context.actorOf(Props[ClientHandler]))
        case Some(actorRef) =>
      }

      context.children.foreach(_ forward msg)

  }
}
