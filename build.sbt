import play.Project._

name := "infoRPKI"

version := "1.0"

libraryDependencies ++= Seq(javaJdbc, javaEbean, "mysql" % "mysql-connector-java" % "5.1.29")

playJavaSettings
