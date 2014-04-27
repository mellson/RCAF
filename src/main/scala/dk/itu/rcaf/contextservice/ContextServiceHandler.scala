package dk.itu.rcaf.contextservice

import akka.actor.{ActorRef, Actor}
import dk.itu.rcaf.abilities._
import scala.collection.immutable.HashMap

class ContextServiceHandler extends Actor {
  var entityListeners: Map[ActorRef, List[ActorRef]] = HashMap.empty
  var classListeners: Map[Class[_], List[ActorRef]] = HashMap.empty
  var contexts: Map[ActorRef, Context] = HashMap.empty

  override def receive: Receive = {
    //    case event: ContextEvent => AddClassListener(new ActorEntity, this.getClass)

    case msg: AddContextItem =>
//      msg.
      self ! NotifyListeners(msg.entityRef, msg.clazz)

    case msg: NotifyListeners =>
      entityListeners.filter(_._1 == msg.subject).foreach(_._2.foreach(_ ! msg))
      classListeners.filter(_._1 == msg.clazz).foreach(_._2.foreach(_ ! msg))

    case msg: AddClassListener =>
      val listeners = classListeners getOrElse(msg.clazz, Nil)
      classListeners = classListeners ++ Map(msg.clazz -> (msg.listener :: listeners))

    case msg: RemoveClassListener =>
      classListeners get msg.clazz match {
        case None =>
        case Some(listeners) =>
          classListeners = classListeners ++ Map(msg.clazz -> listeners.filterNot(_ == msg.listener))
      }

    case msg: AddEntityListener =>
      val listeners = entityListeners getOrElse(msg.subject, Nil)
      entityListeners = entityListeners ++ Map(msg.subject -> (sender :: listeners))

    case msg: RemoveEntityListener =>
      entityListeners get msg.subject match {
        case None =>
        case Some(listeners) =>
          entityListeners = entityListeners ++ Map(msg.subject -> listeners.filterNot(_ == sender))
      }
  }
}
