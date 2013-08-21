package trem.akka.avionics

import akka.actor._

object Plane {
  case object GiveMeControl
}

class Plane extends Actor {
  import Altimeter._
  import Plane._
  import EventSource._

  val altimeter = context.actorOf(Props(Altimeter()), "altimeter")
  val control = context.actorOf(Props(new ControlSurfaces(altimeter)), "controls")

  override def preStart() {
    altimeter ! RegisterListener(self)
  }

  def receive = {
    case AltimeterUpdate(altitude) =>
      println(s"Our current height is $altitude")
    case GiveMeControl =>
      println("Plane giving control")
      sender ! control
  }
}

