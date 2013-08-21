package trem.akka.avionics 

import akka.actor._
import akka.util.Timeout
import scala.concurrent.duration._

object Altimeter {
  case class RateChange(amount: Float)
  case class AltimeterUpdate(altitude: Double)
  def apply() = new Altimeter with RealEventSource
}

class Altimeter extends Actor {
  this: EventSource =>
  import Altimeter._

  implicit val ec = context.dispatcher
  
  val ceiling = 43000
  val maxRateOfClimb = 500
  var rateOfClimb = 0f
  var altitude = 0d
  var lastTick = System.currentTimeMillis
  
  val ticker = context.system.scheduler.schedule(100.millis, 100.millis, self, Tick)

  case object Tick

  def altimeterReceive: Receive = {
    case RateChange(amount) =>
      rateOfClimb = amount.min(1.0f).max(-1.0f) * maxRateOfClimb
      println(s"climb $rateOfClimb")
    case Tick =>
      val tick = System.currentTimeMillis
      altitude = altitude + ((tick - lastTick) / 60000.0) * rateOfClimb
      lastTick = tick
      sendEvent(AltimeterUpdate(altitude))
  }

  def receive = eventSourceReceive orElse altimeterReceive

  override def postStop() : Unit = ticker.cancel
}
