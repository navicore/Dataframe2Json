name := "Dataframe2Json"
version := "0.9.0"
scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % "1.6.1" % "provided",
  "org.apache.spark" %% "spark-sql" % "1.6.1" % "provided",
  "org.scalatest" % "scalatest_2.11" % "2.2.6" % "test",
  "net.sf.opencsv" % "opencsv" % "2.3",
  "com.typesafe.play" %% "play" % "2.3.10" % "provided",
  "com.typesafe.netty" % "netty-http-pipelining" % "1.1.4"
)

