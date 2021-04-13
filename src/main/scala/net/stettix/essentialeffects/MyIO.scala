package net.stettix.essentialeffects

case class MyIO[A] (unsafeRun: () => A) {

  def map[B](f: A => B): MyIO[B] = {
    MyIO(() => f(unsafeRun()))
  }

  def flatMap[B](f: A => MyIO[B]): MyIO[B] =
    MyIO(() => f(unsafeRun()).unsafeRun())

}

object MyIO {
  def putStr(s: => String): MyIO[Unit] =
    MyIO(() => println(s))
}
