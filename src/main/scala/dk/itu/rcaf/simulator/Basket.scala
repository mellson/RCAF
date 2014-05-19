package dk.itu.rcaf.simulator

trait Speed
case object Slow extends Speed
case object Fast extends Speed

class Basket {
  var room = Room("Entrance")
  var capacity = 15.0
  var speed: Speed = Slow
  def canHoldProduct(weightOfProduct: Float): Boolean = capacity - weightOfProduct >= 0
  def filled: Boolean = capacity - 3 < 0 // If we are within 3 KG's of the baskets capacity it's full
  def addProduct(product: SupermarketProduct) = capacity -= product.weight
}
