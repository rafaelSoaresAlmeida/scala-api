package repositories

import models.User
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfigProvider}
import slick.jdbc.JdbcProfile

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class UserRepository @Inject()(
                                protected val dbConfigProvider: DatabaseConfigProvider
                              )(implicit ec: ExecutionContext)
  extends HasDatabaseConfigProvider[JdbcProfile] {

  import profile.api._

  // Mapeamento da tabela USERS
  private class UsersTable(tag: Tag) extends Table[User](tag, "USERS") {
    def id = column[Long]("ID", O.PrimaryKey, O.AutoInc)

    def name = column[String]("NAME")

    def email = column[String]("EMAIL")

    def * = (id.?, name, email) <> ((User.apply _).tupled, User.unapply)
  }

  private val users = TableQuery[UsersTable]

  // Inserção
  def add(user: User): Future[User] = {
    val insertQuery = (users returning users.map(_.id)
      into ((userObj, newId) => userObj.copy(id = Some(newId)))) += user
    db.run(insertQuery)
  }

  // Buscar todos
  def getAll: Future[Seq[User]] = {
    db.run(users.result)
  }

  // Buscar por name
  //def getByName(name: String): Future[Option[User]] = {
  def getByName(name: String): Future[Seq[User]] = {
    // db.run(users.filter(_.name === name).result.headOption)
    db.run(users.filter(_.name === name).result)
  }

  // Deletar por ID (retorna true se algum registro foi removido)
  def delete(id: Long): Future[Boolean] = {
    db.run(users.filter(_.id === id).delete).map(_ > 0)
  }
}