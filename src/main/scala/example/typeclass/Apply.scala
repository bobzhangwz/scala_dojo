package example.typeclass

trait Apply[F[_]] {
  def map[A, B](mapValue: F[A])(f: A => B): F[B]
}
