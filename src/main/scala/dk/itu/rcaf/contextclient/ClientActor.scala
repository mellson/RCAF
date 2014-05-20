package dk.itu.rcaf.contextclient

import akka.actor.Actor
import dk.itu.rcaf.abilities.AbstractMonitor
import akka.actor.Actor.Receive

class ClientActor extends Actor  {

  override def receive = {
    case msg => println(msg)
  }
}

class ContextMonitor extends AbstractMonitor {
  override def receive: Receive = ???

  override def run(): Unit = ???
}