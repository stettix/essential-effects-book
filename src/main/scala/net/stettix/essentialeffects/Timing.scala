package net.stettix.essentialeffects

import scala.concurrent.duration.{FiniteDuration, MILLISECONDS}

object Timing {

  val clock: MyIO[Long] = MyIO(() => System.currentTimeMillis())

  def time[A](action: MyIO[A]): MyIO[(FiniteDuration, A)] =
    for {
      start <- clock
      value <- action
      end <- clock
    } yield (FiniteDuration(end - start, MILLISECONDS), value)

  def main(args: Array[String]): Unit = {

    val timedHello = Timing.time(MyIO.putStr("hello"))

    timedHello.unsafeRun() match {
      case (duration, _) => println(s"'hello' took $duration")
    }
  }

}
