package $package$.frontend.services

import $package$.shared.i18n.Translations
import io.udash._
import io.udash.i18n._

import org.scalajs.dom

import scala.concurrent.duration.DurationInt
import scala.language.postfixOps

/** Keeps entities responsible for translations. */
class TranslationsService(rpc: RemoteTranslationRPC) {
  implicit val translationProvider: TranslationProvider =
    new RemoteTranslationProvider(rpc, Some(dom.window.localStorage), 6 hours)

  implicit val currentLangProperty: Property[Lang] =
    Property(Lang(Translations.langs.head))

  def setLanguage(lang: Lang): Unit = {
    require(Translations.langs.contains(lang.lang), "Selected language is not supported.")
    currentLangProperty.set(lang)
  }
}
