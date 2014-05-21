package dk.itu.rcaf.abilities

import akka.actor.ActorRef

case class AddContextItem(entity: ActorRef, clazz: Class[_], relation: Relationship, item: ContextItem)

case class RemoveContextItem(entity: ActorRef, clazz: Class[_], relation: Relationship, item: ContextItem)

case class AddClassListener(listener: ActorRef, clazz: Class[_])

case class AddEntityListener(subject: ActorRef)

case class RemoveEntityListener(subject: ActorRef)

case class RemoveClassListener(listener: ActorRef, clazz: Class[_])

case class NotifyListeners(subject: ActorRef, clazz: Class[_], msg: Any)

case object RemoveAllListeners

case class GetContext(subject: ActorRef)

case class GetEntity(name: String)
