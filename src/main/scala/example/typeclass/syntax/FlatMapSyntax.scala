package example.typeclass.syntax

import example.typeclass.FlatMap

trait FlatMapSyntax {
  implicit class FlatMapOps[F[_], A](instance: F[A]) {
    def flatMap[B](f: A => F[B])(implicit fm: FlatMap[F]): F[B] = ???

//    def map[B](f: A => B)(implicit fm: FlatMap[F]): F[B] = flatMap(f andThen fm.pure[B])
  }
}

object FlatMapSyntax extends FlatMapSyntax
