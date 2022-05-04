package lectures.part5typesystems

object RockingInheritance extends App {

  // convenience

  trait Writer[T] {
    def write(value: T): Unit
  }

  trait Closable {
    def close(status: Int): Unit
  }

  trait GenericStream[T] {
    def foreach(f: T => Unit): Unit
  }

  def processStream[T](stream: GenericStream[T] with Writer[T] with Closable): Unit = {
    stream.foreach(println)
    stream.close(0)
  }

  // diamond problem

  trait Animal {
    def name: String
  }

  trait Lion extends Animal {
    override def name: String = "lion"
  }

  trait Tiger extends Animal {
    override def name: String = "tiger"
  }

  class Mutant extends Lion with Tiger

  val m = new Mutant
  println(m.name) // LAST override gets picked

  // the super problem
  trait Cold {
    def print: Unit = println("cold")
  }

  trait Blue extends Cold {
    override def print: Unit = {
      println("blue")
      super.print
    }
  }

  trait Green extends Cold {
    override def print: Unit = {
      println("green")
      super.print
    }
  }

  trait Red {
    def print: Unit = println("red")
  }

  class White extends Red with Green with Blue {
    override def print: Unit = {
      println("white")
      super.print
    }
  }

  val color = new White

  color.print
}
