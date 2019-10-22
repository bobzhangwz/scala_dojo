package example.typeclass

trait FlatMap[F[_]] {
  def flatMap[A, B](a: F[A])(f: A => F[B]): F[B]
  def pure[A](a: A): F[A]
}
