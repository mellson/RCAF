package dk.itu.rcaf.abilities

import scala.collection.immutable.Iterable
import akka.actor.ActorRef

case class Context(entity: ActorRef) {
  private var context: Map[Relationship, ContextItem] = Map.empty

  def setContextItem(relation: Relationship, item: ContextItem) = context = context ++ Map(relation -> item)

  def removeContextItem(relation: Relationship) = context = context - relation

  def removeContextItem(item: ContextItem) = context = context.filterNot(_._2 == item)

  def getContextItem(relation: Relationship): Option[ContextItem] = context.get(relation)

  def getContextItems: Iterable[ContextItem] = context.map(_._2)

  def contains(item: ContextItem): Boolean = !context.filter(_._2 == item).isEmpty

  def contains(relation: Relationship): Boolean = context.contains(relation)
}
