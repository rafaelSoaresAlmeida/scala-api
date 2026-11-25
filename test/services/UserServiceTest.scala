package services

import models.User
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.{times, verify, when}
import org.scalatest.concurrent.ScalaFutures
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play.PlaySpec
import repositories.UserRepository

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class UserServiceTest extends PlaySpec with MockitoSugar with ScalaFutures {


  val newUser = User(name = "Ana Batata", email = "batata@potato.com")
  val runtimeError = "Database error"

//  def beforeAll(): Unit = {
//    mockRepo: UserRepository = mock[UserRepository]
//    service = new UserService(mockRepo)
//  }


  "UserService#addUser" should {

    "add a new user with success" in {
      val mockRepo = mock[UserRepository]
      val service = new UserService(mockRepo)

      when(mockRepo.add(any[User])).thenReturn(Future.successful(newUser))

      val result = service.addUser(newUser)

      whenReady(result) { userReturned =>
        userReturned.email mustBe newUser.email
        userReturned.name mustBe newUser.name
        verify(mockRepo, times(1)).add(any[User])
      }
    }
  }

  "UserService#addUser" should {

    "should throw an error in add user process" in {
      val mockRepo = mock[UserRepository]
      val service = new UserService(mockRepo)

      when(mockRepo.add(any[User])).thenThrow(new RuntimeException(runtimeError))

      val exception = intercept[RuntimeException] {
        service.addUser(newUser)
      }

      exception.getMessage mustBe runtimeError
      verify(mockRepo, times(1)).add(any[User])
    }
  }

}
