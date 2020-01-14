package io.barni.nicecactus.util

import java.util.concurrent.atomic.AtomicInteger

import io.barni.nicecactus.model.Move
import io.barni.nicecactus.model.Move._
import io.barni.nicecactus.model.Player.{Human, Robot}
import io.barni.nicecactus.service.{Console, Moves, Output, Random}
import io.barni.nicecactus.util.CanMove.syntax._
import org.scalatest.{Matchers, WordSpec}

class CanMoveSpec extends WordSpec with Matchers {
  "Human moves based on input" should {
    val Barni = Human("Barni")
    "return Rock" in {
      implicit val console: Console = new Console with NoOutput {

        override def getStrLn: IO[String] = IO.unit("rock")

      }
      CanMove[Human].move(Barni).unsafeRun() shouldBe Rock
      Barni.move.unsafeRun() shouldBe Rock
    }

    "return Paper" in {
      implicit val console: Console = new Console with NoOutput {
        override def getStrLn: IO[String] = IO.unit("paper")
      }
      CanMove[Human].move(Barni).unsafeRun() shouldBe Paper
      Barni.move.unsafeRun() shouldBe Paper
    }

    "return Scissors" in {
      implicit val console: Console = new Console with NoOutput {

        override def getStrLn: IO[String] = IO.unit("Scissors")
      }
      CanMove[Human].move(Barni).unsafeRun() shouldBe Scissors
      Barni.move.unsafeRun() shouldBe Scissors
    }
    "retry on invalid input" in {
      val inputCounter = new AtomicInteger(-1)
      val inputs: List[String] = List("q", "s")

      implicit val console: Console = new Console with NoOutput{

        override def getStrLn: IO[String] = {
          IO.unit({
            val index = inputCounter.incrementAndGet()
            inputs(index)
          })
        }
      }
      CanMove[Human].move(Barni).unsafeRun() shouldBe Scissors
      inputCounter.get() shouldBe 1
    }
  }

  "Robot produces move" in {
    val Bender = Robot("Bender")
    implicit val rockMoves: Moves = new Moves {
      override def moves: Set[Move] = Set(Rock)
    }
    implicit val random: Random = new Random {
      override def nextMove(moves: Moves): Move = moves.moves.toList.head
    }
    implicit val console: Output = new Output {
      override def putStrLn(line: String): IO[Unit] = IO.unit(println(line))

      override def putStr(line: String): IO[Unit] = IO.unit(print(line))
    }

    CanMove[Robot].move(Bender).unsafeRun() shouldBe Rock
  }

  trait NoOutput extends Output {
    override def putStrLn(line: String): IO[Unit] = IO.unit(println(line))

    override def putStr(line: String): IO[Unit] = IO.unit(print(line))
  }

}
