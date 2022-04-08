package exercises

import lectures.part4implicits.TypeClasses.User

object EqualityPlayground extends App {
  /**
   * Equality
   */
  trait Equal[T] {
    def apply(a: T, b: T): Boolean
  }

  implicit object NameEquality extends Equal[User] {
    override def apply(a: User, b: User): Boolean = a.name.equals(b.name)
  }

  object FullEquality extends Equal[User] {
    override def apply(a: User, b: User): Boolean = a.name == b.name && a.age == b.age && a.email == b.email
  }

  // Exercise: implement the TC pattern for the equality

  object Equal {
    def apply[T](a: T, b: T)(implicit equalizer: Equal[T]): Boolean =
      equalizer.apply(a, b)
  }

  val user = User("John", 12, "john@mail.com")

  val anotherJohn = User("John", 12, "mail@test.com")

  println(Equal[User](user, anotherJohn))

  // Exercise - improve the equal TC with an implicit conversion class
  /*
    ===(anotherValue: T)
    !==(anotherValue: T)
   */

  implicit class TypeSaveEqual[T](value: T) {
    def ===(anotherValue: T)(implicit equalizer: Equal[T]): Boolean = equalizer.apply(value, anotherValue)

    def !==(anotherValue: T)(implicit equalizer: Equal[T]): Boolean = !equalizer.apply(value, anotherValue)
  }

  println(user === anotherJohn)
  println(user.===(anotherJohn)(FullEquality))
}
