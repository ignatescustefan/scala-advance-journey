package lectures.part4implicits

import java.util.Date

object JSONSerialization extends App {
  /*
    Users, posts, feeds
    Serialize to JSON
   */
  case class User(name: String, age: Int, email: String)

  case class Post(content: String, createdAt: Date)

  case class Feed(user: User, posts: List[Post])

  /*
  1 - intermediate data types: Int, String, List, Date
  2 - type classes for conversion to intermediate data types
  3 - serialize to JSON
   */

  sealed trait JSONValue { // intermediate data type
    def stringify: String
  }

  final case class JSONString(value: String) extends JSONValue {
    override def stringify: String = s"\"$value\""
  }

  final case class JSONNumber(value: Int) extends JSONValue {
    override def stringify: String = value.toString
  }

  final case class JSONArray(values: List[JSONValue]) extends JSONValue {
    override def stringify: String = values.map(_.stringify).mkString("[", ",", "]")
  }

  final case class JSONObject(values: Map[String, JSONValue]) extends JSONValue {
    /*
    {
      name: "john",
      age: 12,
      friends: []
      latestPost: {
        content: "dada"
        date:
      }
    }
     */
    override def stringify: String = values.map {
      case (key, value) => s"\"$key\":${value.stringify}"
    }.mkString("{", ",", "}")
  }

  val data = JSONObject(Map(
    "user" -> JSONString("Daniel"),
    "posts" -> JSONArray(List(
      JSONString("Scala rocks!"),
      JSONNumber(43)
    ))
  ))

  println(data.stringify)
  // type class
  /*
    1 - type class
    2 - type class instance (implicit)
    3 - pimp library to use type class instances
   */

  // 2.1
  trait JSONConverter[T] {
    def convert(value: T): JSONValue
  }

  // 2.3 conversion

  implicit class JSONOps[T](value: T) {
    def toJSON(implicit converter: JSONConverter[T]): JSONValue =
      converter.convert(value)
  }

  // 2.2
  // existing data types
  implicit object StringConverter extends JSONConverter[String] {
    override def convert(value: String): JSONValue = JSONString(value)
  }

  implicit object NumberConverter extends JSONConverter[Int] {
    override def convert(value: Int): JSONValue = JSONNumber(value)
  }

  // custom data types
  implicit object UserConverter extends JSONConverter[User] {
    override def convert(value: User): JSONValue = JSONObject(Map(
      "name" -> JSONString(value.name),
      "age" -> JSONNumber(value.age),
      "email" -> JSONString(value.email),
    ))
  }

  implicit object PostConverter extends JSONConverter[Post] {
    override def convert(value: Post): JSONValue = JSONObject(Map(
      "content" -> JSONString(value.content),
      "created" -> JSONString(value.createdAt.toString),
    ))
  }

  implicit object FeedConverter extends JSONConverter[Feed] {
    override def convert(value: Feed): JSONValue = JSONObject(Map(
      "user" -> value.user.toJSON, // TODO
      "pots" -> JSONArray(value.posts.map(_.toJSON)), // TODO
    ))
  }



  // cal stringify on result

  val now = new Date(System.currentTimeMillis())
  val john = User("John", 34, "john@mail.com")
  val feed = Feed(john, List(
    Post("Hello", now),
    Post("look at me", now)
  ))

  println(feed.toJSON.stringify)
}