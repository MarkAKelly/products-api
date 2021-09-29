package controllers

import models.{ErrorResponse, Price, Product}
import play.api.Logger
import play.api.libs.json.Json
import play.api.mvc._

import javax.inject.{Inject, Singleton}

@Singleton
class RetrieveProduct @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  lazy val logger: Logger = Logger(this.getClass)

  def retrieveProduct(id: String): Action[AnyContent] = Action { implicit request =>

    def idIsValid(id: String): Boolean = {
      val regex = "^[0-9a-fA-F]{8}-([0-9a-fA-F]{4})-([0-9a-fA-F]{4})-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$"
      id.matches(regex)
    }

    if (!idIsValid(id)) {
      logger.warn(s"parameter $id did not match regex")
      BadRequest(Json.toJson(ErrorResponse("Invalid ID")))
    } else {

      logger.info(s"id received: $id   testHeader: ${testHeaderFromRequest()}")

      val product = Product(
        id,
        name = "Burger",
        category = "Main",
        price = Price("USD", 3.99)
      )

      testHeaderFromRequest() match {
        case Some("NOT_FOUND") =>
          NotFound(Json.toJson(ErrorResponse("A product could not be found with the information supplied")))
        case Some("FORBIDDEN") =>
          Forbidden(Json.toJson(ErrorResponse("User not authorised to access this resource")))
        case Some("SERVER_ERROR") =>
          InternalServerError(Json.toJson(ErrorResponse("An Internal Server Error Occurred")))
        case None | Some("DEFAULT") | _ =>
          Ok(Json.toJson(product))
      }
    }
  }

  private def testHeaderFromRequest()(implicit req: Request[AnyContent]): Option[String] = {
    req.headers.get("TEST_SCENARIO")
  }
}
