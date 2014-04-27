package dk.itu.rcaf.contextclient

import dk.itu.rcaf.abilities.ContextItem

case class Person(id: String, name: String) extends ContextItem

case class Location(id: String, name: String) extends ContextItem