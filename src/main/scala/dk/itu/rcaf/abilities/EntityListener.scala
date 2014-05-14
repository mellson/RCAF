package dk.itu.rcaf.abilities

import akka.actor.Actor

trait EntityListener extends Actor with ContextItem {
  import context._

  def receive: Receive = {
    case StartListening => become(listen)
  }

  def listen: Receive = {
    case msg: NotifyListeners => println(s"$id was notified by ${msg.subject.path.name}")
    case StopListening => become(receive)
    case RemoveAllListener => contextService ! RemoveAllListener
  }
}
