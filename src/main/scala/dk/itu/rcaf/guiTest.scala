package dk.itu.rcaf

import scala.swing._
import java.awt.Color
import rx.Observable
import scala.concurrent.duration._
import java.lang.Long
import rx.functions.Action1

object guiTest extends SimpleSwingApplication {
  def top = new MainFrame {
    title = "Superbrugsen Simulator"
    size = maximumSize

    val label = new Label {
      text = "Hej"
      foreground = Color.white
    }

    val boxPanel = new BoxPanel(Orientation.Vertical) {
      contents += label
      border = Swing.EmptyBorder(20)
      background = Color.BLACK
    }

    contents = boxPanel

    val o: Observable[Long] = Observable.interval(200, MILLISECONDS).take(50)
    o subscribe new Action1[Long] {
      override def call(t1: Long): Unit = {
        label.text = label.text + "."
      }
    }
  }
}