package lectures.part5typesystems

object TypeMembers extends App {

  class Animal

  class Dog extends Animal

  class Cat extends Animal

  class AnimalCollection {
    type AnimalType // abstract type member
    type BoundedAnimal <: Animal // must extend Animal
    type SuperBoundedAnimal >: Dog <: Animal
    type AnimalC = Cat
  }

  val acc = new AnimalCollection
  val dog: acc.AnimalType = ???

  // val cat: acc.BoundedAnimal = new Cat

  val pup: acc.SuperBoundedAnimal = new Dog
  val cat: acc.AnimalC = new Cat

  type CatAlias = Cat
  val anotherCat: CatAlias = new Cat


  // alternative to generics
  trait MyList {
    type T

    def add(element: T): MyList
  }

  class NonEmptyList(value: Int) extends MyList {
    override type T = Int

    override def add(element: Int): MyList = ???
  }

  // .type

  type CatsType = cat.type

  val newCat: CatsType = cat
  // new CatsType

  /*
    Exercise - enforce a type tp be applicable to some types only
   */

  // locked
  trait MList {
    type A

    def head: A

    def tail: MList
  }


  trait ApplicableToNumbers {
    type A <: Number
  }

  // not OK
  /*
  class CustomList(hd: String, tl: CustomList) extends MList with ApplicableToNumbers {
    override type A = String

    override def head: String = hd

    override def tail: CustomList = tl
  }
  */

  // OK
  class IntList(hd: Integer, tl: IntList) extends MList {
    override type A = Integer

    override def head: Integer = hd

    override def tail: IntList = tl
  }

  // Number
}
