package io.barni.nicecactus.model

import io.barni.nicecactus.util.Parsable.ParseResult
import io.barni.nicecactus.util.{CanBeat, Parsable}

sealed trait Move extends Product with Serializable

object Move {

  case object Paper extends Move

  case object Scissors extends Move

  case object Rock extends Move

  implicit val moveParse: Parsable[Move] = new Parsable[Move] {

    val moveMapping: PartialFunction[String, Move] = {
      case "P" | "PAPER" => Paper
      case "R" | "ROCK" => Rock
      case "S" | "SCISSORS" => Scissors
    }

    override def parse(input: String): ParseResult[Move] = for {
      str <- Option(input).toRight("null")
        move <- moveMapping.lift(str.toUpperCase).toRight(input)
    } yield move

  }

  implicit val paperBeats: CanBeat[Paper.type] = (input: Paper.type) => Set(Rock)

  implicit val scissorsBeats: CanBeat[Scissors.type] = (input: Scissors.type) => Set(Paper)

  implicit val rockBeats: CanBeat[Rock.type] = (input: Rock.type) => Set(Scissors)

}
