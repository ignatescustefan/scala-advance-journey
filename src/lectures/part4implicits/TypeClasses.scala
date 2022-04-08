package lectures.part4implicits

import exercises.EqualityPlayground
import lectures.part4implicits.TypeClasses.IntSerializer

object TypeClasses extends App {

  trait HTMLWritable {
    def toHtml: String
  }

  case class User(name: String, age: Int, email: String) extends HTMLWritable {
    def apply(FullEquality: EqualityPlayground.FullEquality.type): _root_.lectures.part4implicits.TypeClasses.User = ???

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

  // Part 3
  implicit class HTMLEnrichment[T](value: T) {
    def toHTML(implicit serializer: HTMLSerializer[T]): String = serializer.serialize(value)
  }

  println(user.toHTML(UserSerializer)) // println(new HTMLEnrichment[User](user).toHTML(UserSerializer)
  println(user.toHTML) // println(new HTMLEnrichment[User](user).toHTML(UserSerializer)

  // Cool
  /*
    - extends to new types
    - choose implementation
    - super expressive
   */

  println(2.toHTML)
  println(user.toHTML(PartialUserSerializer))

  /*
    - type class itself --- HTMLSerializer
    - type class instances(some of which are implicit) --- UserSerializer, PartialUserSerializer
    - conversion with implicit classes
   */

  // cpmtext bounds

  def htmlBoilerplate[T](content: T)(implicit serializer: HTMLSerializer[T]): String =
    s"<html><body>${content.toHTML(serializer)}</body></html>"

  def htmlSugar[T: HTMLSerializer](content: T): String = {
    val serializable = implicitly[HTMLSerializer[T]]
    s"<html><body>${content.toHTML}</body></html>"
  }

  // implicitly
  case class Permissions(mask: String)

  implicit val defaultPermissions = Permissions("0744")

  // in some other part of the code
  val standardPerms = implicitly[Permissions]
}
