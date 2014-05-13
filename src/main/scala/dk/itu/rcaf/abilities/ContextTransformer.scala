package dk.itu.rcaf.abilities

trait ContextTransformer {
  def transform(contextItem: ContextItem): ContextItem
  def aggregate(contextItems: List[ContextItem]): ContextItem
}
