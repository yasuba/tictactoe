ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"

lazy val root = (project in file("."))
  .settings(
    name := "tictactoe",
    libraryDependencies += "org.typelevel" %% "cats-core" % "2.8.0",
    libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.15",
    libraryDependencies += "org.typelevel" %% "cats-effect" % "3.5.2",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.15" % "test"

)
