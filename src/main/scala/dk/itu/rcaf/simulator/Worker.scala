package dk.itu.rcaf.simulator

import java.awt.Color
import scala.concurrent._
import ExecutionContext.Implicits._
import scala.util.Random

class Worker {
  def startWorking(time: Long, room: Room) {
    working = true
    Future { blocking(Thread.sleep(time)); working = false }
  }

  def handleCustomer(room: Room) {
    val timeToDoWork = (Random.nextInt(5) + 1) * 1000
    startWorking(timeToDoWork, room)
  }

  def fillProduct(room: Room) {
    val timeToDoWork = (Random.nextInt(10) + 1) * 100
    startWorking(timeToDoWork, room)
  }

  var working = false
  val size = 15
  val workColor = Color.RED
  var relaxedColor = Color.GREEN
  var x = 0
  var y = 60
}
