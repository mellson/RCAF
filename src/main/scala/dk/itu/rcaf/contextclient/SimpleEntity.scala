package dk.itu.rcaf.contextclient

import dk.itu.rcaf.abilities._

class SimpleEntity extends Entity with EntityListener {
//  getContext.setContextItem(Entered, this)
  override def listen: Receive = { case msg => println(msg) }
}