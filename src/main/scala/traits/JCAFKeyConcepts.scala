package traits

class JCAFKeyConcepts {
  trait Entity // Person, thing, place etc.
  trait Relationship // uses, arrives, leaves etc.
  trait ContextItem // pc, coffeecup etc.
  trait ContextEvent // pc, coffeecup etc.
  trait ContextService // A service receive, manage, store, and distribute context information for entities.
  trait ContextClient // Context clients can submit context information and can listen to changes in con- text information for entities.
  trait ContextMonitor extends ContextClient // Context clients that specialize in sensing, resolving, and submitting context information is often called context monitors.
  trait ContextActuator extends ContextClient // Context clients which are specialized in using context information is often called context actuators.
  trait EntityListener extends ContextClient // A context service allow special context client (entity listeners) to register interest in events in specific entities and receive a notification of the occurrence of such an event.
  trait TypebasedSubscription // TODO - skal nok flyttes til EntityListener. enabling entity listeners to subscribe to types of entities, like all patients, persons, or places.
}
