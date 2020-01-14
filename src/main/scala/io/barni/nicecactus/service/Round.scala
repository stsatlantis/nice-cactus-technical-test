package io.barni.nicecactus.service

import io.barni.nicecactus.model.ScoreBoard._
import io.barni.nicecactus.model.{Player, ScoreBoard, Turn}
import io.barni.nicecactus.util.CanBeat.syntax._
import io.barni.nicecactus.util.IO

trait Round {

  protected def chooseWinner(turn1: Turn, turn2: Turn): IO[Option[Player]]

  final def winner(scoreBoard: ScoreBoard, turn1: Turn, turn2: Turn): IO[(Option[Player], ScoreBoard)] = for {
    player <- chooseWinner(turn1, turn2)
      newScoreBoard = updateScoreBoard(scoreBoard, player)
  } yield (player, newScoreBoard)


  private def updateScoreBoard(scoreBoard: ScoreBoard, player: Option[Player]): ScoreBoard = {
    player match {
      case Some(player) if boardPlayer1Lens.get(scoreBoard) == player => boardPlayer1ScoreLens.modify(_.increment)(scoreBoard)
      case Some(player) if boardPlayer2Lens.get(scoreBoard) == player => boardPlayer2ScoreLens.modify(_.increment)(scoreBoard)
      case None => boardDrawScore.modify(_.increment)(scoreBoard)
    }
  }
}

trait LiveRound extends Round {
  final protected def chooseWinner(turn1: Turn, turn2: Turn): IO[Option[Player]] =
    (turn1, turn2) match {
      case (Turn(p1, m1), Turn(p2, m2)) => if (m1 == m2) {
        IO.unit(None)
      }
      else if (m1.beats.contains(m2)) {
        IO.unit(Option(p1))
      }
      else {
        IO.unit(Option(p2))
      }
    }
}

object LiveRound extends LiveRound