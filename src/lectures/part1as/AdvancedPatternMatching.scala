package lectures.part1as

object AdvancedPatternMatching extends App {

  val numbers = List(1)
  val description: Unit = numbers match {
    case head :: Nil => println(s"The only element is $head")
    case _ =>
  }

  /*
  - constants
  - wildcards
  - tuples
  - some special magic like above
   */

  class Person(val name: String, val age: Int)

  // define an object companion

  //  object PersonPattern {
  object Person {
    def unapply(person: Person): Option[(String, Int)] = {
      if (person.age < 20) None
      else Some((person.name, person.age))
    }

    def unapply(age: Int): Option[String] =
      Some(if (age < 21) "minor" else "major")
  }

  val bob = new Person("Bob", 22)
  val greeting = bob match {
    case Person(n, a) => s"Hi, my name is $n and I am $a years old."
  }
  println(greeting)

  val legalStatus = bob.age match {
    case Person(status) => s"My legal status is $status"
  }
  println(legalStatus)

  /*
    Exercise.
   */

  object singleDigit {
    def unapply(args: Int): Boolean = args < 10
  }

  object even {
    def unapply(args: Int): Boolean = args % 2 == 0
  }

  val n: Int = 44

  val mathProperty = n match {
    case x if x < 10 => "single digit"
    case x if x % 2 == 0 => "an even number"
    case _ => "no property"
  }

  object MathProperty {
    def unapply(n: Int): Option[String] = Some(
      if (n < 10) "single digit"
      else if (n % 2 == 0) "even number"
      else "no property"
    )
  }

  val aBetterMathProperty = n match {
    case MathProperty(property) => s"Number status: $property"
  }
  println(aBetterMathProperty)

  val aBetterBetterMathProperty = n match {
    case singleDigit() => "single digit"
    case even() => "an even number"
    case _ => "no property"
  }
  println(aBetterBetterMathProperty)
}
