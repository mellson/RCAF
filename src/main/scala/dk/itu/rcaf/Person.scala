package dk.itu.rcaf

import java.awt.Color

trait Direction
case object Forward extends Direction
case object Backward extends Direction
case object Up extends Direction
case object Down extends Direction

class Person {
  val size = 20
  var moodColor = Color.WHITE
  var x, y = 0
  var readyToMove = false // This is set to false until the room tries to draw the person
  var direction: Direction = Forward

  def move(x: Int, y: Int) {
    this.x = x
    this.y = y
  }

  def becomeAngry() {
    moodColor = Color.RED
  }


  def becomeHappy() {
    moodColor = Color.GREEN
  }
}