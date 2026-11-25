package security

import pdi.jwt.JwtClaim
import play.api.libs.json.Json
import play.api.mvc._
import services.JwtService

import javax.inject.Inject
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

case class AuthenticatedRequest[A](
                                    userId: String,
                                    claim: JwtClaim,
                                    request: Request[A]
                                  ) extends WrappedRequest[A](request)

class JwtAuthAction @Inject()(
                               val parser: BodyParsers.Default,
                               jwtService: JwtService
                             )(implicit val executionContext: ExecutionContext)
  extends ActionBuilder[AuthenticatedRequest, AnyContent] {

  private val bearerRegex = """Bearer\s+(.+)""".r

  override def invokeBlock[A](
                               request: Request[A],
                               block: AuthenticatedRequest[A] => Future[Result]
                             ): Future[Result] = {
    request.headers.get("Authorization") match {
      case Some(bearerRegex(token)) =>
        jwtService.validateToken(token) match {
          case Success(claim) =>
            val userId = claim.subject.getOrElse("Unknow")
            block(AuthenticatedRequest(userId, claim, request))

          case Failure(_) =>
            Future.successful(Results.Unauthorized(Json.obj("erro" -> "Expired Token")))
        }

      case _ =>
        Future.successful(Results.Unauthorized(Json.obj("erro" -> "Invalid Token")))
    }
  }
}