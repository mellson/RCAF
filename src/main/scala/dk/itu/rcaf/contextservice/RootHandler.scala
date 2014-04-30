package dk.itu.rcaf.contextservice

import akka.actor.{Props, ActorRef, Actor}
import dk.itu.rcaf.abilities.ConnectMe
import akka.pattern.pipe

class RootHandler extends Actor {
  var clientToHandlers: Map[ActorRef, ActorRef] = Map.empty

  override def receive: Receive = {
    case ConnectMe =>
      println(sender() + " connected")
      clientToHandlers get sender() match {
        case None => clientToHandlers = clientToHandlers ++ Map(sender() -> context.actorOf(Props[ClientHandler]))
        case Some(actorRef) =>
      }

    case msg =>
      println(msg)
      context.children.foreach(_ forward msg)

  }
}
