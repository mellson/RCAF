package dk.itu.rcaf.abilities

trait EventType
case object RELATIONSHIP_ADDED extends EventType
case object ITEM_REMOVED extends EventType
case object RELATIONSHIP_REMOVED extends EventType