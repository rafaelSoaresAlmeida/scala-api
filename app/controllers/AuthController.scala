package controllers

import play.api.libs.json._
import play.api.mvc._
import security.JwtAuthAction
import services.JwtService

import javax.inject._
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class AuthController @Inject()(
                                cc: ControllerComponents,
                                jwtService: JwtService,
                                jwtAuthAction: JwtAuthAction
                              )(implicit ec: ExecutionContext) extends AbstractController(cc) {

  def login(): Action[JsValue] = Action.async(parse.json) { request =>
    val user = (request.body \ "user").asOpt[String]
    val password = (request.body \ "password").asOpt[String]

    // Simulação de validação assíncrona no banco
    Future.successful {
      (user, password) match {
        case (Some("batata"), Some("potato")) =>
          val token = jwtService.generateToken(userId = "batata", role = "ADMIN")
          Ok(Json.obj("token" -> token))
        case _ =>
          Unauthorized(Json.obj("erro" -> "Invalid Credentials"))
      }
    }
  }

}