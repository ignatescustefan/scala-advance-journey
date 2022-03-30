package lectures.part4implicits

object PimpMyLibrary extends App {

  // 2.isPrime

  implicit class RichInt(val value: Int) extends AnyVal {
    def isEven: Boolean = value % 2 == 0

    def sqrt: Double = Math.sqrt(value)

    def times(f: () => Unit): Unit = {
      def timesAux(n: Int): Unit = {
        if (n <= 0) ()
        else {
          f()
          timesAux(n - 1)
        }
      }

      timesAux(value)
    }

    def *[T](list: List[T]): List[T] = {
      def concatenate(n: Int): List[T] = {
        if (n <= 0) List.empty
        else {
          concatenate(n - 1) ++ list
        }
      }

      concatenate(value)
    }
  }


  implicit class RicherInt(richInt: RichInt) {
    def isOdd: Boolean = richInt.value % 2 != 0
  }

  new RichInt(43).sqrt
  43.isEven

  // type  enrichment = pimping

  1 to 10

  import scala.concurrent.duration._

  3.seconds

  // compiler doesn't do multiple implicit searches
  // 42.isOdd

  /*
    Enrich the String class
      - asInt
      - encrypt
          "John" -> Lqjp

    Keep enriching the Int class
      - times(function)
          3.times(() => ...)
      - *
        3 * List(1,2) => List(1,2,1,2,1,2)
   */

  implicit class RichString(val value: String) extends AnyVal {
    def asInt: Int = Integer.valueOf(value)

    def encrypt(cypherDistance: Int): String = value.map(c => (c + cypherDistance).toChar)
  }

  println("4".asInt + 6)
  println("John".encrypt(2))

  3.times(() => println("Something"))

  println(4 * List(1, 2))

  // "3" / 4

  implicit def stringToInt(string: String): Int = Integer.valueOf(string)


  println("6" / 2) // => stringToInt("6") / 2

  // equivalent: implicit class RichAltInt(value: Int)
  class RichAltInt(value: Int)

  implicit def enrich(value: Int): RichAltInt = new RichAltInt(value)

  // danger zone
  implicit def intToBoolean(i: Int): Boolean = i == 1

  /*
    if(n) do something
    else bla bla
   */

  val aConditionValue = if (3) "OK" else "Something wrong"

  println(aConditionValue)
}
