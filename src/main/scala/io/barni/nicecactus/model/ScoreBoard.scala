package io.barni.nicecactus.model

import io.barni.nicecactus.util.lens.Lens

import scala.language.implicitConversions

final class ScoreValue(private val value: Int) extends AnyVal {
  def increment = new ScoreValue(value + 1)
  override def toString: String = value.toString
}

object ScoreValue {

  class ScoreValueOps(private val value: Int) {
    def toScore = new ScoreValue(value)
  }

  implicit def scoreValueOps(int: Int): ScoreValueOps = new ScoreValueOps(int)
}

final case class PlayerScore(player: Player, score: ScoreValue)

object PlayerScore {

  import ScoreValue._

  def apply(player: Player): PlayerScore = new PlayerScore(player, 0.toScore)

  val scoreLens: Lens[PlayerScore, ScoreValue] =
    Lens[PlayerScore, ScoreValue](_.score, (score, valueValue) => score.copy(score = valueValue))
  val playerLens: Lens[PlayerScore, Player] =
    Lens[PlayerScore, Player](_.player, (score, player) => score.copy(player = player))
}

final case class ScoreBoard(
    score1: PlayerScore,
    score2: PlayerScore,
    draws: ScoreValue)

object ScoreBoard {

  import ScoreValue._

  def apply(p1: Player, p2: Player): ScoreBoard = new ScoreBoard(PlayerScore(p1), PlayerScore(p2), 0.toScore)

  private[this] val _boardPlayer1Lens: Lens[ScoreBoard, PlayerScore] =
    Lens[ScoreBoard, PlayerScore](_.score1, (b, s) => b.copy(score1 = s))

  private[this] val _boardPlayer2Lens: Lens[ScoreBoard, PlayerScore] =
    Lens[ScoreBoard, PlayerScore](_.score2, (b, s) => b.copy(score2 = s))

  val boardPlayer1Lens: Lens[ScoreBoard, Player] = _boardPlayer1Lens compose PlayerScore.playerLens
  val boardPlayer2Lens: Lens[ScoreBoard, Player] = _boardPlayer2Lens compose PlayerScore.playerLens
  val boardPlayer1ScoreLens: Lens[ScoreBoard, ScoreValue] = _boardPlayer1Lens compose PlayerScore.scoreLens
  val boardPlayer2ScoreLens: Lens[ScoreBoard, ScoreValue] = _boardPlayer2Lens compose PlayerScore.scoreLens
  val boardDrawScore: Lens[ScoreBoard, ScoreValue] = Lens[ScoreBoard, ScoreValue](_.draws, (b, s) => b.copy(draws = s))
}
