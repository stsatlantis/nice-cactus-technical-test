package io.barni.nicecactus.util.lens

case class Lens[A, B](get: A => B, set: (A, B) => A) {
  self =>

  def compose[C](other: Lens[B, C]): Lens[A, C] = Lens(
    self.get andThen other.get,
    (a, c) => self.set(a, other.set(self.get(a), c))
  )

  def modify(f: B => B): A => A = a => self.set(a, (self.get andThen f) (a))

}
