package exception

import play.api._
import play.api.http.DefaultHttpErrorHandler
import play.api.libs.json.Json
import play.api.mvc.Results._
import play.api.mvc._
import play.api.routing.Router

import javax.inject._
import scala.concurrent._

@Singleton
class ErrorHandler @Inject()(
                              env: Environment,
                              config: Configuration,
                              sourceMapper: OptionalSourceMapper,
                              router: Provider[Router]
                            ) extends DefaultHttpErrorHandler(env, config, sourceMapper, router) {

  // Handles unhandled exceptions thrown in your actions
  override def onServerError(request: RequestHeader, exception: Throwable): Future[Result] = {
    exception match {
      case e: UserNotFoundException =>
        Future.successful(NotFound(Json.obj("error" -> e.getMessage)))
      case e: InvalidPayloadException =>
        Future.successful(BadRequest(Json.obj("error" -> e.getMessage)))
      case e: InternalServerException =>
        Future.successful(InternalServerError(Json.obj("error" -> e.getMessage)))
      case _ =>
        // Falls back to Play's default error behavior (shows dev error page in Dev, generic error in Prod)
        super.onServerError(request, exception)
    }
  }

  // Handles client errors (e.g., 404 Route Not Found, 400 Bad Request from binding)
  override def onClientError(request: RequestHeader, statusCode: Int, message: String): Future[Result] = {
    Future.successful(
      Status(statusCode)(Json.obj("status" -> statusCode, "error" -> message))
    )
  }
}
