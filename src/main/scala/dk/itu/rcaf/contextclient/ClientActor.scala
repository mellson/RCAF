package dk.itu.rcaf.contextclient

import akka.actor.{Props, Actor}
import dk.itu.rcaf.ContextClientConfig.contextService
import dk.itu.rcaf.abilities._

class ClientActor extends Actor  {
  val timeMonitor = context actorOf Props[TimeMonitor]
  val simpleEntity = context actorOf Props[SimpleEntity]
  contextService ! AddClassListener(simpleEntity, classOf[TimeMonitor])

  override def receive = {
    case msg => println(msg)
  }
}