package dk.itu.rcaf

import akka.actor._
import com.typesafe.config.ConfigFactory
import dk.itu.rcaf.contextclient.SimpleActorEntity
import dk.itu.rcaf.abilities._
import dk.itu.rcaf.example.TimeSensor

object ContextClient extends App {
  val config = ConfigFactory.load("context_client.conf")
  val system = ActorSystem("ContextClient", config)
  val pathToContextServiceHandler = "akka.tcp://ContextService@0.0.0.0:2552/user/handler"
  val contextService = system.actorSelection(pathToContextServiceHandler)

  val timeActor = system.actorOf(Props[TimeSensor], "TimeSensor")
  val actor1 = system.actorOf(Props[SimpleActorEntity], "actor1")
  val actor2 = system.actorOf(Props[SimpleActorEntity], "actor2")
  val actor3 = system.actorOf(Props[SimpleActorEntity], "actor3")

  contextService tell(AddClassListener(actor1, classOf[TimeSensor]), actor1)
  contextService tell(AddClassListener(actor2, classOf[TimeSensor]), actor2)
  contextService tell(AddClassListener(actor3, classOf[SimpleActorEntity]), actor3)

  contextService tell(AddEntityListener(actor3), actor2)
  contextService tell(NotifyListeners(actor3, classOf[SimpleActorEntity]), actor3)
}