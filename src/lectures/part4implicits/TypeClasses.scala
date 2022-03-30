package lectures.part4implicits

import lectures.part4implicits.TypeClasses.IntSerializer

object TypeClasses extends App {

  trait HTMLWritable {
    def toHtml: String
  }

  case class User(name: String, age: Int, email: String) extends HTMLWritable {
    override def toHtml: String = s"<div>$name ($age yo) <a href=$email/> </div>"
  }

  User("John", 12, "john@mail.com").toHtml
  /*
    1 - for the types WE write
    2 - ONE implementation
   */

  // option 2 - pattern matching

  object HTMLSerializerPM {
    def serializeToHTML(value: Any) = value match {
      case User(n, a, e) =>
      case _ =>
    }
  }

  /*
    1 - lost type safety
    2 - need to modify the code every time
    3 - still one implamentation
   */

  trait HTMLSerializer[T] {
    def serialize(value: T): String
  }

  implicit object UserSerializer extends HTMLSerializer[User] {
    override def serialize(user: User): String = s"<div>${user.name} (${user.age} yo) <a href=${user.email}/> </div>"
  }

  val user = User("John", 12, "john@mail.com")

  println(UserSerializer.serialize(user))

  // 1 we can define serializers for other types

  import java.util.Date

  object DateSerializer extends HTMLSerializer[Date] {
    override def serialize(value: Date): String = s"<div>${value.toString}</div>"
  }

  // 2 - we can define multiple serializers
  object PartialUserSerializer extends HTMLSerializer[User] {
    override def serialize(user: User): String = s"<div>${user.name}</div>"
  }

  // Type class

  trait MyTypeClassTemplate[T] {
    def action(value: T): String
  }

  object MyTypeClassTemplate {
    def apply[T](implicit instance: MyTypeClassTemplate[T]) = instance
  }

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

  // Part 2

  object HTMLSerializer {
    def serialize[T](value: T)(implicit serializer: HTMLSerializer[T]): String =
      serializer.serialize(value)

    def apply[T](implicit serializer: HTMLSerializer[T]) = serializer
  }

  implicit object IntSerializer extends HTMLSerializer[Int] {
    override def serialize(value: Int): String = s"<div>$value<div>"
  }


  println(HTMLSerializer.serialize(34))
  println(HTMLSerializer.serialize(user))

  // access to the entire type class interface
  println(HTMLSerializer[User].serialize(user))

  // Exercise: implement the TC pattern for the equality

  object Equal {
    def apply[T](a:T, b:T)(implicit equalizer:Equal[T]):Boolean =
      equalizer.apply(a,b)
  }

  val anotherJohn = User("John",12,"mail@test.com")

  println(Equal[User](user,anotherJohn))
  // Adhoc polymorphisms
}
