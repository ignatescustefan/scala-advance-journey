package lectures.part4implicits

object OrganizingImplicits extends App {

  implicit val reverseOrdering: Ordering[Int] = Ordering.fromLessThan(_ > _)
  //  implicit val normalOrdering: Ordering[Int] = Ordering.fromLessThan(_ > _)
  println(List(1, 4, 3, 2, 5).sorted)

  // scala.Predef
  /*
    Implicits (used as implicit parameters):
     - val/var
     - object
     - accessor methods = defs with no parentheses
   */

  // Exercise
  case class Person(name: String, age: Int)

  val person = List(
    Person("Steve", 30),
    Person("Amy", 40),
    Person("John", 40)
  )

  //  object SomeObject {
  //    implicit val alphabeticallyOrdering: Ordering[Person] = Ordering.fromLessThan((a, b) => a.name.compareTo(b.name) < 0)
  //  }

  //  object Person {
  //    implicit val alphabeticallyOrdering: Ordering[Person] = Ordering.fromLessThan((a, b) => a.name.compareTo(b.name) < 0)
  //  }
  object AlphabeticallyNameOrdering {
    implicit val alphabeticallyOrdering: Ordering[Person] = Ordering.fromLessThan((a, b) => a.name.compareTo(b.name) < 0)
  }

  object AgeOrdering {
    implicit val ageOrdering: Ordering[Person] = Ordering.fromLessThan((a, b) => a.age < b.age)
  }

  import AlphabeticallyNameOrdering._

  println(person.sorted)
  /*
    Implicit scope
    - normal scope = Local scope
    - imported scope
    - companions of all types involved in the method signature
      - List
      - Ordering
      - all the types involved = A or any supertype
   */

  // def sorted[B >: A](implicit ord: Ordering[B]): List[B]


  /*
    Exercise

    - totalPrice = most used(50%)
    - by unit count = 25%
    - by unit price = 25%

   */

  case class Purchase(nUnits: Int, unitPrice: Double)

  object Purchase {
    implicit val totalPriceOrdering: Ordering[Purchase] = Ordering.fromLessThan((a, b) => a.unitPrice * a.nUnits < b.unitPrice * b.nUnits)
  }

  object UnitCountOrdering {
    implicit val unitCountOrdering: Ordering[Purchase] = Ordering.fromLessThan(_.nUnits < _.nUnits)
  }

  object UnitPriceOrdering {
    implicit val unitPriceOrdering: Ordering[Purchase] = Ordering.fromLessThan(_.unitPrice < _.unitPrice)
  }

}
