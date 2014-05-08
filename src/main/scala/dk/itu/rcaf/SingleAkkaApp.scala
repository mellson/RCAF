package dk.itu.rcaf

import akka.actor._
import dk.itu.rcaf.contextclient.{SimpleActorEntity, TimeMonitor}
import dk.itu.rcaf.abilities._
import dk.itu.rcaf.contextservice.RootHandler
import scala.swing._
import java.awt.Color
import dk.itu.rcaf.abilities.AddClassListener

object SingleAkkaApp extends SimpleSwingApplication {
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

  val system = ActorSystem("ActorSystem")
  val handler = system.actorOf(Props[RootHandler], "handler")
  handler ! Connect

  val timeMonitor = system.actorOf(Props[TimeMonitor], "TimeMonitor")
  val guiActor = system.actorOf(Props[GuiActor], "GuiActor")
  val simpleActorEntity1 = system.actorOf(Props[SimpleActorEntity], "SimpleActorEntity1")

  handler tell(AddClassListener(simpleActorEntity1, classOf[TimeMonitor]), simpleActorEntity1)
  handler tell(AddClassListener(guiActor, classOf[TimeMonitor]), guiActor)
}

class GuiActor extends Entity {
  override def receive: Receive = {
    case msg => {
      println(msg.toString)
      Swing.onEDT {
        SingleAkkaApp.label.text = SingleAkkaApp.label.text + "."
      }
    }
  }
}