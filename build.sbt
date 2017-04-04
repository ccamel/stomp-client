import Dependencies._

lazy val stompclient = (project in file(".")).
  settings(
    inThisBuild(List(
      organization := "me.ccm",
      scalaVersion := "2.12.1",
      version      := "0.1.0-SNAPSHOT"
    )),
    name := "stomp-client",
    libraryDependencies += scalaTest % Test
  )
