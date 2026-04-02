name := "resilient-event-handling"
version := "0.1.0"
scalaVersion := "3.3.4"

val http4sVersion = "0.23.30"

Compile / unmanagedResourceDirectories += baseDirectory.value

libraryDependencies ++= Seq(
  "org.http4s" %% "http4s-dsl"          % http4sVersion,
  "org.http4s" %% "http4s-ember-server" % http4sVersion,
  "org.http4s" %% "http4s-ember-client" % http4sVersion,
)
