package dk.itu.rcaf.contextclient

import dk.itu.rcaf.abilities._

class SimpleActorEntity extends Entity {
  def receive = {
    case event: ContextEvent => println(event)
    case msg => println(id + " received msg " + msg)
  }
}