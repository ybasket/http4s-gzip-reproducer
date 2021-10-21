name := "http4s-gzip-reproducer"

version := "0.1"

scalaVersion := "2.13.6"

libraryDependencies ++= List(
  "org.http4s" %% "http4s-ember-server" % "0.23.6",
  "org.http4s" %% "http4s-async-http-client" % "0.23.6",
  "org.http4s" %% "http4s-dsl" % "0.23.6"
)
