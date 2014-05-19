package dk.itu.rcaf.simulator

import java.awt.Color
import scala.util.Random

class Customer {
  var isReadyToBeDrawn = false // This is set to false until the room tries to draw the person

  val size = 20
  var moodColor = Color.WHITE
  var x = 0
  var y = 30
  def readyToBuy = Random.nextInt(10) == 7
  var isBeingHandled = false // Indicates if this person has been served at the counter
  val basket = new Basket

  var money = Random.nextInt(300)

  
  def move(x: Int, y: Int) {
    this.x = x
    this.y = y
  }
}