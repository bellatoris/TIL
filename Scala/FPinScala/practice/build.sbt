import Dependencies._


lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "doogie.scala",
      scalaVersion := "2.12.4"
    )),
    name := "fpinscala",
    libraryDependencies += "org.typelevel" %% "cats" % "0.9.0",
    libraryDependencies += scalaCheck % "test"
  )
