package dk.itu.rcaf.abilities

abstract class Relationship
abstract class TimedRelationship extends Relationship
case object EnteredLocation extends TimedRelationship
case object LeftLocation extends TimedRelationship