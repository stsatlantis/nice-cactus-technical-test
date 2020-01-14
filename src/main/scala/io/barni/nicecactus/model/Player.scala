package io.barni.nicecactus.model

import io.barni.nicecactus.service.{ Console, Moves, Output, Random }
import io.barni.nicecactus.util.Parsable.printInvalid
import io.barni.nicecactus.util.{ CanMove, IO, Parsable }

sealed trait Player extends Product with Serializable {
  def name: String

  final override def toString: String = name
}

object Player {

  val Bender = Robot("Bender")
  val `3CPO` = Robot("3CPO")

  final case class Human(name: String) extends Player

  final case class Robot private[model] (name: String) extends Player

  implicit def humanCanMove(implicit console: Console): CanMove[Human] = new CanMove[Human] {
    def move(input: Human): IO[Move] =
      for {
        _ <- console.putStr("Your move: ")
        moveOpt <- console.getStrLn.map(Parsable[Move].parse(_)).doUntilTap(_.isRight)(printInvalid)
        move = moveOpt.fold[Move](_ => sys.error("Impossible"), identity)
        _ <- console.putStrLn(s"Your move: $move")
      } yield move
  }

  implicit def robotCanMove(
      implicit
      moves: Moves,
      random: Random,
      console: Output
    ): CanMove[Robot] = new CanMove[Robot] {
    def move(input: Robot): IO[Move] =
      for {
        move <- IO.unit(random.nextMove(moves))
        _ <- console.putStrLn(s"$input's move: $move")
      } yield move
  }

}
