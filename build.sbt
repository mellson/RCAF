name := "RCAF"

version := "0.1"

scalaVersion := "2.11.0"

libraryDependencies ++= Seq(
  "com.netflix.rxjava" % "rxjava-scala" % "+",
  "com.typesafe.akka" %% "akka-actor" % "+",
  "com.typesafe.akka" %% "akka-remote" % "+"
)