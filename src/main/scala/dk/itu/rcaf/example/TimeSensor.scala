package dk.itu.rcaf.example

import dk.itu.rcaf.abilities._
import dk.itu.rcaf.ContextClient._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class TimeSensor extends Entity {
  system.scheduler.schedule(0 seconds, 2 seconds, new NotifyRunner)
  contextService ! AddClassListener(self, getClass)

  override def receive: Receive = {
    case msg => println(msg)
  }

  class NotifyRunner extends Runnable {
    override def run(): Unit = contextService ! NotifyListeners(self, classOf[TimeSensor])
  }
}