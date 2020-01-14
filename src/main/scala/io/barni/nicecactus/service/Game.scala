package io.barni.nicecactus.service

import io.barni.nicecactus.model.Player.{Bender, Human, `3CPO`}
import io.barni.nicecactus.model.PlayerSelectCommand.{HumanVsRobot, RobotVsRobot}
import io.barni.nicecactus.model.ScoreBoard._
import io.barni.nicecactus.model.{Player, PlayerSelectCommand, ScoreBoard, Turn}
import io.barni.nicecactus.util.CanMove.syntax._
import io.barni.nicecactus.util.{IO, Parsable}
import io.barni.nicecactus.util.Parsable.printInvalid

object Game {
  def apply()(
    implicit
    console: Console,
    random : Random,
    moves  : Moves,
    roundService: Round
  ): IO[Unit] = for {
    _ <- printWelcome
      scoreboard <- selectMode
      _ <- gameLoop(scoreboard)
  } yield ()

  private def selectMode(
    implicit
    console: Console
  ): IO[ScoreBoard] = for {
    _ <- console.putStrLn("Bender is ready to play")
      _ <- console.putStrLn(
        """
          |Would like to play against Bender or watch other players?
          |1 - I want to play against Bender
          |2 - Watch other players""".stripMargin)
      input <- console.getStrLn.map(Parsable[PlayerSelectCommand].parse(_)).doUntilTap(_.isRight)(printInvalid)
      scoreboard = input match {
        case Right(HumanVsRobot) => ScoreBoard(Human("You"), Bender)
        case Right(RobotVsRobot) => ScoreBoard(Bender, `3CPO`)
        case _ : Left[String, PlayerSelectCommand] => sys.error("Impossible")
      }
  } yield scoreboard

  private def gameLoop(scoreBoard: ScoreBoard)(
    implicit
    console: Console,
    random : Random,
    moves  : Moves,
    roundService: Round
  ): IO[Unit] = {

    def loop(round: Int, scoreBoard: ScoreBoard): IO[Unit] = for {
      _ <- console.putStrLn(s"Round $round")
        p1Move <- boardPlayer1Lens.get(scoreBoard).move.map(Turn(scoreBoard.score1.player, _))
        p2Move <- boardPlayer2Lens.get(scoreBoard).move.map(Turn(scoreBoard.score2.player, _))
        playerScore <- roundService.winner(scoreBoard, p1Move, p2Move) // with the better-monadic-for plugin this should be detupled to (winner, scoreBoard)
        _ <- printWinner(playerScore._1)
        _ <- printScoreBoard(playerScore._2)
        isNewRound <- checkNewRound
        _ <- if (isNewRound) loop(round + 1, playerScore._2)
        else IO.unit(())
    } yield ()

    loop(1, scoreBoard)
  }

  private def printWinner(player     : Option[Player])(implicit console: Output): IO[Unit] = {
    player match {
      case Some(p) => console.putStrLn(s"The winner of the round is: ${p}")
      case None => console.putStrLn("Draw")
    }
  }

  private def printScoreBoard(scoreBoard: ScoreBoard)(implicit console: Output): IO[Unit] = for {
    _ <- console.putStrLn(
      s"""${boardPlayer1Lens.get(scoreBoard)} - ${boardPlayer1ScoreLens.get(scoreBoard)}
         |${boardPlayer2Lens.get(scoreBoard)} - ${boardPlayer2ScoreLens.get(scoreBoard)}
         |Draws - ${boardDrawScore.get(scoreBoard)}""".stripMargin.replace("\n", "\t\t\t"))
  } yield ()

  private def checkNewRound(implicit console: Console): IO[Boolean] = for {
    _ <- console.putStrLn("Do you want to play an extra round? (Y/y - Yes, N/n - No)")
      input <- console.getStrLn.map(Parsable[Boolean].parse(_)).doUntilTap(_.isRight)(printInvalid)
      move = input.fold[Boolean](_ => sys.error("Impossible"), identity)
  } yield move

  private def printWelcome(implicit console     : Output, moves: Moves): IO[Unit] = for {
    _ <- console.putStrLn("Welcome to the Game")
      ms = moves.moves.map {
        e =>
          val str = e.toString
          val options = List(str.capitalize, str.toLowerCase, str.take(1).toUpperCase, str.take(1).toLowerCase()).mkString("/")
          s"$str - $options"
      }.mkString(", ")
      _ <- console.putStrLn(s"The followings are the valid moves: $ms")
  } yield ()

}
