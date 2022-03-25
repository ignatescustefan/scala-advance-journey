package lectures.part3concurrency

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

  smartProducerConsumer()
}
