name := "RCAF"

version := "0.1"

scalaVersion := "2.11.0"

resolvers += "spray repo" at "http://repo.spray.io"

libraryDependencies ++= Seq(
  "org.scala-lang.modules" %% "scala-swing" % "+",
  "com.typesafe.akka" %% "akka-actor" % "+",
  "com.typesafe.akka" %% "akka-remote" % "+"
)