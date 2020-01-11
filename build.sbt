scalaVersion     := "2.13.1"
version          := "0.1.0-SNAPSHOT"

val scalaTest = "org.scalatest" %% "scalatest" % "3.0.8"

lazy val root = (project in file("."))
  .settings(
    name := "nicecactus",
    libraryDependencies += scalaTest % Test
  )