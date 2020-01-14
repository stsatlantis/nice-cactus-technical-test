package io.barni.nicecactus

import io.barni.nicecactus.util.Parsable
import io.barni.nicecactus.util.Parsable.ParseResult

package object service {

  type AppContext = Console with Random with Moves with Round

  implicit val booleanParsable: Parsable[Boolean] = new Parsable[Boolean] {
    def parse(input: String): ParseResult[Boolean] = input.toUpperCase match {
      case "Y" => Right(true)
      case "N" => Right(false)
      case _   => Left(input)
    }
  }
}
