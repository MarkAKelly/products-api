package models

import play.api.libs.json.Json

case class ErrorResponse(error: String)

object ErrorResponse {
  implicit val fmt = Json.format[ErrorResponse]
}
