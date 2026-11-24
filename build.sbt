name := """scala-api"""
organization := "com.api"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.18"

libraryDependencies += guice


//libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.2" % Test
//libraryDependencies += "org.playframework" %% "play-slick" % "7.0.0-M1"
//libraryDependencies += "org.playframework" %% "play-slick-evolutions" % "7.0.0-M1"

libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "6.0.2" % Test
libraryDependencies += "org.playframework" %% "play-slick" % "6.2.0"
libraryDependencies += "org.playframework" %% "play-slick-evolutions" % "6.2.0"


//libraryDependencies += "mysql" % "mysql-connector-java" % "8.0.33"
//libraryDependencies += "com.typesafe.play" %% "play-slick" % "5.4.0"
//libraryDependencies += "com.typesafe.play" %% "play-slick-evolutions" % "5.4.0"


libraryDependencies ++= Seq(
 // jdbc,
  evolutions, // Optional: required if using Play Evolutions
  "com.h2database" % "h2" % "2.5.252" // Use % Test if only needed for unit tests
)


PlayKeys.playDefaultPort := 8080

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.api.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.api.binders._"
