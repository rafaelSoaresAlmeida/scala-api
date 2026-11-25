package services

import pdi.jwt.{JwtAlgorithm, JwtClaim, JwtJson}
import play.api.Configuration
import play.api.libs.json.Json

import java.time.Clock
import javax.inject.{Inject, Singleton}
import scala.util.Try

@Singleton
class JwtService @Inject()(config: Configuration) {
  implicit val clock: Clock = Clock.systemUTC()
  private val secretKey = config.get[String]("jwt.secret")
  private val algorithm = JwtAlgorithm.HS256

  def generateToken(userId: String, role: String): String = {
    val json = Json.obj("userId" -> userId, "role" -> role).toString()
    val claim = JwtClaim(
      content = json,
      subject = Some(userId)
    ).issuedNow.expiresIn(3600) // Expira em 1 hora

    JwtJson.encode(claim, secretKey, algorithm)
  }

  def validateToken(token: String): Try[JwtClaim] = {
    JwtJson.decode(token, secretKey, Seq(algorithm))
  }
}