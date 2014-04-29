package dk.itu.rcaf.example

import dk.itu.rcaf.abilities._
import scala.concurrent.duration._

class TimeMonitor extends Entity {
  import context._

  system.scheduler.schedule(2 seconds, 2 seconds, new NotifyRunner)

  class NotifyRunner extends Runnable {
    override def run(): Unit = notifyListeners()
  }

  override def receive: Receive = {
    case msg => println(msg)
  }
}