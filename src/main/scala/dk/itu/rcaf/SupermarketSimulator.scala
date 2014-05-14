package dk.itu.rcaf

import akka.actor._
import dk.itu.rcaf.contextclient.TimeMonitor
import dk.itu.rcaf.abilities._
import dk.itu.rcaf.contextservice.RootHandler
import scala.swing._
import dk.itu.rcaf.abilities.AddClassListener
import scala.util.Random
import dk.itu.rcaf.example.Room

object SupermarketSimulator extends SimpleSwingApplication {
  val numberOfPersonsInRoom = 100
  lazy val rooms: List[Room] = List(
    new Room((for (i <- 1 to numberOfPersonsInRoom) yield new Person).toList, "Grønt"),
    new Room((for (i <- 1 to 0) yield new Person).toList, "Brød"),
    new Room((for (i <- 1 to 0) yield new Person).toList, "Kolonial"),
    new Room((for (i <- 1 to 0) yield new Person).toList, "Kasse"))

  def top = new MainFrame {
    title = "Superbrugsen Zimulator"
//    preferredSize = new Dimension(600, 400)
    preferredSize = maximumSize

    val squared = math.sqrt(rooms.size).toInt
    contents = new GridPanel(squared, squared) {
      for (room <- rooms)
        contents += room
    }
  }

  val system = ActorSystem("ActorSystem")
  val handler = system.actorOf(Props[RootHandler], "handler")

  val timeMonitor = system.actorOf(Props[TimeMonitor], "TimeMonitor")
  val guiActor = system.actorOf(Props[GuiUpdater], "GuiActor")
  //  val simpleActorEntity1 = system.actorOf(Props[SimpleActorEntity], "SimpleActorEntity1")

  //  handler tell(AddClassListener(simpleActorEntity1, classOf[TimeMonitor]), simpleActorEntity1)
  handler tell(AddClassListener(guiActor, classOf[TimeMonitor]), guiActor)
}

class GuiUpdater extends Entity {

  def exitRoom(person: Person, room: Room) = room.persons = room.persons.filter(_ != person)

  override def receive: Receive = {
    case msg =>
      Swing.onEDT {
        for {
          room <- SupermarketSimulator.rooms
          person <- room.persons
          if person.readyToMove
        } {
          val x = person.x + getRandomInt
          val y = person.y + 0
          val width = room.bounds.getWidth
//          val height = room.bounds.getHeight

          if (x <= 0)          moveToPreviousRoom(person, room)
          else if (x >= width) moveToNextRoom(person, room)
          else                 person.move(x, y)

//          if (getRandomInt == 0)
//            person.becomeAngry()
//          if (getRandomInt == 3)
//            person.becomeHappy()
        }
        SupermarketSimulator.rooms.foreach(_.repaint())
      }
  }

  def moveToNextRoom(person: Person, room: Room) {
    val nextRoomIndex = SupermarketSimulator.rooms.indexOf(room) + 1
    if (SupermarketSimulator.rooms.size > nextRoomIndex) {
      SupermarketSimulator.rooms(nextRoomIndex).persons = person :: SupermarketSimulator.rooms(nextRoomIndex).persons
      exitRoom(person, room)
      person.move(getRandomInt, 0)
    } else exitRoom(person, room)
  }

  def moveToPreviousRoom(person: Person, room: Room) {
    val previousRoomIndex = SupermarketSimulator.rooms.indexOf(room) - 1
    if (previousRoomIndex >= 0) {
      SupermarketSimulator.rooms(previousRoomIndex).persons = person :: SupermarketSimulator.rooms(previousRoomIndex).persons
      exitRoom(person, room)
      person.move(room.bounds.getWidth.toInt-person.size, 0)
    } else exitRoom(person, room)
  }

  def getRandomInt: Int = {
    val i = Random.nextInt(10)
    val b = Random.nextInt(3)
    if (b > 1) -1 * i else i
  }
}