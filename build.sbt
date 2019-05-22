lazy val akkaHttpVersion = "10.1.8"
lazy val akkaVersion    = "2.6.0-M1"

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization    := "pl.edu.agh",
      scalaVersion    := "2.12.8"
    )),
    name := "distributed-projecting-systems",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http"            % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-xml"        % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-stream"          % akkaVersion,

      "com.typesafe.akka" %% "akka-http-testkit"    % akkaHttpVersion % Test,
      "com.typesafe.akka" %% "akka-testkit"         % akkaVersion     % Test,
      "com.typesafe.akka" %% "akka-stream-testkit"  % akkaVersion     % Test,
      "org.scalatest" %% "scalatest" % "3.0.5" % Test,

      "io.circe" %% "circe-core" % "0.12.0-M1",
      "io.circe" %% "circe-generic" % "0.12.0-M1",
      "io.circe" %% "circe-parser" % "0.12.0-M1"

    )
  )
