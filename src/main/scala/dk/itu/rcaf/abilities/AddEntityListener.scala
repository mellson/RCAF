package dk.itu.rcaf.abilities

import akka.actor.ActorRef

case class AddEntityListener(listener: ActorRef, entityId: String)
