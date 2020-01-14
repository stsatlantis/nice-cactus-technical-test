package io.barni.nicecactus.model

import io.barni.nicecactus.util.Parsable
import io.barni.nicecactus.util.Parsable.ParseResult


sealed trait PlayerSelectCommand extends Product with Serializable

object PlayerSelectCommand {

  case object HumanVsRobot extends PlayerSelectCommand

  case object RobotVsRobot extends PlayerSelectCommand

  implicit val parsePlayerSelect: Parsable[PlayerSelectCommand] = new Parsable[PlayerSelectCommand] {
    override def parse(input: String): ParseResult[PlayerSelectCommand] = input match {
      case "1" => Right(HumanVsRobot)
      case "2" => Right(RobotVsRobot)
      case _ => Left(input)

    }
  }
}
