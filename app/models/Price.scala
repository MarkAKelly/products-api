package models

import play.api.libs.json.{Json, OFormat}

case class Price(currency: String,
                 amount: Double)

object Price {

  implicit val fmt: OFormat[Price] = Json.format[Price]
}
