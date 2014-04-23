package traits

import akka.actor.Actor

class SimpleActor extends Actor {
  def receive = {
    case ent: Entity => println("Entity received " + ent)
    case msg => println(msg)
  }
}