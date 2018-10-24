package example

import cats.effect.IO
import org.atnos.eff.{Eff, Fx2, Safe}
import org.atnos.eff.addon.cats.effect.IOEffect
import org.atnos.eff.all._
import org.atnos.eff.syntax.addon.cats.effect._
import org.atnos.eff.syntax.all._

object SafeEff extends App {

  type Stack = Fx2[Safe, IO]

  case class Resource(isOk: Boolean)

  def open: Eff[Stack, Resource] = protect {
    Resource(false)
  }

  def using(r: Resource): Eff[Stack, String] = protect {
    throw new Error("oh my god")
  }

  def close(r: Resource): Eff[Stack, Unit] =IOEffect.ioDelay {
    throw new Error("oh my godness")
  }

  val a = bracket[Stack, Resource, String, Unit](open)(using)(close).execSafe.unsafeRunSync

  println(a)
}