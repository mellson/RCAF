package dk.itu.rcaf.simulator

import scala.swing.{FlowPanel, Label}
import java.awt.{Color, Graphics2D}

case class Room(roomName: String) extends FlowPanel {
  var open: Boolean = true
  // Is it possible to move in this room? Or are we maybe stuck in a queue.
  var productsInRoom = 100 // Percentile of how much product is in this room
  var workers: List[Worker] = Nil
  var customers: List[Customer] = Nil
  
  val label = new Label {
    foreground = Color.white
  }

  contents += label

  override def paintComponent(g: Graphics2D) {
    g.setBackground(Color.BLACK)
    g.clearRect(0, 0, size.width, size.height)
    label.text = s"${customers.size} persons in $roomName, $productsInRoom% goods left."

    for (person <- customers) {
      person.isReadyToBeDrawn = true
      if (person.money == 0)
        g.setColor(Color.CYAN)
      else
        g.setColor(person.moodColor)
      g.fillOval(person.x, person.y, person.size, person.size)
    }

    var xOffset = 0
    for (worker <- workers) {
      if (worker.working)
        g.setColor(worker.workColor)
      else
        g.setColor(worker.relaxedColor)
      g.fillOval(xOffset, worker.y, worker.size, worker.size)
      xOffset += 40
    }
  }
}
