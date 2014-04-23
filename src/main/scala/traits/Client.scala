package traits

import akka.actor._
import traits.BlueToothReceiver
import akka.actor.SupervisorStrategy.{Restart, Stop, Decider}
import com.typesafe.config.ConfigFactory

object Client extends App {
  val config = ConfigFactory.load("frontend")

  val system = ActorSystem("frontend", config)

  val path = "akka.tcp://backend@0.0.0.0:2552/user/simple"

  val simpleActor = system.actorSelection(path)

  val bluetoothReceiver = BlueToothReceiver("bluetooth receiver")
  val wifiReceiver = WifiReceiver("wifi receiver")

  simpleActor ! "normal message 1"
  simpleActor ! bluetoothReceiver
  simpleActor ! "normal message 2"
  simpleActor ! wifiReceiver
}