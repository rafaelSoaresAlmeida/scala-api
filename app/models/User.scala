package models

import play.api.libs.json.{Json, OFormat}

case class User(
                 id: Option[Long] = None,
                 name: String,
                 email: String
               )

object User {
  // Enables automatic conversion between User and JSON
  implicit val format: OFormat[User] = Json.format[User]
}
