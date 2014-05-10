package dk.itu.rcaf.contextclient

import dk.itu.rcaf.abilities._
import scala.concurrent.duration._

class TimeMonitor extends AbstractTimedMonitor(interval = 200 milliseconds) {
  override def receive: Receive = {
    case msg => println(msg)
  }

  override def run(): Unit = notifyListeners()
}