organization := "com.example"

name := "spark-elasticsearch-blogpost"

version := "0.1"

scalaVersion := "2.9.3"

libraryDependencies ++= Seq(
    "org.apache.spark" % "spark-core_2.9.3" % "0.8.0-incubating",
    "com.codahale" % "jerkson_2.9.1" % "0.5.0"
)

resolvers ++= Seq(
  "Akka Repository" at "http://repo.akka.io/releases/",
  "Spray Repository" at "http://repo.spray.cc/",
  "repo.codahale.com" at "http://repo.codahale.com/",
  "repo.typesafe.com" at "http://repo.typesafe.com/")
