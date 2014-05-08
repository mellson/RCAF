package dk.itu.rcaf.contextclient

import dk.itu.rcaf.abilities.Entity
import java.awt.Color
import scala.swing._
import dk.itu.rcaf.SingleAkkaApp

object Simulator extends SimpleSwingApplication {
  lazy val label = new Label {
    text = "Hej"
    foreground = Color.white
  }

  def top = new MainFrame {
    title = "Superbrugsen Simulator"
    size = maximumSize

    contents = new BoxPanel(Orientation.Vertical) {
      contents += label
      border = Swing.EmptyBorder(20)
      background = Color.BLACK
    }
  }

  SingleAkkaApp
}