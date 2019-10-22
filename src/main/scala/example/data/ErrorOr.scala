package example.data

import example.typeclass.FlatMap

trait ErrorOr[E, A]

case class ErrorLeft[E, A](error: E) extends ErrorOr[E, A]
case class ErrorRight[E, A](a: A) extends ErrorOr[E, A]

object ErrorOr {

  implicit def flatMapInstance[E]: FlatMap[ErrorOr[E, ?]] = new FlatMap[ErrorOr[E, ?]] {

    override def flatMap[A, B](a: ErrorOr[E, A])(f: A => ErrorOr[E, B]): ErrorOr[E, B] = ???

    override def pure[A](a: A): ErrorOr[E, A] = ???
  }
}
