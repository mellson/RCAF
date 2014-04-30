package dk.itu.rcaf

import akka.actor._
import com.typesafe.config.ConfigFactory
import dk.itu.rcaf.contextclient.{ClientActor, SimpleActorEntity}
import dk.itu.rcaf.abilities._
import dk.itu.rcaf.example.TimeMonitor
import scala.concurrent.ExecutionContext.Implicits.global

object ContextClient extends App {
  val config = ConfigFactory.load("context_client.conf")
  val system = ActorSystem("ContextClient", config)
  val protocol = config.getString("backend.protocol")
  val systemName = config.getString("backend.system")
  val pathToContextServiceHandler = s"$protocol://$systemName@0.0.0.0:2552/user/handler"
  val contextService = system.actorSelection(pathToContextServiceHandler)

  val clientName = args.headOption.getOrElse("UnnamedClient")
  val client = system.actorOf(Props[ClientActor], clientName)
}