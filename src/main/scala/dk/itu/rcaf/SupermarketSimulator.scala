package dk.itu.rcaf

import akka.actor._
import dk.itu.rcaf.abilities._
import dk.itu.rcaf.contextservice.RootHandler
import scala.swing._
import dk.itu.rcaf.abilities.AddClassListener
import scala.util.Random
import dk.itu.rcaf.simulator.{SimProduct, EmptyRoom, Person, Room}
import scala.concurrent.duration._

object SupermarketSimulator extends SimpleSwingApplication {
  val numberOfPersonsInRoom = 10
  lazy val rooms: List[Room] = List(
    new Room((for (i <- 1 to numberOfPersonsInRoom) yield new Person).toList, "Grønt"),
    new EmptyRoom("Brød"),
    new EmptyRoom("Kolonial"),
    new EmptyRoom("Kasse"))

  def top = new MainFrame {
    title = "Superbrugsen Zimulator"
    preferredSize = new Dimension(600, 400)
    //    preferredSize = maximumSize

    val squared = math.sqrt(rooms.size).toInt
    contents = new GridPanel(squared, squared) {
      for (room <- rooms)
        contents += room
    }
  }

  val system = ActorSystem("ActorSystem")
  val handler = system.actorOf(Props[RootHandler], "handler")
  val simUpdateActor = system.actorOf(Props[SimUpdater], "SimUpdateActor")
}

class SimUpdater extends AbstractTimedMonitor(interval = 50 milliseconds) {

  import SupermarketSimulator._

  override def receive: Receive = {
    case msg =>
      Swing.onEDT {
        for {
          room <- rooms
          person <- room.persons
          if person.isReadyToBeDrawn
        } {
          shop(person, room)
          move(person, room)
          changeMood(person)
        }
        SupermarketSimulator.rooms.foreach(_.repaint())
      }
  }

  def changeMood(person: Person) = {
    if (getRandomInt == 0)
      person.becomeAngry()
    if (getRandomInt == 3)
      person.becomeHappy()
  }

  def exitRoom(person: Person, room: Room) = room.persons = room.persons.filter(_ ne person)

  def shop(person: Person, room: Room) = {
    val numberOfProductsToBuy = Random.nextInt(3) + 1 // Buy 1, 2 or 3 number of goods
    val products = for (_ <- 1 to numberOfProductsToBuy) yield new SimProduct // Assign prices to each product
    products.foreach {
      product =>
        if (person.readyToBuy &&
          person.money >= product.price &&
          room.productsInRoom > 0 &&
          person.capacity >= product.weight) {
          person.money -= product.price
          room.productsInRoom -= 1
          person.becomeHappy()
        } else person.becomeAngry()
    }
  }

  def move(person: Person, room: Room) = {
    val x = person.x + getRandomInt
    val y = person.y + 1
    val width = room.bounds.getWidth
    val height = room.bounds.getHeight

    if (x <= 0 && rooms.indexOf(room) != 0) moveToPreviousRoom(person, room)
    else if (x >= width) moveToNextRoom(person, room)
    else person.move(x, y)
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
      person.move(room.bounds.getWidth.toInt - person.size, 0)
    } else exitRoom(person, room)
  }

  def getRandomInt: Int = {
    val i = Random.nextInt(10)
    val b = Random.nextInt(3)
    if (b > 1) -1 * i else i
  }

  override def run(): Unit = self ! "Update simulation"
}