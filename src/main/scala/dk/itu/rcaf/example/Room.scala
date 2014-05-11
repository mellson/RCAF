package dk.itu.rcaf.example

import scala.swing.{FlowPanel, Label, Panel}
import java.awt.{Color, Graphics2D}
import dk.itu.rcaf.Person

case class Room(var persons: List[Person], roomName: String) extends FlowPanel {
  val personSize = 20

  val label = new Label { foreground = Color.white }

  contents += label

  override def paintComponent(g: Graphics2D) {
    g.setBackground(Color.BLACK)
    g.clearRect(0, 0, size.width, size.height)
    label.text = roomName + " : " + persons.size

    for (person <- persons) {
      g.setColor(person.moodColor)
      g.fillOval(person.x, person.y, personSize, personSize)
    }
  }
}
