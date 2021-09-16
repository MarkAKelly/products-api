package models

import play.api.libs.json.{Json, OFormat}

case class Product(id: String,
                   name: String,
                   category: String,
                   price: Price)

object Product {

  implicit val fmt: OFormat[Product] = Json.format[Product]
}
