package dk.itu.rcaf.contextservice

import akka.actor.{ActorRef, Actor}
import dk.itu.rcaf.abilities._
import scala.collection.immutable.HashMap
import akka.actor.Stash

class ClientHandler extends Actor with Stash {
  var entityListeners: Map[ActorRef, List[ActorRef]] = HashMap.empty
  var classListeners: Map[Class[_], List[ActorRef]] = HashMap.empty

  var contexts: Map[ActorRef, Context] = HashMap.empty

  override def receive: Receive = {
    case AddContextItem(entity, clazz, relation, item) =>
      val context = Context(entity)
      context.setContextItem(relation, item)
      contexts = contexts ++ Map(entity.self -> context)
      self ! NotifyListeners(entity.self, clazz, ContextEvent(sender, relation, item))

    case NotifyListeners(subject, clazz, msg) =>
      entityListeners.filter(_._1 == subject).foreach(_._2.foreach(_ forward msg))
      classListeners.filter(_._1 == clazz).foreach(_._2.foreach(_ forward msg))

    case AddClassListener(listener, clazz) =>
      val listeners = classListeners getOrElse(clazz, Nil)
      classListeners = classListeners ++ Map(clazz -> (listener :: listeners))

    case RemoveClassListener(listener, clazz) =>
      classListeners get clazz match {
        case None =>
        case Some(listeners) =>
          classListeners = classListeners ++ Map(clazz -> listeners.filterNot(_ == listener))
      }

    case AddEntityListener(subject) =>
      val listeners = entityListeners getOrElse(subject, Nil)
      entityListeners = entityListeners ++ Map(subject -> (sender :: listeners))

    case RemoveEntityListener(subject) =>
      entityListeners get subject match {
        case None =>
        case Some(listeners) =>
          entityListeners = entityListeners ++ Map(subject -> listeners.filterNot(_ == sender))
      }

    case RemoveAllListeners =>
      entityListeners.filter(_._2.contains(sender)).foreach(x => self tell(RemoveEntityListener(x._1), sender))
      classListeners.filter(_._2.contains(sender)).foreach(x => self tell(RemoveClassListener(sender, x._1), sender))
  }
}
