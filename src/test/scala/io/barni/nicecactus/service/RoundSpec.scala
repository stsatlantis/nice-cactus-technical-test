package io.barni.nicecactus.service

import io.barni.nicecactus.model.Player._
import io.barni.nicecactus.model.ScoreBoard._
import io.barni.nicecactus.model.{ScoreBoard, Turn}
import io.barni.nicecactus.util.CanBeat.syntax._
import org.scalatest.{Matchers, WordSpec}

class RoundSpec extends WordSpec with Matchers {
  val player1: Robot = Bender
  val player2: Robot = Robot("C3PO")
  val dummyScoreBoard = ScoreBoard(player1, player2)
  val drawIncrementedScoreBoard: ScoreBoard = boardDrawScore.modify(_.increment)(dummyScoreBoard)
  val player1WinnerScoreBoard: ScoreBoard = boardPlayer1ScoreLens.modify(_.increment)(dummyScoreBoard)
  val player2WinnerScoreBoard: ScoreBoard = boardPlayer2ScoreLens.modify(_.increment)(dummyScoreBoard)
  "LiveRound" should {
    LiveMoves.moves.foreach { move =>
      s"draw in the same move ($move)" in {
        val turnP1 = Turn(player1, move)
        val turnP2 = Turn(player2, move)
        LiveRound.winner(dummyScoreBoard, turnP1, turnP2).unsafeRun() shouldBe(None, drawIncrementedScoreBoard)
      }

      move.beats.foreach { beatenMove =>
        s"$player1 with $move beats $player2 with $beatenMove" in {
          val turnP1 = Turn(player1, move)
          val turnP2 = Turn(player2, beatenMove)
          LiveRound.winner(dummyScoreBoard, turnP1, turnP2).unsafeRun() shouldBe(Option(player1), player1WinnerScoreBoard)
        }
      }

      LiveMoves.moves.filterNot(m => move.beats.contains(m) || m == move).foreach { winnerMove =>
        s"$player1 with $move loses to $player2 with $winnerMove" in {
          val turnP1 = Turn(player1, move)
          val turnP2 = Turn(player2, winnerMove)
          LiveRound.winner(dummyScoreBoard, turnP1, turnP2).unsafeRun() shouldBe(Option(player2), player2WinnerScoreBoard)
        }
      }
    }
  }
}
