package dk.itu.rcaf.abilities

import dk.itu.rcaf.ContextClientConfig

trait ContextItem {
  val contextService = ContextClientConfig.contextService
  val id: String
}

import akka.serialization._

class ContextItemSerializer extends Serializer {
  def includeManifest: Boolean = false

  def identifier = 1234567

  def toBinary(obj: AnyRef): Array[Byte] = obj.asInstanceOf[ContextItem].toString.getBytes

  def fromBinary(bytes: Array[Byte], clazz: Option[Class[_]]): AnyRef = bytes.map(_.toChar).mkString
}
