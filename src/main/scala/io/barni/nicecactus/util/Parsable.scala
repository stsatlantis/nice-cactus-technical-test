package io.barni.nicecactus.util

import io.barni.nicecactus.service.Output
import io.barni.nicecactus.util.Parsable.ParseResult

trait Parsable[T] {
  def parse(input: String): ParseResult[T]
}

object Parsable {
  type ParseResult[T] = Either[String,T]
  def apply[T](implicit instance: Parsable[T]): Parsable[T] = instance

  def printInvalid[T](input: ParseResult[T])(implicit console: Output): Unit = input match {
    case Left(input) => console.putStr(s"Invalid input ('$input') try again: ").unsafeRun()
    case _: Right[String,T] => ()
  }
}
