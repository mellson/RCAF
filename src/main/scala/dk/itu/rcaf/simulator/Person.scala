package dk.itu.rcaf.simulator

import java.awt.Color
import scala.util.Random

trait Direction
case object Forward extends Direction
case object Backward extends Direction
case object Up extends Direction
case object Down extends Direction

class Person() {
  var isReadyToBeDrawn = false // This is set to false until the room tries to draw the person
  val size = 20
  var moodColor = Color.WHITE
  var x, y = 0
  var readyToBuy = false
  var direction: Direction = Forward

  var money = Random.nextInt(30)
  var capacity = Random.nextInt(50)


  def move(x: Int, y: Int) {
    this.x = x
    this.y = y
  }

  def becomeAngry() {
    moodColor = Color.RED
    readyToBuy = false
  }


  def becomeHappy() {
    moodColor = Color.GREEN
    readyToBuy = true
  }
}