package lectures.part4implicits

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object MagnetPattern extends App {

  // method overloading

  class P2PRequest

  class P2PResponse

  class Serializer[T]

  trait Actor {
    def receive(statusCode: Int): Int

    def receive(request: P2PRequest): Int

    def receive(response: P2PResponse): Int

    // def receive[T](message: T)(implicit serializer: Serializer[T]): Int
    def receive[T: Serializer](message: T): Int

    def receive[T: Serializer](message: T, statusCode: Int): Int

    def receive(future: Future[P2PRequest]): Int
    // def receive(future: Future[P2PResponse]): InT
    // lots of overloads

  }
  /*
    1 - type erasure
    2 - lifting doesn't work for all overloads

      val receive = receive _ // ?!

    3 - code duplication
    4 - type inference and default args
      actor.receive(?!)
   */

  trait MessageMagnet[Result] {
    def apply(): Result
  }

  def receive[R](magnet: MessageMagnet[R]): R = magnet.apply()

  implicit class FromP2PRequest(request: P2PRequest) extends MessageMagnet[Int] {
    override def apply(): Int = {
      // logic handling P2PRequest
      println("Handling P2PRequest")
      242
    }
  }

  implicit class FromP2PResponse(response: P2PResponse) extends MessageMagnet[Int] {
    override def apply(): Int = {
      // logic handling P2PResponse
      println("Handling P2PResponse")
      424
    }
  }

  receive(new P2PRequest)
  receive(new P2PResponse)

  // 1 - no more tye erasure problems!

  implicit class FromRequestFuture(future: Future[P2PRequest]) extends MessageMagnet[Int] {
    override def apply(): Int = {
      println("Handling Future P2PRequest")
      242
    }
  }

  implicit class FromResponseFutureP2P(future: Future[P2PResponse]) extends MessageMagnet[Int] {
    override def apply(): Int = {
      // logic handling P2PResponse
      println("Handling Future P2PResponse")
      424
    }
  }

  println(receive(Future(new P2PRequest)))
  println(receive(Future(new P2PResponse)))

  // 2 - lifting works

  trait MathLib {
    def add1(x: Int) = x + 1

    def add1(x: String) = x.toInt + 1
  }

  // "magnetize"
  trait AddMagnet {
    def apply(): Int
  }

  def add1(magnet: AddMagnet): Int = magnet()

  implicit class AddInt(x: Int) extends AddMagnet {
    override def apply(): Int = x + 1
  }

  implicit class AddString(x: String) extends AddMagnet {
    override def apply(): Int = x.toInt + 1
  }

  val addFV = add1 _

  println(addFV(1))
  println(addFV("41"))

  //  val receiveFV = receive _
  //  receiveFV(new P2PRequest)

  /*
    Drawbacks
    1 - verbose
    2 - harder to read
    3 - you can't name or place default arguments
    4 - call by name doesn't work correctly
      (exercise: prove it)
   */

  class Handler {
    def handle(s: => String): Unit = {
      println(s)
      println(s)
    }
    // other overloads
  }

  trait HandleMagnet {
    def apply(): Unit
  }

  def handle(magnet: HandleMagnet) = magnet()

  implicit class StringHandle(s: => String) extends HandleMagnet {
    override def apply(): Unit = {
      println(s)
      println(s)
    }
  }

  def sideEffectMethod(): String = {
    println("Hello scala!")
    "Ha ha ha"
  }

  //  handle(sideEffectMethod())
  handle {
    println("Hello scala!")
    "Ha ha ha"
  }
}


