package net.stettix.essentialeffects

import cats.effect.{IO, IOApp}
import net.stettix.essentialeffects.debug._

object Never extends IOApp.Simple {

  override def run: IO[Unit] =
    never.guarantee(IO("Well I never!").debug.void)

  val never: IO[Nothing] =
    IO.async_(_ => ())

}
