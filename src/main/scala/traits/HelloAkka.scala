package traits

import akka.actor.{Actor, Props, ActorSystem}

object HelloAkka extends App {
  val system = ActorSystem("HelloAkkaSystem")
  val simpleActor = system.actorOf(Props[SimpleActor], name = "simpleActor")

  val bluetoothReceiver = BlueToothReceiver("bluetooth receiver")

  simpleActor ! bluetoothReceiver
  simpleActor ! "hello"
}

trait Named {
  val name: String
}
trait Entity extends Named

case class BlueToothReceiver(name: String) extends Entity
case class WifiReceiver(name: String) extends Entity

class SimpleActor extends Actor {
  def receive = {
    case e: Entity => println(e.name) // TODO virker dette på tværs af JVM'er? Prøv at start et Akka system op og senere tilføj en ny case class der arver fra Entity kan denne sendes til den instans der allerede kører?
    case _         => println("Did not understand that message!")
  }
}
