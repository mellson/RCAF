package dk.itu.rcaf.abilities

case class AddContextItem(entity: Entity, clazz: Class[_], relation: Relationship, item: ContextItem)
