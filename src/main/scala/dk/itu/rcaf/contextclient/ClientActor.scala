package dk.itu.rcaf.contextclient

import akka.actor.Props
import dk.itu.rcaf.abilities.{AbstractTimedMonitor, Entity, AddClassListener}
import scala.concurrent.duration._

class ClientActor extends Entity  {
  val timeMonitor = context actorOf Props[TimeMonitor]
  val simpleEntity = context actorOf Props[SimpleEntity]
  contextService ! AddClassListener(simpleEntity, classOf[TimeMonitor])
  override def receive = { case msg => println(msg)
  }
}

class TimeMonitor extends AbstractTimedMonitor(1 second) {
  override def run() = notifyListeners("Hello")

  override def receive = { case msg => println(msg) }
}

class SimpleEntity extends Entity {
  override def receive = { case msg => println(msg) }
}