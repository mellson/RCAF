package dk.itu.rcaf.contextclient

import akka.actor.{Props, Actor}
import dk.itu.rcaf.ContextClient.contextService
import dk.itu.rcaf.abilities._
import scala.concurrent.duration._

class ClientActor extends Actor  {
  val timeMonitor = context actorOf Props[TimeMonitor]
  val simpleEntity = context actorOf Props[SimpleEntity]
  contextService ! AddClassListener(simpleEntity, classOf[TimeMonitor])

  override def receive = {
    case msg => println(msg)
  }
}

class TimeMonitor extends AbstractTimedMonitor(interval = 2 seconds) {
  override def receive: Receive = {
    case Run => run()
  }

  override def run(): Unit = notifyListeners()
}

class SimpleEntity extends Entity with EntityListener