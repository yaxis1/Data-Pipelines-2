ThisBuild / scalaVersion := "2.12.12"
ThisBuild / organization := "dsti"


val scalaTest = "org.scalatest" %% "scalatest" % "3.2.2"

lazy val app = (project in file("."))
  .settings(
    name := "App",
    libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.0",
    libraryDependencies += "org.apache.spark" %% "spark-sql" % "3.0.1",
    libraryDependencies += scalaTest % "test",
  )

}
