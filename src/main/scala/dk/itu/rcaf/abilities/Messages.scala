package dk.itu.rcaf.abilities

import akka.actor.ActorRef

case object Run

case object StartListening

case object StopListening

case object RemoveAllListener

case class AddContextItem(entity: Entity, clazz: Class[_], relation: Relationship, item: ContextItem)

case class AddClassListener(listener: ActorRef, clazz: Class[_])

case class AddEntityListener(subject: ActorRef)

case class RemoveEntityListener(subject: ActorRef)

case class RemoveClassListener(listener: ActorRef, clazz: Class[_])

case class NotifyListeners(subject: ActorRef, clazz: Class[_], event: ContextEvent)
