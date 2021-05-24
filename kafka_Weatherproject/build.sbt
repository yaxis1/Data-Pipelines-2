
name := "weather_streaming"

version := "1.0"

scalaVersion := "2.12.12"

libraryDependencies += "org.apache.spark" %% "spark-sql" % "2.4.0"

libraryDependencies += "org.apache.spark" %% "spark-sql-kafka-0-10" % "2.4.0"

libraryDependencies += "org.apache.kafka" %% "kafka-clients" % "1.1.0"
