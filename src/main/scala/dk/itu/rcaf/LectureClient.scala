package dk.itu.rcaf

import scala.util.Random
import scala.concurrent.duration._
import akka.actor.{ActorRef, Actor, Props}
import dk.itu.rcaf.ContextClientConfig._
import dk.itu.rcaf.abilities._
import dk.itu.rcaf.simulator.Location

object LectureClientDemo extends App {
  val lectureClient = system.actorOf(Props[LectureClient])
  val teacher = system.actorOf(Props[Teacher])
  contextService ! AddClassListener(teacher, classOf[Student])
}

class LectureClient extends AbstractTimedMonitor(1 second) {
  val classRoom: ContextItem = Location("AUD2")

  override def run(): Unit = {
    val age = Random.nextInt(80)
    val name = Random.nextString(8)
    val awake = Random.nextBoolean()
    val studentRef = context actorOf Props(Student(name, age, awake))
    contextService ! AddContextItem(studentRef, classOf[Student], EnteredLocation, classRoom)
  }

  override def receive: Receive = {
    case msg => println(msg)
  }
}

object LectureMessages {
  val learningMsg = "Yay, I am learning!"
  val sleepMsg = "No, I am sleeping."
  val think = "Think"
  val ready = "Are you awake and of age?"
}

class Teacher extends Entity {
  var students: List[ActorRef] = Nil

  def clearScreen() = (1 to 30).foreach(_ => println("\n"))

  def printInfo() = {
    clearScreen()
    println(s"There are ${students.size} students awake at the lecture.")
  }

  override def receive = {
    case event: ContextEvent => event.entity ! LectureMessages.ready
    case LectureMessages.learningMsg =>
      students = sender :: students
      printInfo()
      students.foreach(_ ! LectureMessages.think)
    case LectureMessages.sleepMsg =>
      students = students.filter(_ != sender)
      printInfo()
  }
}

case class Student(name: String, age: Int, awake: Boolean) extends Entity {
  def fellAsleep() = Random.nextInt(4) == 0

  override def receive: Actor.Receive = {
    case LectureMessages.think => if (fellAsleep()) sender ! LectureMessages.sleepMsg
    case LectureMessages.ready =>
      if (age > 18 && awake) sender ! LectureMessages.learningMsg
      else sender ! LectureMessages.sleepMsg
  }
}