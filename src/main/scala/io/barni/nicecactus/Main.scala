package io.barni.nicecactus

import io.barni.nicecactus.model.Player.Human
import io.barni.nicecactus.service.{Console, Game, LiveConsole, LiveMoves, LiveRandom, LiveRound, Moves, Random}

object Main extends App {

  implicit val console: LiveConsole with LiveRandom with LiveMoves with LiveRound = new LiveConsole with LiveRandom with LiveMoves with LiveRound

  val barni = Human("Barni")
  (for {
    _ <- Game()
  } yield ()).unsafeRun()
}
