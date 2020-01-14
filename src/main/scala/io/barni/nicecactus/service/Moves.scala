package io.barni.nicecactus.service

import io.barni.nicecactus.model.Move
import io.barni.nicecactus.model.Move.{Paper, Rock, Scissors}

trait Moves {
  def moves: Set[Move]
}

trait LiveMoves extends Moves {
  final def moves: Set[Move] = Set(Rock, Paper, Scissors) // Todo: iterating over the Move Coproduct to get the values
}

object LiveMoves extends LiveMoves