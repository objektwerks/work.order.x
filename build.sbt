name := "poolmate.web"

lazy val caskVersion = "0.8.0"
lazy val laminarVersion = "0.14.2"
lazy val waypointVersion = "0.5.0"
lazy val upickleVersion = "1.4.4"
lazy val scalaJavaTimeVersion = "2.3.0"
lazy val postgresqlVersion = "42.3.1"
lazy val scalaTestVersion = "3.2.10"

lazy val common = Defaults.coreDefaultSettings ++ Seq(
  organization := "objektwerks",
  version := "0.1-SNAPSHOT",
  scalaVersion := "3.1.1"
)

lazy val poolmate = project.in(file("."))
  .aggregate(sharedJs, sharedJvm, js, jvm)
  .settings(common)
  .settings(
    publish := {},
    publishLocal := {}
  )

lazy val shared = crossProject(JSPlatform, JVMPlatform)
  .crossType(CrossType.Pure)
  .in(file("shared"))
  .settings(common)
  .settings(
    libraryDependencies ++= Seq(
      "com.lihaoyi" %% "upickle" % upickleVersion,
      "io.github.cquiroz" %% "scala-java-time" % scalaJavaTimeVersion,
      "org.scalatest" %% "scalatest" % scalaTestVersion % Test
    )
  )

lazy val sharedJs = shared.js
lazy val sharedJvm = shared.jvm

lazy val js = (project in file("js"))
  .dependsOn(sharedJs)
  .enablePlugins(ScalaJSPlugin)
  .settings(common)
  .settings(
    libraryDependencies ++= Seq(
      "com.raquo" %%% "laminar" % laminarVersion,
      "com.raquo" %%% "waypoint" % waypointVersion,
      "com.lihaoyi" %%% "upickle" % upickleVersion,
      "io.github.cquiroz" %%% "scala-java-time" % scalaJavaTimeVersion
    )
  )

lazy val jvm = (project in file("jvm"))
  .dependsOn(sharedJvm, js)
  .enablePlugins(JavaServerAppPackaging)
  .settings(common)
  .settings(
    reStart / mainClass := Some("objektwerks.poolmate.Server"),
    libraryDependencies ++= {
      Seq(
        "com.lihaoyi" %% "cask" % caskVersion,
        "com.lihaoyi" %% "upickle" % upickleVersion,
        "com.lihaoyi" %% "requests" % "0.7.0",
        "org.scalikejdbc" %% "scalikejdbc" % "4.0.0",
        "org.postgresql" % "postgresql" % postgresqlVersion,
        "com.h2database" % "h2" % "2.0.206",
        "io.github.cquiroz" %% "scala-java-time" % "2.3.0",
        "com.typesafe" % "config" % "1.4.1",
        "com.typesafe.scala-logging" %% "scala-logging" % "3.9.4",
        "ch.qos.logback" % "logback-classic" % "1.2.10",
        "org.scalatest" %% "scalatest" % scalaTestVersion % Test
      )
    }
  )