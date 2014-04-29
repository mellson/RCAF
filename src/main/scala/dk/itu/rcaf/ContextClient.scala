package dk.itu.rcaf

import akka.actor._
import com.typesafe.config.ConfigFactory
import dk.itu.rcaf.contextclient.SimpleActorEntity
import dk.itu.rcaf.abilities._
import dk.itu.rcaf.example.TimeMonitor
import scala.concurrent.ExecutionContext.Implicits.global

object ContextClient extends App {
  val config = ConfigFactory.load("context_client.conf")
  val system = ActorSystem("ContextClient", config)
  val protocol = config.getString("backend.protocol")
  val systemName = config.getString("backend.system")
  val pathToContextServiceHandler = s"$protocol://$systemName@0.0.0.0:2552/user/handler"
  val contextService = system.actorSelection(pathToContextServiceHandler)

  val timeMonitor = system.actorOf(Props[TimeMonitor], "TimeMonitor")
  val simpleActorEntity1 = system.actorOf(Props[SimpleActorEntity], "SimpleActorEntity1")
  val simpleActorEntity2 = system.actorOf(Props[SimpleActorEntity], "SimpleActorEntity2")
  val simpleActorEntity3 = system.actorOf(Props[SimpleActorEntity], "SimpleActorEntity3")

  contextService tell(AddClassListener(simpleActorEntity1, classOf[TimeMonitor]), simpleActorEntity1)
  contextService tell(AddEntityListener(simpleActorEntity3), simpleActorEntity2)
  contextService tell(NotifyListeners(simpleActorEntity3, classOf[SimpleActorEntity]), simpleActorEntity3)

  import scala.concurrent.duration._
  system.scheduler.scheduleOnce(11 seconds, new NotifyRunner2)

  class NotifyRunner2 extends Runnable {
    override def run(): Unit = simpleActorEntity1 tell(RemoveAllListener, simpleActorEntity1)
  }
}