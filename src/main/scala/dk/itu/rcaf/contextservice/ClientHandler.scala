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
    case msg: AddContextItem =>
      val context = Context(msg.entity)
      context.setContextItem(msg.relation, msg.item)
      contexts = contexts ++ Map(msg.entity.self -> context)
      self ! NotifyListeners(msg.entity.self, msg.clazz, ContextEvent(sender, msg.relation, msg.item))

    case msg: NotifyListeners =>
      entityListeners.filter(_._1 == msg.subject).foreach(_._2.foreach(_ forward msg))
      classListeners.filter(_._1 == msg.clazz).foreach(_._2.foreach(_ forward msg))

    case msg: AddClassListener =>
      val listeners = classListeners getOrElse(msg.clazz, Nil)
      classListeners = classListeners ++ Map(msg.clazz -> (msg.listener :: listeners))
      msg.listener ! StartListening

    case msg: RemoveClassListener =>
      classListeners get msg.clazz match {
        case None =>
        case Some(listeners) =>
          classListeners = classListeners ++ Map(msg.clazz -> listeners.filterNot(_ == msg.listener))
          msg.listener ! StopListening
      }

    case msg: AddEntityListener =>
      val listeners = entityListeners getOrElse(msg.subject, Nil)
      entityListeners = entityListeners ++ Map(msg.subject -> (sender :: listeners))
      sender ! StartListening

    case msg: RemoveEntityListener =>
      entityListeners get msg.subject match {
        case None =>
        case Some(listeners) =>
          entityListeners = entityListeners ++ Map(msg.subject -> listeners.filterNot(_ == sender))
          sender ! StopListening
      }

    case RemoveAllListener =>
      entityListeners.filter(_._2.contains(sender)).foreach(x => self tell(RemoveEntityListener(x._1), sender))
      classListeners.filter(_._2.contains(sender)).foreach(x => self tell(RemoveClassListener(sender, x._1), sender))
  }
}
