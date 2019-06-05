lazy val frontend = project
  .settings(
  inThisBuild(
    List(
      organization    := "pl.edu.agh",
      scalaVersion    := "2.12.8"
    )
  ),
    name := "dps-frontend",
    libraryDependencies ++= javaFXModules.map( m=>
      "org.openjfx" % s"javafx-$m" % "11" classifier osName
    ),
    libraryDependencies += "org.scalafx" %% "scalafx" % "12.0.1-R17",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % Test,
    fork := true,
    shellPrompt := { state => System.getProperty("user.name") + s":${name.value}> " }
  )

lazy val osName = System.getProperty("os.name") match {
  case n if n.startsWith("Linux") => "linux"
  case n if n.startsWith("Mac") => "mac"
  case n if n.startsWith("Windows") => "win"
  case _ => throw new Exception("Unknown platform!")
}

// Add JavaFX dependencies
lazy val javaFXModules = Seq("base", "controls", "fxml", "graphics", "media", "swing", "web")