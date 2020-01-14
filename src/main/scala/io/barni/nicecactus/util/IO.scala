package io.barni.nicecactus.util

import scala.annotation.tailrec

case class IO[A](unsafeRun: () => A) {
  self =>

  def map[B](f: A => B): IO[B] = IO.unit(f(self.unsafeRun()))

  def flatMap[B](f: A => IO[B]): IO[B] = IO.unit(f(self.unsafeRun()).unsafeRun())

  final def doUntil(p: A => Boolean):IO[A] = self.doUntilTap(p)(_ => ())

  @tailrec
  final def doUntilTap(p: A => Boolean)(f: A => Unit): IO[A] = {
    val result = self.unsafeRun()
    if (p(result)) IO.unit(result)
    else {
      f(result)
      doUntilTap(p)(f)
    }
  }

}

object IO {
  def unit[A](a: => A): IO[A] = IO(() => a)
}
