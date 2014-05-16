package dk.itu.rcaf.abilities

import dk.itu.rcaf.ContextClientConfig

trait ContextItem {
  val contextService = ContextClientConfig.contextService
  val id: String
}
