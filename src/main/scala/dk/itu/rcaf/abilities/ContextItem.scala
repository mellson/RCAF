package dk.itu.rcaf.abilities

import dk.itu.rcaf.ContextClient

trait ContextItem {
  val contextService = ContextClient.contextService
  val id: String
}
