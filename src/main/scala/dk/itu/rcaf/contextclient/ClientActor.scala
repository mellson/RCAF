package dk.itu.rcaf.contextclient

import akka.actor.{ActorSystem, Props, Actor}
import dk.itu.rcaf.ContextClient.contextService
import dk.itu.rcaf.abilities._
import spray.http._
import spray.client.pipelining._
import scala.concurrent.Future
import dk.itu.rcaf.GuiActor

class ClientActor extends Actor  {
  contextService ! Connect

  val timeMonitor = context.actorOf(Props[TimeMonitor], "TimeMonitor")
  val guiActor = context.actorOf(Props[GuiActor], "GuiActor")
  val simpleActorEntity1 = context.actorOf(Props[SimpleActorEntity], "SimpleActorEntity1")
  val simpleActorEntity2 = context.actorOf(Props[SimpleActorEntity], "SimpleActorEntity2")
  val simpleActorEntity3 = context.actorOf(Props[SimpleActorEntity], "SimpleActorEntity3")

  contextService tell(AddClassListener(simpleActorEntity1, classOf[TimeMonitor]), simpleActorEntity1)
  contextService tell(AddClassListener(simpleActorEntity1, classOf[TimeMonitor]), guiActor)
  contextService tell(AddEntityListener(simpleActorEntity3), simpleActorEntity2)
  contextService tell(NotifyListeners(simpleActorEntity3, classOf[SimpleActorEntity]), simpleActorEntity3)

  implicit val system = ActorSystem()
  import system.dispatcher
  val pipeline: HttpRequest => Future[HttpResponse] = sendReceive
  val response: Future[HttpResponse] = pipeline(Get("http://google.dk/"))
  response.map(x => println(x))

  override def receive: Receive = {
    case msg => println("client received " + msg)
  }
}
