name := "microServicesTT"

trapExit := false

version := "1.0"

scalaVersion := "2.11.8"

resolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"
resolvers += "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"
resolvers += Resolver.bintrayRepo("cakesolutions", "maven")


val akkaVersion = "2.5.13"
val akkaHttpVersion = "10.1.3"
val typesafeConfigVersion = "1.3.2"
val quillVersion = "1.4.0"
val kafkaClientVersion = "1.1.0"
val playJsonVersion = "2.6.7"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test,
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-core" % akkaHttpVersion
)

libraryDependencies ++= Seq(
  "net.cakesolutions" %% "scala-kafka-client" % kafkaClientVersion,
  "net.cakesolutions" %% "scala-kafka-client-akka" % kafkaClientVersion
)

libraryDependencies += "com.typesafe" % "config" % typesafeConfigVersion
libraryDependencies += "io.getquill" %% "quill-async-mysql" % quillVersion
libraryDependencies += "com.typesafe.play" %% "play-json" % playJsonVersion
libraryDependencies += "com.lambdaworks" %% "jacks" % "2.3.3"
libraryDependencies += "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0"
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.1.2"


