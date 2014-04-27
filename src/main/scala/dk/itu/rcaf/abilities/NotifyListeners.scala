package dk.itu.rcaf.abilities

import akka.actor.ActorRef

case class NotifyListeners(subject: ActorRef, clazz: Class[_])
