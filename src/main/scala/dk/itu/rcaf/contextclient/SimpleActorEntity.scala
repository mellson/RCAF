package dk.itu.rcaf.contextclient

import dk.itu.rcaf.abilities._
import dk.itu.rcaf.abilities.ContextEvent

class SimpleActorEntity extends Entity {
  def receive = {
    case event: ContextEvent => println(event)
    case msg=> println(id + " received msg " + msg)
  }

  override val id: String = self.path.name
}