package dk.itu.rcaf.contextclient

import akka.actor.Actor

class ClientActor extends Actor  {

  override def receive = {
    case msg => println(msg)
  }
}