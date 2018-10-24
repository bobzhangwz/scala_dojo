package example

import org.atnos.eff.Fx2
import cats.effect.IO
import org.atnos.eff.all._
import org.atnos.eff.syntax.addon.cats.effect._
import org.atnos.eff.syntax.all._
import org.atnos.eff.ErrorEffect._
import org.atnos.eff.addon.cats.effect.IOEffect
import org.atnos.eff.Eff

object ErrorEff extends App {
  type Stack = Fx2[ErrorOrOk, IO]
  
  def using: Eff[Stack, String] = IOEffect.ioDelay {
    throw new Exception("oh my god")
  }
  
  def close: Eff[Stack, Unit] =IOEffect.ioDelay {
    println("hell==========================>")
  }
  
  val _ = andFinally[Stack, String](using, close).runError.unsafeRunSync
}