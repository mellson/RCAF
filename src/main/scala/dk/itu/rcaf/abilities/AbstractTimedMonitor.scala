package dk.itu.rcaf.abilities

import akka.actor.Cancellable

import scala.concurrent.duration._

/**
 * Extension of the AbstractMonitor trait.
 * This extension provides the ability to schedule the run function at a timed interval.
 * @param interval the interval at which the run function should be called indefinitely.
 */
abstract case class AbstractTimedMonitor(interval: FiniteDuration, initialDelay: FiniteDuration = 0 seconds) extends AbstractMonitor {
  import context._
  private var scheduler: Cancellable = _

  override def preStart(): Unit = {

    scheduler = context.system.scheduler.schedule(
      initialDelay = initialDelay,
      interval = interval,
      this
    )
  }

  override def postStop(): Unit = {
    scheduler.cancel()
  }
}
