import Dependencies._

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "com.example",
      scalaVersion := "2.12.3",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "Hello",
    libraryDependencies += scalaTest % Test,
    libraryDependencies += "org.typelevel" %% "cats-core" % "1.0.0-MF",
    libraryDependencies += "org.typelevel" %% "cats-macros" % "1.0.0-MF",
    libraryDependencies += "org.typelevel" %% "cats-kernel" % "1.0.0-MF"
  )
