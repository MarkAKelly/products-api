import sbt.Keys.javaOptions

enablePlugins(JavaAppPackaging)
enablePlugins(DockerPlugin)

lazy val root = (project in file("."))
  .enablePlugins(PlayScala)
  .settings(
    name := "products-api",
    version := "2.8.x",
    scalaVersion := "2.13.6",
    libraryDependencies ++= Seq(
      guice
    ),
    scalacOptions ++= Seq(
      "-feature",
      "-deprecation",
      "-Xfatal-warnings",

    ),
    javaOptions in Universal ++= Seq(
      "-Dpidfile.path=/dev/null"
    )
  )
  .settings(PlayKeys.playDefaultPort := 9002)

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}

dockerExposedPorts := Seq(9002)
