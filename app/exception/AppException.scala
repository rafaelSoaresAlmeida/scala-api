package exception

sealed trait AppException extends RuntimeException {
  def message: String
  def statusCode: Int
  override def getMessage: String = message
}

case class UserNotFoundException(name: String) extends AppException {
  val message: String = s"User with name $name was not found."
  val statusCode: Int = 404
}

case class InvalidPayloadException(details: String) extends AppException {
  val message: String = s"Invalid request payload: $details"
  val statusCode: Int = 400
}

case class InternalServerException(details: String) extends AppException {
  val message: String = s"Internal Server Error"
  val statusCode: Int = 500
}
