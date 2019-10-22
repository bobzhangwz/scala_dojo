package example

import org.scalatest._

class HelloSpec extends FlatSpec with Matchers {
  "typeclass syntax " should "import from syntax" in {
    import cats.syntax.all._
    1.some.flatMap(i => Some(i + 1)) should be(Some(2))
  }

  "typeclass instances/syntax" should "be imported from syntax and instances" in {
    import cats.syntax.all._
    import cats.instances.all._

    1.show should be("1")
  }

  "typeclass instances/syntax" should "be imported from implicits" in {
    import cats.implicits._

    1.show should be("1")
  }
}
