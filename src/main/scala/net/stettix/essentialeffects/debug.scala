package net.stettix.essentialeffects

import cats.effect._

object debug {

  implicit class DebugHelper[A](ioa: IO[A]) {

    /** Evaluate effect and show result together with thread name. */
    def debug: IO[A] =
      for {
        a <- ioa
        tn = Thread.currentThread.getName
        _ = println(s"[$tn] $a")
      } yield a
  }

}
