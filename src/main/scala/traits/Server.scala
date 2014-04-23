package traits

import akka.actor.{ActorSystem, Props}
import com.typesafe.config.ConfigFactory

object Server extends App {
  val config = ConfigFactory.load("backend")
  val system = ActorSystem("backend", config)
  system.actorOf(Props[SimpleActor], "simple")
}
