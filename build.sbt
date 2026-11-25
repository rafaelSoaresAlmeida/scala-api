name := """scala-api"""
organization := "com.api"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.18"

libraryDependencies += guice
libraryDependencies += "com.github.jwt-scala" %% "jwt-play-json" % "11.0.3"
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "6.0.2" % Test
libraryDependencies += "org.playframework" %% "play-slick" % "6.2.0"
libraryDependencies += "org.playframework" %% "play-slick-evolutions" % "6.2.0"

libraryDependencies ++= Seq(
  evolutions, // Optional: required if using Play Evolutions
  "com.h2database" % "h2" % "2.5.252" // Use % Test if only needed for unit tests
)


PlayKeys.playDefaultPort := 8080
