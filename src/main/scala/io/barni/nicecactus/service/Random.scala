package io.barni.nicecactus.service

import io.barni.nicecactus.model.Move

import scala.util.{ Random => Rand }

trait Random {
  def nextMove(moves: Moves): Move
}

trait LiveRandom extends Random {
  final def nextMove(moves: Moves): Move = Rand.shuffle(moves.moves).toList.head
}

object LiveRandom extends LiveRandom
