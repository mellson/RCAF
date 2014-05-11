package dk.itu.rcaf.abilities

import scala.concurrent.duration._
import akka.actor.Cancellable

/**
 * Extension of the AbstractMonitor trait.
 * This extension provides the ability to schedule the run function at a timed interval.
 * @param interval the interval at which the run function should be called indefinitely.
 */
abstract case class AbstractTimedMonitor(interval: FiniteDuration) extends AbstractMonitor {
  import context._

  private var scheduler: Cancellable = _

  override def preStart(): Unit = {
    import scala.concurrent.duration._
    scheduler = context.system.scheduler.schedule(
      initialDelay = 0 seconds,
      interval = interval,
      receiver = self,
      message = Run
    )
  }

  override def postStop(): Unit = {
    scheduler.cancel()
  }
}
