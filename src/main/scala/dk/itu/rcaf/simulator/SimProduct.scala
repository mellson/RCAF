package dk.itu.rcaf.simulator

import scala.util.Random

class SimProduct {
  lazy val price = Random.nextInt(20) + 1
  lazy val weight = Random.nextInt(20) + 1
}
