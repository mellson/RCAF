package dk.itu.rcaf

import akka.actor.{ActorSystem, Props}
import com.typesafe.config.ConfigFactory
import dk.itu.rcaf.contextservice.{RootHandler, ClientHandler}

object ContextService extends App {
  val config = ConfigFactory.load("context_service.conf")
  val system = ActorSystem("ContextService", config)
  val handler = system.actorOf(Props[RootHandler], "handler")
}
