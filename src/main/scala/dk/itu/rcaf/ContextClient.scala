package dk.itu.rcaf

import akka.actor._
import com.typesafe.config.ConfigFactory
import dk.itu.rcaf.contextclient.SimpleActorEntity
import dk.itu.rcaf.abilities.{AddEntityListener, NotifyListeners, AddClassListener}

object ContextClient extends App {
  val config = ConfigFactory.load("context_client.conf")
  val system = ActorSystem("ContextClient", config)
  val pathToContextServiceHandler = "akka.tcp://ContextService@0.0.0.0:2552/user/handler"
  val contextService = system.actorSelection(pathToContextServiceHandler)

  val actor1 = system.actorOf(Props[SimpleActorEntity], "actor1")
  val actor2 = system.actorOf(Props[SimpleActorEntity], "actor2")
  val actor3 = system.actorOf(Props[SimpleActorEntity], "actor3")

  contextService ! AddClassListener(actor1, classOf[SimpleActorEntity])
  contextService ! AddClassListener(actor2, classOf[SimpleActorEntity])
  contextService ! AddEntityListener(actor3, actor2.path.name)
  contextService ! NotifyListeners(actor2.path.name, classOf[SimpleActorEntity])
}