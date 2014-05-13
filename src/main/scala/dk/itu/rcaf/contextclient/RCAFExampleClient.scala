package dk.itu.rcaf.contextclient

import akka.actor.{Props, Actor}
import dk.itu.rcaf.ContextClient._
import dk.itu.rcaf.abilities._

class RCAFExampleClient extends Actor {
  val timeMonitor = context actorOf Props[TimeMonitor]
  val simpleEntities = for (_ <- 1 to 100) yield context actorOf Props[SimpleEntity]
  simpleEntities foreach(entity => contextService ! AddClassListener(entity, classOf[TimeMonitor]))

  override def receive = {
    case msg => println(msg)
  }
}