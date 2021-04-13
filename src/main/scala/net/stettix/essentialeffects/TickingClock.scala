package net.stettix.essentialeffects

import cats.effect.{IO, IOApp}

import scala.annotation.tailrec
import scala.concurrent.duration._

object TickingClock extends IOApp.Simple {

  override def run: IO[Unit] =
    tickingClock

  @tailrec
  val tickingClock: IO[Unit] = {

    val tick: IO[Unit] = for {
      time <- IO.realTimeInstant
      _ <- IO.println(time)
      _ <- IO.sleep(1.second)
    } yield ()

    tick >> tickingClock
  }

}
