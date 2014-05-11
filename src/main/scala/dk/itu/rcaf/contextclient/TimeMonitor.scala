package dk.itu.rcaf.contextclient

import dk.itu.rcaf.abilities._
import scala.concurrent.duration._

class TimeMonitor extends AbstractTimedMonitor(interval = 50 milliseconds) {
  override def receive: Receive = {
    case Run => run()
  }

  override def run(): Unit = notifyListeners()
}