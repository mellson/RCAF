package dk.itu.rcaf

import akka.actor._
import com.typesafe.config.ConfigFactory
import dk.itu.rcaf.contextclient.ClientActor

object ContextClient extends App {
  val config = ConfigFactory.load("context_client.conf")
  val system = ActorSystem("ContextClient", config)

  val protocol = config.getString("backend.protocol")
  val systemName = config.getString("backend.system")
  val host = config.getString("backend.host")
  val port = config.getInt("backend.port")
  val pathToContextServiceHandler = s"$protocol://$systemName@$host:$port/user/handler"
  val contextService = system.actorSelection(pathToContextServiceHandler)

  val clientName = args.headOption.getOrElse("UnnamedClient")
  val client = system.actorOf(Props[ClientActor], clientName)
}