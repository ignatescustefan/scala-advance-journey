package lectures.part2afp

object LazyEvaluation extends App {

  // lazy DELAYS the evaluation of values
  lazy val x: Int = {
    println("hello")
    43
  }
  println(x)
  println(x)

  // examples of implications:

  def sideEffectCondition: Boolean = {
    println("Boo")
    true
  }

  def simpleCondition: Boolean = false

  lazy val lazyCondition = sideEffectCondition
  println(if (simpleCondition && lazyCondition) "yes" else "no") // not evaluated because is not needed

  // println(if(lazyCondition && simpleCondition)"yes" else "no")

  def byNameMethod(n: => Int): Int = {
    // Call by need
    lazy val t = n
    t + t + t + 1
  }

  def retrieveMagiValue = {
    println("waiting")
    Thread.sleep(1000)
    43
  }

  println(byNameMethod(retrieveMagiValue))
  // use lazy val

  //filtering with lazy val
  def lessThan30(i: Int): Boolean = {
    println(s"$i is less than 30?")
    i < 30
  }

  def greaterThan20(i: Int): Boolean = {
    println(s"$i is greater than 20?")
    i > 20
  }

  val numbers = List(1, 24, 30, 5, 23)
  val lt30 = numbers.filter(lessThan30)
  val gt20 = lt30.filter(greaterThan20)
  println(gt20)

  val lt30Lazy = numbers.withFilter(lessThan30) // lazy val under the hood
  val gt20Lazy = lt30Lazy.withFilter(greaterThan20)

  println
  gt20Lazy.foreach(println)

  // for-comprehensions use withFilter with guards
  for {
    a <- List(1, 2, 3) if a % 2 == 0 // use lazy val
  } yield a + 1
  List(1, 2, 3).withFilter(_ % 2 == 0).map(_ + 1) // List[Int]

  /*
    Exercise: implement a lazily evaluated, singly linked STREAM of elements.

    naturals = MyStream.from(1)(x=> x+1) = stream of natural numbers
    naturals.take(100) // lazily evaluated of the first 100 naturals (finite stream)
    naturals.take(100).foreach(println)
    naturals.foreach(println)'
    naturals.map(_ * 2) // stream of al even numbers

   */
  abstract class MyStream[+A] {
    def isEmpty: Boolean

    def head: A

    def tail: MyStream[A]

    def #::[B >: A](element: B): MyStream[B] // prepend operator

    def ++[B >: A](anotherStream: MyStream[B]): MyStream[B] // concatenate two streams

    def foreach(f: A => Unit): Unit

    def map[B](f: A => B): MyStream[B]

    def flatMap[B](f: A => MyStream[B]): MyStream[B]

    def filter(predicate: A => Boolean): MyStream[A]

    def take(n: Int): MyStream[A] // takes the first n elements of this stream

    def takeAsList(n: Int): List[A]

  }

  object MyStream {
    def from[A](start: A)(generator: A => A): MyStream[A] = ???
  }
}
