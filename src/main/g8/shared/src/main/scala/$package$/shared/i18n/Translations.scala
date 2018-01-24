package $package$.shared.i18n

import io.udash.i18n._

object Translations {
  import TranslationKey._

  /** The list of supported languages. */
  val langs = Seq("en", "pl")
  /** The list of translation bundles in `backend` resources. */
  val bundlesNames = Seq("auth", "chat", "global")

  object Global {
    val unknownError = key("global.unknownError")
  }

  object Auth {
    val usernameFieldLabel = key("auth.username.label")
    val usernameFieldPlaceholder = key("auth.username.placeholder")
    val passwordFieldLabel = key("auth.password.label")
    val passwordFieldPlaceholder = key("auth.password.placeholder")
    val submitButton = key("auth.submit.label")

    val userNotFound = key("auth.user_not_found")
    val info = key("auth.info")
  }

  object Chat {
    // notice that this translation takes one Int argument
    val connections = key1[Int]("chat.connections")
    val inputPlaceholder = key("chat.input.placeholder")
  }
}
