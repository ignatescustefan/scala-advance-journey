package lectures.part5typesystems

object PathDependentTypes extends App {

  class Outer {
    class Inner

    object InnerObject

    type InnerType

    def print(i: Inner): Unit = println(i)

    def printGeneral(i: Outer#Inner): Unit = println(i)
  }

  def aMethod: Int = {
    class HelperClass
    type HelpType = String
    2
  }

  // per-instance

  val outer = new Outer
  val inner = new outer.Inner // outer.Inner is a type

  val oo = new Outer

  // val otherInner: oo.Inner = new outer.Inner

  outer.print(inner)
  // oo.print(inner)

  // Outer#Inner
  outer.printGeneral(inner)
  oo.printGeneral(inner)

  /*
    Exercise

    db keyed by Int or String, but maybe others
  */

  /*
    use path-dependent types
    abstract type members and/or type aliases
   */

  trait ItemLike {
    type Key
  }

  trait Item[K] extends ItemLike {
    override type Key = K
  }

  trait IntItem extends Item[Int]

  trait StringItem extends Item[String]

  def get[ItemType <: ItemLike](key: ItemType#Key): ItemType = ???

  get[IntItem](42) // ok
  get[StringItem]("home") // OK
  // get[IntItem]("home") // not OK
}
