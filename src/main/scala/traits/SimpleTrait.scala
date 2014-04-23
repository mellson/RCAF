package traits

trait Named {
  val name: String
}

trait Entity extends Named

case class BlueToothReceiver(name: String) extends Entity

case class WifiReceiver(name: String) extends Entity
