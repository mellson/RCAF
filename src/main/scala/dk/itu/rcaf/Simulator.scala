package dk.itu.rcaf

import akka.actor._
import dk.itu.rcaf.contextclient.{SimpleActorEntity, TimeMonitor}
import dk.itu.rcaf.abilities._
import dk.itu.rcaf.contextservice.RootHandler
import scala.swing._
import java.awt.Color
import dk.itu.rcaf.abilities.AddClassListener
import scala.util.Random
import dk.itu.rcaf.example.Room

object Simulator extends SimpleSwingApplication {
  val numberOfPersonsInRoom = 10
  lazy val rooms: List[Room] = List(
    new Room((for (i <- 1 to numberOfPersonsInRoom) yield new Person).toList, "Grønt"),
    new Room((for (i <- 1 to numberOfPersonsInRoom) yield new Person).toList, "Brød"),
    new Room((for (i <- 1 to numberOfPersonsInRoom) yield new Person).toList, "Kolonial"),
    new Room((for (i <- 1 to numberOfPersonsInRoom) yield new Person).toList, "Kasse"))

  def top = new MainFrame {
    title = "Superbrugsen Zimulator"
    preferredSize = maximumSize // new Dimension(700, 400)

    val squared = math.sqrt(rooms.size).toInt
    contents = new GridPanel(squared,squared) {
      for (room <- rooms)
        contents += room
    }
  }

  val system = ActorSystem("ActorSystem")
  val handler = system.actorOf(Props[RootHandler], "handler")
  handler ! Connect

  val timeMonitor = system.actorOf(Props[TimeMonitor], "TimeMonitor")
  val guiActor = system.actorOf(Props[GuiUpdater], "GuiActor")
//  val simpleActorEntity1 = system.actorOf(Props[SimpleActorEntity], "SimpleActorEntity1")

//  handler tell(AddClassListener(simpleActorEntity1, classOf[TimeMonitor]), simpleActorEntity1)
  handler tell(AddClassListener(guiActor, classOf[TimeMonitor]), guiActor)
}

class GuiUpdater extends Entity {
  override def receive: Receive = {
    case msg =>
      Swing.onEDT {
        for (room <- Simulator.rooms) {
          if (!room.showing) // TODO Dont move persons before the graphics is on screen
            println("not yet")
          else {
            for (person <- room.persons) {
              val x = person.x + getRandomInt
              val y = person.y + 0
              println(s"person x:${person.x}, y:${person.y} - in room x:${room.bounds.getX}, y:${room.bounds.getY}")
              val width = room.bounds.getWidth
              val height = room.bounds.getHeight
              if (x >= width) {
                room.persons = room.persons diff List(person)
                val index = Simulator.rooms.indexOf(room)
                if (Simulator.rooms.size > index + 1) {
                  Simulator.rooms(index + 1).persons =  person :: Simulator.rooms(index).persons
                  person.move(getRandomInt,0)
                }
              } else
                person.move(x,y)
              if (getRandomInt == 0)
                person.becomeAngry()
              if (getRandomInt == 3)
                person.becomeHappy()
              room.repaint()
            }
          }
        }
      }
  }

  def getRandomInt: Int = {
    val i = Random.nextInt(10)
    val b = Random.nextInt(3)
    if (b > 1) -1 * i else i
  }
}