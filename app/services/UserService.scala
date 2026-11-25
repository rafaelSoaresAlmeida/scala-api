package services

import exception.{InternalServerException, UserNotFoundException}
import models.User
import play.api.Logger
import repositories.UserRepository

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserService @Inject()(userRepository: UserRepository)(implicit ec: ExecutionContext) {

  private val auditLogger: Logger = Logger("application")

  def addUser(user: User): Future[User] = {
    auditLogger.info("Adding User")
    userRepository.add(user).recoverWith {
      case ex: Throwable =>
        auditLogger.error("Error in adding user", ex)
        Future.failed(InternalServerException("Error to add user"))
    }
  }

  def getAll: Future[Seq[User]] = {
    auditLogger.info("Get All User")
    userRepository.getAll
  }

  def getByName(name: String): Future[Seq[User]] = {
    auditLogger.info("Get User by Name")
    userRepository.getByName(name).flatMap { users =>
      if (users.isEmpty) {
        auditLogger.warn("User not found")
        Future.failed(UserNotFoundException(name))
      } else {
        Future.successful(users)
      }
    }
  }

  def deleteUser(id: Long): Future[Boolean] = {
    auditLogger.info("Delete User")
    userRepository.delete(id)
  }

  def getAllEmails: Future[Set[String]] = {

    auditLogger.info("Get All Emails in database")
    Future {
      return userRepository.getAll.map { users =>
        users
          .view
          .map(_.email)
          .toSet
      }
    }.recover {
      auditLogger.error("Error to get emails")
      throw InternalServerException("Error to get emails")
    }
  }
}