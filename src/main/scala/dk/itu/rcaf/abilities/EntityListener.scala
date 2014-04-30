package dk.itu.rcaf.abilities

import akka.actor.Actor

case object StartListening
case object StopListening

trait EntityListener extends Actor with ContextItem {
  import context._

  def receive: Receive = {
    case StartListening => become(listen)
  }

  def listen: Receive = {
    case msg: NotifyListeners => println(s"Monitor $id got msg from ${msg.subject.path.name}")
    case StopListening => become(receive)
    case RemoveAllListener => contextService ! RemoveAllListener
  }
}