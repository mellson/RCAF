package dk.itu.rcaf.abilities

import akka.actor.ActorRef

case class AddClassListener(listener: ActorRef, clazz: Class[_])
