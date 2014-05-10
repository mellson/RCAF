package dk.itu.rcaf

import scala.swing.Panel
import java.awt.{Graphics2D, Color}

class Person extends Panel {
  var centerColor = Color.WHITE
  var x, y = 0
  var personSize = 20

  override def paintComponent(g: Graphics2D) {
    g.setBackground(Color.black)

    // Start by erasing this Canvas
    g.clearRect(0, 0, size.width, size.height)

    // Draw background here
    g.setColor(centerColor)
    g.fillOval(x, y, personSize, personSize)
  }

  def move(x: Int, y: Int) {
    this.x = this.x + x
    this.y = this.y + y
    repaint()
  }

  def becomeAngry() {
    centerColor = Color.RED
  }

  def becomeHappy() {
    centerColor = Color.GREEN
  }
}