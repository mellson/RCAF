package dk.itu.rcaf.abilities

import akka.actor.ActorRef

case class RemoveClassListener(listener: ActorRef, clazz: Class[_])
