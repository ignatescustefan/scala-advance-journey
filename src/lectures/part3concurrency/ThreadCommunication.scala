package lectures.part3concurrency

import scala.collection.mutable
import scala.util.Random

object ThreadCommunication extends App {
  /*
   the produce-consumer problem

   producer -> [?] -> consumer

   */
  class SimpleContainer {
    private var value: Int = 0

    def isEmpty: Boolean = value == 0

    def set(newValue: Int): Unit = value = newValue

    def get: Int = {
      val result = value
      value = 0
      result
    }
  }

  def naiveProducerConsumer(): Unit = {
    val container = new SimpleContainer

    val consumer = new Thread(() => {
      println("[consumer] waiting..")
      while (container.isEmpty) {
        println("[consumer] actively waiting...")
      }
      println("[consumer] I have consumed " + container.get)
    })

    val producer = new Thread(() => {
      println("[producer] computing...")
      Thread.sleep(500)
      val value = 40
      println("[producer] I have produced: " + value)
      container.set(value)
    })

    consumer.start()
    producer.start()
  }

  //  naiveProducerConsumer()

  // wait&notify

  def smartProducerConsumer(): Unit = {
    val container = new SimpleContainer

    val consumer = new Thread(() => {
      println("[consumer] waiting..")
      container.synchronized {
        container.wait()
      }
      println("[consumer] I have consumed " + container.get)
    })

    val producer = new Thread(() => {
      println("[producer] computing...")
      Thread.sleep(2000)
      val value = 40

      container.synchronized {
        println("[producer] I'm producing: " + value)
        container.set(value)
        container.notify()
      }
    })

    consumer.start()
    producer.start()
  }

  //  smartProducerConsumer()

  /*
    producer -> [? ? ?] -> consumer

   */

  def producerConsumerLargeBuffer(): Unit = {
    val buffer: mutable.Queue[Int] = new mutable.Queue[Int]
    val capacity = 3

    val consumer = new Thread(() => {
      val random = new Random()
      while (true) {
        buffer.synchronized {
          if (buffer.isEmpty) {
            println("[consumer] buffer empty, waiting..")
            buffer.wait()
          }
          val x = buffer.dequeue()
          println("[consumer] consumed " + x)

          buffer.notify()
        }
        Thread.sleep(random.nextInt(500))
      }
    })

    val producer = new Thread(() => {
      val random = new Random()
      var i = 0

      while (true) {
        buffer.synchronized {
          if (buffer.size == capacity) {
            println("[producer] buffer is full, waiting...")
            buffer.wait()
          }
          // there must be at least one empty space in the buffer
          println("[producer] producing " + i)
          buffer.enqueue(i)
          buffer.notify()

          i += 1
        }
        Thread.sleep(random.nextInt(500))
      }
    })


    consumer.start()
    producer.start()
  }

  //  producerConsumerLargeBuffer()

  /*
    producer1 -> [? ? ?] -> consumer1
    producer2 ---^     ^--- consumer2
   */

  class Consumer(id: Int, buffer: mutable.Queue[Int]) extends Thread {
    override def run(): Unit = {
      val random = new Random()
      while (true) {
        buffer.synchronized {
          while (buffer.isEmpty) {
            println(s"[consumer($id)] buffer empty, waiting..")
            buffer.wait()
          }
          val x = buffer.dequeue()
          println(s"[consumer($id)] consumed $x")

          buffer.notify()
        }
        Thread.sleep(random.nextInt(500))
      }
    }
  }

  class Producer(id: Int, buffer: mutable.Queue[Int], capacity: Int) extends Thread {
    override def run(): Unit = {
      val random = new Random()
      var i = 0

      while (true) {
        buffer.synchronized {
          while (buffer.size == capacity) {
            println(s"[producer($id)] buffer is full, waiting...")
            buffer.wait()
          }
          // there must be at least one empty space in the buffer
          println(s"[producer($id)] producing " + i)
          buffer.enqueue(i)
          buffer.notify()

          i += 1
        }
        Thread.sleep(random.nextInt(500))
      }
    }
  }

  def multiProdCons(nConsumers: Int, nProducers: Int): Unit = {
    val buffer: mutable.Queue[Int] = new mutable.Queue[Int]
    val capacity = 10

    (1 to nConsumers).foreach(i => new Consumer(i, buffer).start())
    (1 to nProducers).foreach(i => new Producer(i, buffer, capacity).start())

  }

  multiProdCons(3, 3)
}
