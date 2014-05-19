package dk.itu.rcaf.abilities

import akka.actor.Actor

trait EntityListener extends Actor with ContextItem {
  override val id = self.path.name
}
