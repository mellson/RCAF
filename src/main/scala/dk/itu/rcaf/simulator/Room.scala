package dk.itu.rcaf.simulator

import scala.swing.{FlowPanel, Label}
import java.awt.{Color, Graphics2D}

class EmptyRoom(roomName: String) extends Room(Nil, roomName)

case class Room(var persons: List[Person], roomName: String) extends FlowPanel {
  val label = new Label {
    foreground = Color.white
  }
  var productsInRoom = 100 // Percentile of how much product is in this room

  contents += label

  override def paintComponent(g: Graphics2D) {
    g.setBackground(Color.BLACK)
    g.clearRect(0, 0, size.width, size.height)
    label.text = s"${persons.size} persons in $roomName, $productsInRoom% goods left."

    for (person <- persons) {
      person.isReadyToBeDrawn = true



      g.setColor(person.moodColor)
      g.fillOval(person.x, person.y, person.size, person.size)
    }
  }
}
