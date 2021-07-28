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


  // infix patterns
  case class Or[A, B](a: A, b: B) //Either

  val either = Or(2, "two")
  val humanDescription = either match {
    case number Or string => s"$number is written as $string"
  }
  println(humanDescription)

  // decomposing sequences
  val vararg = numbers match {
    case List(1, _*) => "starting with 1"
  }

  abstract class MyList[+A] {
    def head: A = ???

    def tail: MyList[A] = ???
  }

  case object Empty extends MyList[Nothing]

  case class Cons[+A](override val head: A, override val tail: MyList[A]) extends MyList[A]

  object MyList {
    def unapplySeq[A](list: MyList[A]): Option[Seq[A]] =
      if (list == Empty) Some(Seq.empty)
      else unapplySeq(list.tail).map(list.head +: _)
  }

  val myList: MyList[Int] = Cons(1, Cons(2, Cons(3, Empty)))

  val decomposed = myList match {
    case MyList(1, 2, _*) => "Starting with 1, 2"
    case _ => "something else"
  }
  println(decomposed)

  // custom return types for unapply
  // isEmpty: boolean, get: something

  abstract class Wrapper[T] {
    def isEmpty: Boolean

    def get: T
  }

  object PersonWrapper {
    def unapply(person: Person): Wrapper[String] = new Wrapper[String] {
      override def isEmpty: Boolean = false

      override def get = person.name
    }
  }

  println(bob match {
    case PersonWrapper(n) => s"This person's name is $n"
    case _ => "an alien"
  })
}
