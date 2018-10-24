package example.typeclass

// class Person(weight: Int)
// 多态,

object V0 {
  // 类型多态
  def head[A](xs: List[A]): A = xs(0)
}

object V1Plus {

  trait CanPlus[A] {
    def plus(a: A) : A
  }
  // 子类型多态
  def plus[A <: CanPlus[A]](a1: A, a2: A): A = a1.plus(a2)

}

object V2Plus {
  // 集中多态 type class
  trait CanPlus[A] {
    def plus(a: A, a1: A) : A
  }
  def plus[A : CanPlus](a1: A, a2: A): A = implicitly[CanPlus[A]].plus(a1, a2)

}

object V3Plus {
  trait CanPlus[A] {
    def plus(a: A, a1: A) : A
  }

  object CanPlusSyntax {
    implicit class CanPlusOps[A: CanPlus](a: A) {
      def plus(a1: A): A = implicitly[CanPlus[A]].plus(a, a1)
      val |+| = plus _
    }
  }

  object CanPlusInstances {
    implicit val intPlus = new CanPlus[Int] {
      override def plus(a: Int, a1: Int): Int = a + a1
    }

    implicit val stringPlus = new CanPlus[String] {
      override def plus(a: String, a1: String): String = a + a1
    }
  }

  def main(args: Array[String]): Unit = {
    import CanPlusInstances._
    import CanPlusSyntax._

    val _ = 1.plus(2)
    val _ = 1 |+| 2
  }
}

object V4Plus {
  trait Monoid[A] {
    def combine(a: A, a1: A): A
    def empty: A
  }

  object MonoidSyntax {
    implicit class MoinoidOps[A : Monoid](a: A) {
      def |+|(a1: A): A = implicitly[Monoid[A]].combine(a, a1)
    }
  }

  def sum[A](xs: List[A])(implicit m: Monoid[A]): A = xs.foldLeft(m.empty)(m.combine)

//  import cats.instances.all._
//  import cats.syntax.all._
//
//  import cats.instances.option._
//  import cats.syntax.option._

//  import cats.Order
//  import cats.Eq
//  import cats.Monoid


//  import cats.Functor
//  import cats.Applicative
//  import cats.Monad
//  import cats.Foldable
}

object V5Plus {
  trait Functor[M[_]] {
    def map[A, B](a: M[A]): M[B]
  }

  trait Applicative[M[_]] {
//    def ap[A, B](ff: M[A => B])(fa: M[A]): M[B]
//    def pure[A](a: => A): M[A]  // lift
    def map2[A, B, C](fa: M[A], fb: M[B])(f: (A, B) => C): M[C] // Some((Int, Int)=> Int)
    // def map3 map4 map5
  }

}

