package pl.edu.agh

import akka.actor.{ActorRef, ActorSystem}
import akka.event.Logging
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse, StatusCodes}
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.MethodDirectives.get
import akka.http.scaladsl.server.directives.RouteDirectives.complete
import pl.edu.agh.model.SimulationModel
import io.circe.syntax._
import io.circe.generic.auto._
import scala.concurrent.Future
import scala.io.Source

//#user-routes-class
trait UserRoutes {
  //#user-routes-class

  // we leave these abstract, since they will be provided by the App
  implicit def system: ActorSystem

  lazy val log = Logging(system, classOf[UserRoutes])
  //#all-routes
  //#users-get-post
  //#users-get-delete
  lazy val userRoutes: Route =
  get {
    val folder = Source.fromResource("src/test/resources/CoolingS355").getLines.toList
    val sortedFolderFiles = folder.sortBy(file => file.substring(4, file.length - 3))
    val files = sortedFolderFiles
      .map(filename => Source.fromResource(s"CoolingS355/$filename")
        .getLines
        .toSeq)

    val model: SimulationModel = ModelLoader.loadModel(files)

    complete(model.asJson.toString)

  }

  // other dependencies that UserRoutes use
  def userRegistryActor: ActorRef

  //#all-routes
}
