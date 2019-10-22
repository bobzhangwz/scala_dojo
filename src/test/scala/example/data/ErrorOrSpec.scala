package example.data

import org.scalatest._
import example.typeclass.syntax._

class ErrorOrSpec extends FlatSpec with Matchers {
  "ErrorOr" should "flatMap" in {
    val errorOr: ErrorOr[Throwable, Int] = ErrorRight(1)
    errorOr.flatMap(i => ErrorRight(1 + i)) should be(ErrorRight(2))

    (ErrorLeft(new Error("...")): ErrorOr[Throwable, Int]).flatMap(i => ErrorRight(1 + i)) should matchPattern {
      case ErrorLeft(_) =>
    }
  }

  "ErrorOr" should "map" in {
    val errorOr: ErrorOr[Throwable, Int] = ErrorRight(1)
//    errorOr.map(i => 1 + i) should be(ErrorRight(2))
//
//    (ErrorLeft(new Error("...")): ErrorOr[Throwable, Int]).map(i => 1 + i) should matchPattern {
//      case ErrorLeft(_) =>
//    }
  }

  "Error" should "using for" in {
//    val a = for {
//      i <- ErrorRight(1): ErrorOr[Throwable, Int]
//      j <- ErrorRight(2): ErrorOr[Throwable, Int]
//    } yield i + j
//
//    a should be(ErrorRight(3))
  }
}
