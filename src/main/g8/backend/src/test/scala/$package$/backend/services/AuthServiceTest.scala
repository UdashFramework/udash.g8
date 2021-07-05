package $package$.backend.services

import java.util.ArrayList

import $package$.shared.model.SharedExceptions
import $package$.shared.model.auth.UserToken
import org.scalamock.scalatest.AsyncMockFactory
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AsyncWordSpec

class AuthServiceTest extends AsyncWordSpec with Matchers with AsyncMockFactory {
  "AuthService" should {
    "authenticate users" in {
      val users = new ArrayList[String]()
      for (i <- 1 to 3) {
        users.add(s"u\$i:p\$i")
      }

      val service = new AuthService(users)
      for {
        ctx1 <- service.login("u1", "p1")
        ctx2 <- service.login("u2", "p2")
        _ <- recoverToSucceededIf[SharedExceptions.UserNotFound](service.login("u1", "p3"))
      } yield {
        ctx1.name should be("u1")
        ctx2.name should be("u2")
      }
    }

    "return ctx of authenticated users" in {
      val users = new ArrayList[String]()
      for (i <- 1 to 3) {
        users.add(s"u\$i:p\$i")
      }

      val service = new AuthService(users)
      for {
        ctx1 <- service.login("u1", "p1")
        ctx2 <- service.login("u2", "p2")
      } yield {
        service.findUserCtx(ctx1.token) should be(Some(ctx1))
        service.findUserCtx(ctx2.token) should be(Some(ctx2))
        service.findUserCtx(UserToken("t321")) should be(None)
      }
    }
  }
}
