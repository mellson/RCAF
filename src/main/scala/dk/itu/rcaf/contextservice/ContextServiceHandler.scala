package dk.itu.rcaf.contextservice

import akka.actor.{ActorRef, Props, Actor}
import dk.itu.rcaf.abilities._
import dk.itu.rcaf.contextclient.SimpleActorEntity
import scala.collection.immutable.HashMap

class ContextServiceHandler extends Actor {
  var entityListeners: Map[String, List[ActorRef]] = HashMap.empty
  var classListeners: Map[Class[_], List[ActorRef]] = HashMap.empty


  override def receive: Receive = {
//    case event: ContextEvent => AddClassListener(new ActorEntity, this.getClass)

    case notifyListeners: NotifyListeners =>
      for { (id, listeners) <- entityListeners
        if id == notifyListeners.entityId
      } listeners foreach(_ ! notifyListeners)

      for { (clazz, listeners) <- classListeners
            if clazz == notifyListeners.clazz
      } listeners foreach(_ ! notifyListeners)

    case addClassListener: AddClassListener =>
      val clazz = addClassListener.clazz
      val listenerRef = addClassListener.listener
      classListeners get clazz match {
        case None => classListeners = classListeners ++ Map(clazz -> List(listenerRef))
        case Some(listeners) => classListeners = classListeners ++ Map(clazz -> (listenerRef :: listeners))
      }

    case addEntityListener: AddEntityListener =>
      val entityId = addEntityListener.entityId
      val listenerRef = addEntityListener.listener
      entityListeners get entityId match {
        case None => entityListeners = entityListeners ++ Map(entityId -> List(listenerRef))
        case Some(listeners) => entityListeners = entityListeners ++ Map(entityId -> (listenerRef :: listeners))
      }
  }
}
