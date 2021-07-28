package lectures.part1as

import scala.annotation.tailrec

object Recap extends App {

  val aCondition: Boolean = false
  val aConditionedVal = if (aCondition) 43 else 131
  // instructions vs expressions

  // compiler infers types for us
  val aCodeBlock = {
    if (aCondition) 54
    43
  }

  // Unit -> side effects
  val theUnit = println("hello, Scala")

  // functions
  def aFunction(x: Int): Int = x + 1

  // recursion: stack and tail

  @tailrec
  def factorial(n: Int, accumulator: Int): Int = {
    if (n < 1) accumulator
    else factorial(n - 1, accumulator * n)
  }

  println(factorial(3, 1))

  // object oriented programming

  class Animal

  class Dog extends Animal

  val aDog: Animal = new Dog // subtyping polymorphism

  trait Carnivore {
    def eat(a: Animal): Unit
  }

  class Crocodile extends Animal with Carnivore {
    override def eat(a: Animal): Unit = println("crunch!")
  }

  // method notations
  val aCrocodile = new Crocodile
  aCrocodile.eat(aDog)
  aCrocodile eat aDog // natural language

  1 + 2
  1.+(2)

  // anonymous classes
  // the compiler creates an anonumous class
  val aCarnivore = new Carnivore {
    override def eat(a: Animal): Unit = println("roar!")
  }

  //generics

  abstract class MyList[+A] // variance and variance problems in THIS COURSES

  // singletons and companions
  object MyList

  // case classes
  // have companions, serializable, all the parameters are fields, apply method etc
  case class Person(name: String, age: Int)

  // exceptions and try catch

  // val throwsException = throw new RuntimeException // Nothing
  val aPotentialFailure = try {
    throw new RuntimeException
  } catch {
    case e: Exception => "I caught an exception"
  } finally {
    println("some logs")
  }

  // packaging and imports

  // functional programming
  val incrementer = new Function1[Int, Int] {
    override def apply(v1: Int): Int = v1 + 1
  }

  incrementer(1)

  val anonymousIncrementer = (x: Int) => x + 1
  List(1, 2, 3).map(anonymousIncrementer)

  // map, flatMap, filter

  // for-comprehension
  val pairs = for {
    num <- List(1, 2, 3) // if condition
    char <- List('a', 'b', 'c')
  } yield num + "-" + char
  println(pairs)

  // Scala collections: Seq, Array, List, Vector, Map, Tuple

  val aMap = Map(
    "Daniel" -> 576,
    "Jess" -> 123
  )

  // "collections": Options & Try

  val anOption = Some(2)

  // patternMatching
  val x = 2
  val order = x match {
    case 1 => "first"
    case 2 => "second"
    case 3 => "third"
    case _ => x + "th"
  }

  val bob = Person("Bob", 22)

  val greeting = bob match {
    case Person(n, _) => s"Hi, my name is $n"
  }
  // all the patterns
}
