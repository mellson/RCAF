package dk.itu.rcaf.contextservice

import akka.actor.Actor

class EntityListenerHandler extends Actor {
  override def receive: Receive = {
    case msg => println(msg)
  }
}
