package dk.itu.rcaf.contextclient

import akka.actor.Props
import dk.itu.rcaf.abilities.{AbstractTimedMonitor, AddClassListener, Entity}

import scala.concurrent.duration._

class ClientActor extends Entity  {
  val timeMonitor = context.actorOf(Props[TimeMonitor], name = "TimeMonitor")
  val simpleEntity = context.actorOf(Props[SimpleEntity], name = "SimpleEntity")

  contextService ! AddClassListener(simpleEntity, classOf[TimeMonitor])

  override def receive = {
    case msg => println(s"$msg, caught by ClientActor")
  }
}

class TimeMonitor extends AbstractTimedMonitor(1 second, 5 seconds) {
  var counter = 0

  override def run() = {
    notifyListeners(s"Input from sensor = $counter")
    counter += 1
  }

  override def receive = {
    case msg => println(s"$msg, caught by TimeMonitor")
  }
}

class SimpleEntity extends Entity {
  override def receive = {
    case msg => println(s"$msg, caught by SimpleEntity - sent by ${sender.path.name}")
  }
}