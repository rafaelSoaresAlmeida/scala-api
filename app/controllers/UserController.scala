package controllers

import models.User
import play.api.libs.json.{JsError, Json}
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}
import security.JwtAuthAction
import services.UserService

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserController @Inject()(cc: ControllerComponents,
                               jwtAuthAction: JwtAuthAction, userService: UserService)(implicit ec: ExecutionContext)
  extends AbstractController(cc) {

  def addUser(): Action[AnyContent] = jwtAuthAction.async { implicit request =>
    request.body.asJson match {
      case Some(json) =>
        json.validate[User].fold(
          errors => Future.successful(BadRequest(Json.obj("error" -> JsError.toJson(errors)))),
          user => userService.addUser(user).map(created => Created(Json.toJson(created)))
        )
      case None =>
        Future.successful(BadRequest(Json.obj("error" -> "Invalid Json")))
    }
  }

  def getAll: Action[AnyContent] = jwtAuthAction.async {
    userService.getAll.map(users => Ok(Json.toJson(users)))
  }

  def getByName(name: String): Action[AnyContent] = Action.async {
    userService.getByName(name).map(users => Ok(Json.toJson(users)))
  }

  def deleteUser(id: Long): Action[AnyContent] = Action.async {
    userService.deleteUser(id).map {
      case true => NoContent
      case false => NotFound(Json.obj("error" -> s"User with id $id not found"))
    }
  }
}
