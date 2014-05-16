package dk.itu.rcaf.abilities

import akka.actor.ActorRef

case class ContextEvent(entity: ActorRef, relation: Relationship, item: ContextItem)