package lectures.part2afp

object PartialFunction extends App {

  val aFunction = (x: Int) => x + 1 // Function1[Int, Int] === Int => Int

  val aFussyFunction = (x: Int) =>
    if (x == 1) 43
    else if (x == 2) 432
    else if (x == 4) 42
    else throw new FunctionNotApplicableException

  class FunctionNotApplicableException extends RuntimeException

  val aNicerFussyFunction = (x: Int) => x match {
    case 1 => 42
    case 2 => 47
    case 5 => 999
  } // {1, 2, 5} -> Int

  val aPartialFunction: PartialFunction[Int, Int] = {
    case 1 => 42
    case 2 => 47
    case 5 => 999
  } // partial function value

  println(aPartialFunction(2))

  // PF utilities
  println(aPartialFunction.isDefinedAt(67))

  // lift
  val lifted = aPartialFunction.lift
  println(lifted(2))
  println(lifted(84))

  val pfChain = aPartialFunction.orElse[Int, Int] {
    case 45 => 67
  }

  println(pfChain(2))
  println(pfChain(45))

  // PF extend normal function

  val aTotalFunction: Int => Int = {
    case 1 => 99
  }
  val aMappedList = List(1, 2, 3).map {
    case 1 => 43
    case 2 => 78
    case 3 => 1000
  }
  println(aMappedList)

  /*
    Note: PF can only have one parameter type
   */
  /**
   * 1 - construct a PF instance yourself (anonymous class)
   * 2 - dumb chatbot as a PF
   */

  val aManualFussyFunction = new PartialFunction[Int, Int] {
    override def isDefinedAt(x: Int): Boolean = x == 1 || x == 2 || x == 5

    override def apply(v1: Int): Int = v1 match {
      case 1 => 41
      case 2 => 43
      case 5 => 999
    }
  }

  val chatbot: PartialFunction[String, String] = {
    case "hello" => "Hi, my name si LX933"
    case "goodbye" => "Bye bye"
    case "call mom" => "Unable to find your phone"
  }

  // scala.io.Source.stdin.getLines().foreach(line => println("Chatbot says: " + chatbot(line)))
  scala.io.Source.stdin.getLines().map(chatbot).foreach(println)

}
