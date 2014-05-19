package dk.itu.rcaf

import akka.actor._
import dk.itu.rcaf.abilities._
import scala.swing._
import scala.util.Random
import dk.itu.rcaf.simulator._
import scala.concurrent.duration._
import dk.itu.rcaf.contextservice.ContextServiceHandler
import scala.concurrent._
import dk.itu.rcaf.abilities.AbstractTimedMonitor
import scala.Some
import dk.itu.rcaf.abilities.AddEntityListener
import dk.itu.rcaf.simulator.Room

object SupermarketSimulator extends SimpleSwingApplication {
  lazy val rooms: List[Room] = List(
    new Room("Grønt"),
    new Room("Brød"),
    new Room("Kolonial"),
    new Room("Kasse"))

  rooms(3).open = false

  // Set the counter room to be closed / queued to start with

  def top = new MainFrame {
    title = "Superbrugsen Zimulator"
    preferredSize = new Dimension(600, 200)
    //    preferredSize = maximumSize

    val squared = math.sqrt(rooms.size).toInt
    contents = new GridPanel(squared, squared) {
      for (room <- rooms)
        contents += room
    }
  }

  def getCustomer: Customer = {
    val customer = new Customer
    customer.basket.room = rooms.head
    customer
  }

  def addCustomer() = rooms(0).customers = getCustomer :: rooms(0).customers

  addCustomer()
  val system = ActorSystem("ActorSystem")
  val simulatorContextService = system.actorOf(Props[ContextServiceHandler])
  val simUpdateActor = system.actorOf(Props[SimUpdater])
  val supermarketHandler = system.actorOf(Props[SuperMarketHandler])
  simulatorContextService.tell(AddEntityListener(simUpdateActor), supermarketHandler)
}

class SuperMarketHandler extends EntityListener {

  import SupermarketSimulator._

  def sensors: List[Basket] = rooms.flatMap(_.customers.map(_.basket))

  val numberOfWorkers = rooms.size * 2
  var workers: List[Worker] = (for (i <- 1 to numberOfWorkers) yield new Worker).toList

  import ExecutionContext.Implicits._

  def addMoreCustomers(): Future[Any] = Future {
    blocking(Thread.sleep(2000L)); addCustomer(); addMoreCustomers()
  }

  addMoreCustomers()

  def sendWorkerToRoom(room: Room) = {
    workers.find(!_.working) match {
      case None =>
      case Some(worker) =>
        room.workers = worker :: room.workers
        workers = workers.filter(_ ne worker)
    }
  }

  def freeFinishedWorkers() = {
    rooms.filter(_.workers.exists(!_.working)).foreach(room => room.workers.filterNot(_.working).foreach(worker => {
      room.workers = room.workers.filter(_ ne worker)
      workers = worker :: workers
    }))
  }

  override def receive: Actor.Receive = {
    case _ =>
      freeFinishedWorkers()
      // If someone is in a rush, make sure that there are workers at the checkout
      if (sensors.exists(_.speed == Fast))
        sendWorkerToRoom(rooms.last)

      // Send workers to the room where there are full baskets
      sensors.filter(_.filled).foreach(basket => sendWorkerToRoom(basket.room))

      rooms.foreach(sendWorkerToRoom)
  }
}

class SimUpdater extends AbstractTimedMonitor(interval = 50 milliseconds) {

  import SupermarketSimulator._

  override def receive: Receive = {
    case _ =>
      Swing.onEDT {
        for {
          room <- rooms
          person <- room.customers
          if person.isReadyToBeDrawn
        } {
          notifyListeners("Update", simulatorContextService)
          shop(person, room)
          move(person, room)
        }
        SupermarketSimulator.rooms.foreach(_.repaint())
      }
  }

  def exitRoom(person: Customer, room: Room) = room.customers = room.customers.filter(_ ne person)

  def shop(person: Customer, room: Room) = {
    if (room.productsInRoom < 100)
      room.workers.find(!_.working) match {
        case None =>
        case Some(worker) =>
          room.productsInRoom += 1
          worker.fillProduct(room)
      }

    val numberOfProductsToBuy = Random.nextInt(3) + 1 // Buy 1, 2 or 3 number of goods
    val products = for (_ <- 1 to numberOfProductsToBuy) yield new SupermarketProduct // Assign prices to each product
    products.foreach {
      product =>
        val readyToBuy = person.readyToBuy && room.productsInRoom > 0
        if (readyToBuy && person.money >= product.price && person.basket.canHoldProduct(product.weight)) {
          person.money -= product.price
          person.basket.addProduct(product)
          room.productsInRoom -= 1
        } else if (readyToBuy && (!person.basket.canHoldProduct(product.weight) || person.money <= product.price))
          person.basket.speed = Fast
    }
  }

  def move(person: Customer, room: Room) = {
    if (person.isBeingHandled || room.open || room.workers.exists(!_.working)) {
      if (!person.isBeingHandled && !room.open && room.workers.exists(!_.working)) {
        room.workers.find(!_.working) match {
          case None =>
          case Some(worker) => worker.handleCustomer(room)
        }
        person.isBeingHandled = true
      }

      val x = if (person.basket.speed == Slow) person.x + getRandomInt else person.x + getRandomInt + 20
      val y = person.y + 0
      val width = room.bounds.getWidth

      if (x <= 0 && rooms.indexOf(room) != 0) moveToPreviousRoom(person, room)
      else if (x >= width) moveToNextRoom(person, room)
      else person.move(x, y)
    }
  }

  def moveToNextRoom(person: Customer, room: Room) {
    val nextRoomIndex = SupermarketSimulator.rooms.indexOf(room) + 1
    if (SupermarketSimulator.rooms.size > nextRoomIndex) {
      SupermarketSimulator.rooms(nextRoomIndex).customers = person :: SupermarketSimulator.rooms(nextRoomIndex).customers
      exitRoom(person, room)
      person.move(getRandomInt, person.y)
    } else exitRoom(person, room)
  }

  def moveToPreviousRoom(person: Customer, room: Room) {
    val previousRoomIndex = SupermarketSimulator.rooms.indexOf(room) - 1
    if (previousRoomIndex >= 0) {
      SupermarketSimulator.rooms(previousRoomIndex).customers = person :: SupermarketSimulator.rooms(previousRoomIndex).customers
      exitRoom(person, room)
      person.move(room.bounds.getWidth.toInt - person.size, person.y)
    } else exitRoom(person, room)
  }

  def getRandomInt: Int = {
    val i = Random.nextInt(10)
    val b = Random.nextInt(3)
    if (b > 1) -1 * i else i
  }

  override def run(): Unit = self ! "Update simulation"
}