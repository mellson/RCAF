package dk.itu.rcaf.contextclient

import dk.itu.rcaf.abilities.AbstractTimedMonitor
import scala.concurrent.duration._

class BlipMonitor extends AbstractTimedMonitor(interval = 1 seconds) {
  override def run(): Unit = println(id)

  override def receive: Receive = {
    case x => println(x)
  }
}
