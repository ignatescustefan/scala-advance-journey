package lectures.part3concurrency

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

object FuturesPromises extends App {
  def calculateMeaningOfLife: Int = {
    Thread.sleep(2000)
    42
  }

  val aFuture = Future {
    calculateMeaningOfLife // calculates the meaning of life on another thread
  } //(global) which is passed by the compiler

  println(aFuture.value) // Option[Try[Int]]

  println("Waiting on the future")

  aFuture.onComplete {
    case Success(value) => println(s"The meaning of life is $value")
    case Failure(e) => println(s"I have failed wiht: $e")
  } // Some thread

  Thread.sleep(3000)
}
