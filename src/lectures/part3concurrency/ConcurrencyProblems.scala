package lectures.part3concurrency

object ConcurrencyProblems {

  def runInParallel(): Unit = {
    var x = 0

    val thread1 = new Thread(() => {
      x = 1
    })

    val thread2 = new Thread(() => {
      x = 2
    })

    thread1.start()
    thread2.start()

    println(x) // race condition

  }

  case class BankAccount(var amount: Int)

  def buy(bankAccount: BankAccount, thing: String, price: Int): Unit = {
    bankAccount.amount -= price
  }

  def buySafe(bankAccount: BankAccount, thing: String, price: Int): Unit = {
    bankAccount.synchronized { // does not allow multiple threads to run the critical section at the same time
      bankAccount.amount -= price // critical section
    }
  }

  def demoBankingProblem(): Unit = {
    (1 to 10000).foreach { _ =>
      val account = BankAccount(50000)
      val thread1 = new Thread(() => buySafe(account, "shoes", 3000))
      val thread2 = new Thread(() => buySafe(account, "iphone", 4000))

      thread1.start()
      thread2.start()
      thread1.join()
      thread2.join()

      if (account.amount != 43000) println(s"I've just broken the bank: ${account.amount}")
    }
  }

  /*
    Exercise
    1 - create "inception threads"
      thread1
        -> thread2
          -> thread3
           ..
      each thread prints "hello from thread $i
      print all messages in reverse order

     2 - what's the max/min value of x
     3 - "sleep fallacy" what's the value of message
   */

  // 1

  def inceptionThread(maxThread: Int, i: Int = 1): Thread =
    new Thread(() => {
      if (i < maxThread) {
        val newThread = inceptionThread(maxThread, i + 1)
        newThread.start()
        newThread.join()
      }
      println(s"hello from thread $i")
    })

  def minMaxX(): Unit = {
    var x = 0
    val threads = (1 to 100).map(_ => new Thread(() => x += 1))
    threads.foreach(_.start())
  }

  def demoSleepFallact(): Unit = {
    var message = ""
    val awesomeThread = new Thread(() => {
      Thread.sleep(1000)
      message = "Scala is awesome"
    })

    message = "Scala sucks"
    awesomeThread.start()
    Thread.sleep(1001)
    println(message)
  }

  def main(args: Array[String]): Unit = {
    // demoBankingProblem()
    inceptionThread(50).start()
  }
}
