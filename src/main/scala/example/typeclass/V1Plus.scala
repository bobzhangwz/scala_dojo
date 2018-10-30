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
  // def plus(a1: CanPlus, a2: CanPlus): CanPlus = a1.plus(a2)

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

  implicit def optionMonoid[A : Monoid]= new Monoid[Option[A]] {
    override def combine(a: Option[A], a1: Option[A]): Option[A] = (a, a1) match {
      case (_, None) => None
      case (None, _) => None
      case (Some(a), Some(a1)) => Some(implicitly[Monoid[A]].combine(a, a1))
    }

    override def empty: Option[A] = Some(implicitly[Monoid[A]].empty)
  }

  def sum[A](xs: List[A])(implicit m: Monoid[A]): A = xs.foldLeft(m.empty)(m.combine)

//  import cats.instances.all._
//  import cats.syntax.all._

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
    def map[A, B](a: M[A], func: A => B): M[B]
  }

  trait Applicative[M[_]] {
// def ap[A, B](ff: M[A => B])(fa: M[A]): M[B]
// def pure[A](a: => A): M[A]  // lift
    def map2[A, B, C](fa: M[A], fb: M[B])(f: (A, B) => C): M[C] // Some((Int, Int)=> Int)
    // def map3 map4 map5
  }

  // A monad is a mechanism for sequencing computations.
  trait Monad[M[_]] {
    def flatMap[A, B](a: M[A], func: A => M[B]): M[B]
  }

}

object V6Plus {
  import cats.Monad
  import scala.annotation.tailrec

  val optionMonad = new Monad[Option] {
    def flatMap[A, B](opt: Option[A])(fn: A => Option[B]): Option[B] = opt flatMap fn

    def pure[A](opt: A): Option[A] = Some(opt)
    //  The tailRecM method is an optimisaton used in Cats to limit the amount
    //  of stack space consumed by nested calls to flatMap. The technique comes
    //  from a 2015 paper by PureScript creator Phil Freeman. The method should
    //  recursively call itself unl the result of fn returns a Right.
    //    If we can make tailRecM tail-recursive, Cats is able to guarantee stack safety
    //  in recursive situations such as folding over large lists. If we
    //    can’t make tailRecM tail-recursive, Cats cannot make these guarantees and
    //    extreme use cases may result in StackOverflowErrors.
    @tailrec
    def tailRecM[A, B](a: A)(fn: A => Option[Either[A, B]]): Option[B] =
      fn(a) match {
        case None => None
        case Some(Left(a1)) => tailRecM(a1)(fn)
        case Some(Right(b)) => Some(b)
      }
  }
}

object V7Plus {
  sealed trait NewOption[+T]
  case class NewSome[+T](value: T) extends NewOption[T]
  case object NewNone extends NewOption[Nothing]

  import cats.Monad
//  import cats.instances.option._

  def transform[A](newOpt: NewOption[A]): Option[A]  = newOpt match {
    case NewSome(value) => Some(value)
    case NewNone => None
  }

  val newOptionMonad = new Monad[NewOption] {
    override def flatMap[A, B](fa: NewOption[A])(f: A => NewOption[B]): NewOption[B] = ???
    override def pure[A](x: A): NewOption[A] = ???
    override def tailRecM[A, B](a: A)(f: A => NewOption[Either[A, B]]): NewOption[B] = ???
  }
  // https://typelevel.org/cats/datatypes/freemonad.html
}