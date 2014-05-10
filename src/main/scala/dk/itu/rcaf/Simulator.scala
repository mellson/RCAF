package dk.itu.rcaf

import akka.actor._
import dk.itu.rcaf.contextclient.{SimpleActorEntity, TimeMonitor}
import dk.itu.rcaf.abilities._
import dk.itu.rcaf.contextservice.RootHandler
import scala.swing._
import java.awt.Color
import dk.itu.rcaf.abilities.AddClassListener
import scala.util.Random

object Simulator extends SimpleSwingApplication {
  lazy val label = new Label {
    text = "Hej"
    foreground = Color.white
  }

  lazy val persons: List[Person] = (for (i <- 1 to 10) yield new Person).toList

//  lazy val circle = new Ci

  def top = new MainFrame {
    title = "Superbrugsen Zimulator"
    preferredSize = maximumSize // new Dimension(700, 400)

//    val verticalBox = new BoxPanel(Orientation.Vertical)

    contents = new BoxPanel(Orientation.Horizontal) {
//      contents += label
      for (person <- persons)
        contents += person
//      border = Swing.EmptyBorder(20)
      background = Color.BLACK
    }
  }

  val system = ActorSystem("ActorSystem")
  val handler = system.actorOf(Props[RootHandler], "handler")
  handler ! Connect

  val timeMonitor = system.actorOf(Props[TimeMonitor], "TimeMonitor")
  val guiActor = system.actorOf(Props[GuiUpdater], "GuiActor")
  val simpleActorEntity1 = system.actorOf(Props[SimpleActorEntity], "SimpleActorEntity1")

  handler tell(AddClassListener(simpleActorEntity1, classOf[TimeMonitor]), simpleActorEntity1)
  handler tell(AddClassListener(guiActor, classOf[TimeMonitor]), guiActor)
}

class GuiUpdater extends Entity {
  override def receive: Receive = {
    case msg =>
      Swing.onEDT {
//        Simulator.label.text = Simulator.label.text + "."
        for (person <- Simulator.persons) {
          person.move(getRandomInt,getRandomInt)
          if (getRandomInt == 0)
            person.becomeAngry()
          if (getRandomInt == 3)
            person.becomeHappy()
        }
      }
  }

  def getRandomInt: Int = {
    val i = Random.nextInt(6)
    val b = Random.nextInt(3)
    if (b > 1) -1 * i else i
  }
}