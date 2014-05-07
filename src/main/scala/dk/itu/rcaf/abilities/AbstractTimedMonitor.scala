package dk.itu.rcaf.abilities

import scala.concurrent.duration._

/**
 * Extension of the AbstractMonitor trait.
 * This extension provides the ability to schedule the run function at a timed interval.
 * @param interval the interval at which the run function should be called indefinitely.
 */
abstract case class AbstractTimedMonitor(interval: FiniteDuration) extends AbstractMonitor {
  import context._

  /**
   * Create a recurring function by taking the run function from AbstractMonitor.
   * Call this function now and after that at the interval defined indefinitely.
   */
  system.scheduler.schedule(0 seconds, interval, new MonitorRunner(run))

  /**
   * Wrapper function that calls the provided function.
   * @param function this is the function you wish to be run indefinitely.
   */
  class MonitorRunner(function: () => Unit) extends Runnable {
    override def run(): Unit = function()
  }
}
