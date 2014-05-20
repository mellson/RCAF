package dk.itu.rcaf.abilities

trait Relationship
trait TimedRelationship extends Relationship
case object EnteredLocation extends TimedRelationship
case object LeftLocation extends TimedRelationship