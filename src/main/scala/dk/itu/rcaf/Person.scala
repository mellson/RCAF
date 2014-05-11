package dk.itu.rcaf

import java.awt.Color

class Person {
  var moodColor = Color.WHITE
  var x, y = 0

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