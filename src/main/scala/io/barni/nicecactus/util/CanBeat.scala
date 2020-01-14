package io.barni.nicecactus.util

import io.barni.nicecactus.model.Move
import shapeless.{ :+:, CNil, Coproduct, Generic, Inl, Inr, Lazy }

trait CanBeat[T] {
  def beats(input: T): Set[Move]
}

object CanBeat {

  def apply[T](implicit instance: CanBeat[T]): CanBeat[T] = instance

  object syntax {

    class CanBeatOps[T](self: T) {
      def beats(implicit instance: CanBeat[T]): Set[Move] = instance.beats(self)
    }

    implicit def liftCanBeatOps[T](input: T): CanBeatOps[T] = new CanBeatOps(input)
  }

  implicit def cnilCanBeat: CanBeat[CNil] = (_: CNil) => Set.empty[Move]

  implicit def clistCanBeat[T, C <: Coproduct](
      implicit
      hBeats: CanBeat[T],
      tBeats: Lazy[CanBeat[C]]
    ): CanBeat[T :+: C] = {
    case Inl(h) => hBeats.beats(h)
    case Inr(t) => tBeats.value.beats(t)
  }

  implicit def genericCanBeat[A, C](
      implicit
      g: Generic.Aux[A, C],
      b: Lazy[CanBeat[C]]
    ): CanBeat[A] = new CanBeat[A] {
    def beats(input: A): Set[Move] = b.value.beats(g.to(input))
  }

}
