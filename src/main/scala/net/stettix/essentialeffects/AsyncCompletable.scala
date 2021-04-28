package net.stettix.essentialeffects

import cats.effect._
import net.stettix.essentialeffects.debug._

import java.util.concurrent.CompletableFuture
import scala.jdk.FunctionConverters._

object AsyncCompletable extends IOApp.Simple {

  override def run: IO[Unit] =
    effect.debug.void

  val effect: IO[String] =
    fromCF(IO(cf()))

  def fromCF[A](cfa: IO[CompletableFuture[A]]): IO[A] =
    cfa.flatMap(
      fa => {
        IO.async_[A] { cb =>
          val handler: (A, Throwable) => Unit = (result: A, error: Throwable) => {
            if (result != null)
              cb(Right(result))
            else
              cb(Left(error))

            ()
          }

          fa.handle(handler.asJavaBiFunction)
        }
      }
    )

  def cf(): CompletableFuture[String] =
    CompletableFuture.supplyAsync(() => "async operation")

}
