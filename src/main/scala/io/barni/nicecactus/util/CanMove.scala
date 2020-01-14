package io.barni.nicecactus.util

import io.barni.nicecactus.model.Move
import shapeless.{ :+:, CNil, Coproduct, Generic, Inl, Inr, Lazy }

trait CanMove[T] {
  def move(input: T): IO[Move]
}

object CanMove {

  def apply[T](implicit instance: CanMove[T]): CanMove[T] = instance

  object syntax {

    class CanMoveOps[T](self: T) {
      def move(implicit instance: CanMove[T]): IO[Move] = instance.move(self)
    }

    implicit def liftCanMoveOps[T](input: T): CanMoveOps[T] = new CanMoveOps(input)
  }

  implicit def cnilCanMove: CanMove[CNil] = new CanMove[CNil] {
    def move(input: CNil): IO[Move] = ???
  }

  implicit def clistCanMove[T, C <: Coproduct](
      implicit
      hCanMove: CanMove[T],
      tCanMove: Lazy[CanMove[C]]
    ): CanMove[T :+: C] = {
    case (Inl(h)) => hCanMove.move(h)
    case (Inr(t)) => tCanMove.value.move(t)
  }

  implicit def genericCanMove[A, C](
      implicit
      g: Generic.Aux[A, C],
      b: Lazy[CanMove[C]]
    ): CanMove[A] = new CanMove[A] {
    def move(input: A): IO[Move] = b.value.move(g.to(input))
  }
}
