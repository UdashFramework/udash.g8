package $package$.backend.services

import java.util.UUID

import com.avsystem.commons._
import $package$.shared.model.SharedExceptions
import $package$.shared.model.auth.{Permission, UserContext, UserToken}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Random

class AuthService(usersData: JList[String]) {
  // data is provided in configuration file
  // every user is described by a separated string in format: <username>:<password>
  private val usersWithPasswords: Map[String, String] =
    usersData.asScala.map { data =>
      val parts = data.split(':')
      (parts(0), parts(1))
    }.toMap

  private val tokens: MMap[UserToken, UserContext] = MMap.empty

  /** Tries to authenticated user with provided credentials. */
  def login(username: String, password: String): Future[UserContext] = Future {
    if (usersWithPasswords.contains(username) && usersWithPasswords(username) == password) {
      val token = UserToken(UUID.randomUUID().toString)
      val ctx = UserContext(
        token, username,
        // on every login user gets random set of permissions
        Permission.values.iterator.filter(_ => Random.nextBoolean()).map(_.id).toSet
      )

      tokens.synchronized { tokens(token) = ctx }

      ctx
    } else throw SharedExceptions.UserNotFound()
  }

  def findUserCtx(userToken: UserToken): Option[UserContext] =
    tokens.synchronized { tokens.get(userToken) }
}
