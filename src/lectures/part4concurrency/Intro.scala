package lectures.part4concurrency

import java.util.concurrent.Executors

object Intro extends App {

  /*
  interface Runnable{
    public void run()
  }
   */
  // JVM threads
  val aThread = new Thread(new Runnable {
    def run(): Unit = println("Running in parallel")
  })

  aThread.start() // gives the signal to the jvm to start a jvm thread
  // create a JVM thread => OS thread
  aThread.join() // blocks until aThread finishes running

  val threadHello = new Thread( () => (1 to 5).foreach(_ => println("hello")))

  val threadGoodBye = new Thread( () => (1 to 5).foreach(_ => println("bye")))

  threadHello.start()
  threadGoodBye.start()

  // executors
  val pool = Executors.newFixedThreadPool(10)
  pool.execute(()=> println("something in the thread pool"))

  pool.execute(()=> {
    Thread.sleep(1000)
    println("done after 1 second")
  })

  pool.execute(()=> {
    Thread.sleep(1000)
    println("almost done after 1 second")
    Thread.sleep(1000)
    println("done after 2 second")
  })

  pool.shutdown()
  println(pool.isShutdown)
//  pool.execute(()=> println("should not appear")) // this throws an exception in the calling thread

//  pool.shutdownNow()


}
