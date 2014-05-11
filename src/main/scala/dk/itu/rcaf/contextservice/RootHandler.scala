package dk.itu.rcaf.contextservice

import akka.actor.{Props, ActorRef, Actor}
import dk.itu.rcaf.abilities.Connect

class RootHandler extends Actor {
  var clientToHandlers: Map[ActorRef, ActorRef] = Map.empty

  override def receive: Receive = {
    case Connect =>
      println(sender + " connected")
      clientToHandlers get sender match {
        case None => clientToHandlers = clientToHandlers ++ Map(sender -> context.actorOf(Props[ClientHandler]))
        case Some(actorRef) =>
      }

    case msg => context.children.foreach(_ forward msg)

  }
}
