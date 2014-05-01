package dk.itu.rcaf.contextclient

import akka.actor.{Props, Actor}
import dk.itu.rcaf.ContextClient.contextService
import dk.itu.rcaf.example.TimeMonitor
import dk.itu.rcaf.abilities._

class ClientActor extends Actor  {
  contextService ! Connect

  val timeMonitor = context.actorOf(Props[TimeMonitor], "TimeMonitor")
  val simpleActorEntity1 = context.actorOf(Props[SimpleActorEntity], "SimpleActorEntity1")
  val simpleActorEntity2 = context.actorOf(Props[SimpleActorEntity], "SimpleActorEntity2")
  val simpleActorEntity3 = context.actorOf(Props[SimpleActorEntity], "SimpleActorEntity3")

  contextService tell(AddClassListener(simpleActorEntity1, classOf[TimeMonitor]), simpleActorEntity1)
  contextService tell(AddEntityListener(simpleActorEntity3), simpleActorEntity2)
  contextService tell(NotifyListeners(simpleActorEntity3, classOf[SimpleActorEntity]), simpleActorEntity3)

  override def receive: Receive = {
    case msg => println("client received " + msg)
  }
}
