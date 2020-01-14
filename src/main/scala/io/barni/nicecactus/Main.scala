package io.barni.nicecactus

import io.barni.nicecactus.model.Player.Human
import io.barni.nicecactus.service.{ AppContext, Game, LiveConsole, LiveMoves, LiveRandom, LiveRound }

object Main extends App {


  implicit val console: AppContext = new LiveConsole with LiveRandom with LiveMoves with LiveRound

  (for {
    _ <- new Game().play
  } yield ()).unsafeRun()
}
