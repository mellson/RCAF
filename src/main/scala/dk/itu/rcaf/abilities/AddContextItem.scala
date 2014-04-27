package dk.itu.rcaf.abilities

import akka.actor.ActorRef

case class AddContextItem(entityRef: ActorRef, clazz: Class[_], relation: Relationship, item: ContextItem)
