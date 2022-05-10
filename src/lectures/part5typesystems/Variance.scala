package lectures.part5typesystems

object Variance extends App {

  trait Animal

  class Dog extends Animal

  class Cat extends Animal

  class Crocodile extends Animal

  class Cage[T]

  // yes - covariance
  class CCage[+T]

  val cCage: CCage[Animal] = new CCage[Animal]

  // no - invariance
  class ICage[T]
  //  val iCage: ICage[Animal] = new ICage[Cat]
  //  val x: Int = "Hello"

  // hell no - opposite = contravariance
  class XCage[-T]

  val xCage: XCage[Cat] = new XCage[Animal]

  class InvariantCage[T](animal: T) // invariant

  // covariant positions

  class CovariantCage[+T](val animal: T) // Covariant position

  // class ContravariantCage[-T](val animal: T) // Contravariant position

  // class CovariantVariableCage[+T](var animal: T) // types of vars are in contravariant positions

  // class ContravariantVariableCage[-T](var animal: T)

  class InvariantVariableCage[T](var animal: T) // OK

  /*
  trait AnotherCovariantCage[+T] {
    def addAnimal(animal: T) // Contravariant position
  }
   */
  class AnotherContravariantCage[-T] {
    def addAnimal(animal: T) = true // Contravariant position
  }

  val acc: AnotherContravariantCage[Cat] = new AnotherContravariantCage[Animal]
  acc.addAnimal(new Cat)

  class Kitty extends Cat

  acc.addAnimal(new Kitty)

  class MyList[+A] {
    def add[B >: A](element: B): MyList[B] = new MyList[B]
  }

  val emptyList = new MyList[Kitty]
  val animals = emptyList.add(new Kitty)
  val moreAnimals = animals.add(new Cat)

  val evenMoreAnimals = moreAnimals.add(new Dog)

  // Method arguments are in contravariant position

  // return types

  class PetShop[-T] {
    // def get(isItaPuppy: Boolean): T // method return types are in covariant position
    def get[S <: T](isItaPuppy: Boolean, defaultAnimal: S): S = defaultAnimal
  }

  val shop: PetShop[Dog] = new PetShop[Animal]

  // val evilCat = shop.get(true, new Cat)
  class TerraNova extends Dog

  val bigFurry = shop.get(true, new TerraNova)

  /*
    Big rule
    - method arguments are in contravariant position
    - return types are in covariant position
   */

  /**
   * 1. Invariant, covariant, contravariant
   * Parking[T](things: List[T]) {
   * park(vehicle: T)
   * impound(vehicles: List[T])
   * checkVehicle(conditions: String): List[T]
   * }
   *
   * 2. used some else's API IList[T]
   * 2. Parking = monad!
   *  - flatmap
   */

  class Vehicle

  class Bike extends Vehicle

  class Car extends Vehicle

  class IList[T]

  class IParking[T](vehicles: List[T]) {
    def park(vehicle: T): IParking[T] = ???

    def impound(vehicles: List[T]): IParking[T] = ???

    def checkVehicle(conditions: String): List[T] = ???

    def flatMap[S](f: T => IParking[S]): IParking[S] = ???
  }

  class CParking[+T](things: List[T]) {
    def park[S >: T](vehicle: S): CParking[S] = ???

    def impound[S >: T](vehicles: List[S]): CParking[S] = ???

    def checkVehicle(conditions: String): List[T] = ???

    def flatMap[S](f: T => CParking[S]): CParking[S] = ???
  }

  class XParking[-T](things: List[T]) {
    def park(vehicle: T): XParking[T] = ???

    def impound(vehicles: List[T]): XParking[T] = ???

    def checkVehicle[S <: T](conditions: String): List[S] = ???

    def flatMap[R <: T, S](f: R => XParking[S]): XParking[S] = ???
  }

  /*
    Rule od thumb
     - use covariance = collection of things
     - use contravariance = group of actions
   */

  class CParking2[+T](things: IList[T]) {
    def park[S >: T](vehicle: S): CParking2[S] = ???

    def impound[S >: T](vehicles: IList[S]): CParking2[S] = ???

    def checkVehicle[S >: T](conditions: String): IList[S] = ???
  }

  class XParking2[-T](things: IList[T]) {
    def park(vehicle: T): XParking2[T] = ???

    def impound[S <: T](vehicles: IList[S]): XParking2[S] = ???

    def checkVehicle[S <: T](conditions: String): IList[S] = ???
  }

  // flatMap
}
