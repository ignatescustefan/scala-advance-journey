package lectures.part2afp

object CurriesPAF extends App {
  // curried function
  val superAdder: Int => Int => Int =
    x => y => x + y

  val add3 = superAdder(3) // Int => Int = y = y+3

  println(add3(5))
  println(superAdder(3)(5)) // curried function

  // METHOD!
  def curriedAdder(x: Int)(y: Int): Int = x + y // curried method

  val add4: Int => Int = curriedAdder(4)

  // lifting = ETA-EXPANSION
  //functions != methods (JVM limitation)

  def inc(x: Int) = x + 1

  List(1, 2, 3).map(inc) // ETA-expansion inc == x=>inc(x)

  // Partial function applications
  val add5 = curriedAdder(4) _ // convert exp Int => Int

  // Exercise
  val simpleAddFunction = (x: Int, y: Int) => x + y

  def simpleAddMethod(x: Int, y: Int) = x + y

  def curriedAddMethod(x: Int)(y: Int) = x + y

  // add7: Int => Int = y + 7

  val add7 = (x: Int) => simpleAddFunction(x, 7)
  val add7_1 = simpleAddFunction.curried(7)
  val add7_2 = curriedAddMethod(7) _ // PAF
  val add7_3 = curriedAddMethod(7)(_) // PAF = alternative syntax
  val add7_4 = simpleAddMethod(7, _: Int) // alternative syntax for turning methods into function values

  // underscores are powerful
  def concatenate(a: String, b: String, c: String): String = a + b + c

  val insertName = concatenate("Hello, I'm ", _: String, ", how are you?")

  println(insertName("Dudu"))

  // Exercise
  /*
    1. Process a list of numbers and return their string representations with different formats
       Use the %4.2f, %8.6f and %14.12f with a curried formatter function
*/
  def curriedFormatter(s: String)(number: Double): String = s.format(number)

  val numbers = List(Math.PI, Math.E, 1, 9.8, 1.3e-12)

  val simpleFormatter = curriedFormatter("%4.2f") _
  val seriousFormatter = curriedFormatter("%4.2f") _
  val preciseFormatter = curriedFormatter("%14.12f") _

  println(numbers.map(simpleFormatter))
  println(numbers.map(seriousFormatter))
  println(numbers.map(preciseFormatter))


  /*
    2. difference between
      - functions vs methods
      - parameters: by-name vs 0 lambda
   */
  def byName(n: => Int) = n + 1

  def byFunction(f: () => Int) = f() + 1

  def method: Int = 42

  def parenMethod(): Int = 42

  /*
   calling byName and byFunction
   - int
   - method
   - parenMethod
   - lambda
   - PAF
   */
  byName(23) // Ok
  byName(method) // Ok
  byName(parenMethod()) // Ok
  byName(parenMethod) // ok but beware => byName(parenMethod())
  // byName(() => 32) // Not ok
  byName((() => 43) ())
  // byName(parenMethod _) // not ok
  // byFunction(32) // not ok
  // byFunction(method) // not ok, compiler does not do ETA expansion
  byFunction(parenMethod) // compiler does ETA-expansion
  byFunction(() => 43) // also works
  byFunction(parenMethod _) // also works, but warning unnecessary

}

