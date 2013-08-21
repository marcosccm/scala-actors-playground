import sbt._
import sbt.Keys._

object AvionicsBuild extends Build {

  lazy val avionics = Project(
    id = "avionics",
    base = file("."),
    settings = Project.defaultSettings ++ Seq(
      name := "Avionics",
      organization := "trem.akka.avionics",
      version := "0.1-SNAPSHOT",
      scalaVersion := "2.10.0",
      scalacOptions ++= Seq("-feature", "-deprecation"),
      resolvers += "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases",
      libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.1.2"
    )
  )
}
