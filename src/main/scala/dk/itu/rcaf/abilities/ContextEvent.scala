package dk.itu.rcaf.abilities

case class ContextEvent(entity: Entity, item: ContextItem, relation: Relationship, event_type: EventType)