package services

import models.User
import repositories.UserRepository

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserService @Inject() (userRepository: UserRepository)(implicit ec: ExecutionContext) {

  def addUser(user: User): Future[User] = {
    userRepository.add(user)
  }

  def getAll: Future[Seq[User]] = {
    userRepository.getAll
  }

  def getByName(name: String): Future[Seq[User]] = {
    userRepository.getByName(name)
  }

  def deleteUser(id: Long): Future[Boolean] = {
    userRepository.delete(id)
  }
}
