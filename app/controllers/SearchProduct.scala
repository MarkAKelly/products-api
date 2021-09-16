package controllers

import models.{ErrorResponse, Price, Product}
import play.api.Logger
import play.api.libs.json.Json
import play.api.mvc._

import java.util.UUID
import javax.inject.{Inject, Singleton}

@Singleton
class SearchProduct @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  lazy val logger: Logger = Logger(this.getClass)

  def searchProduct(category: Option[String],
                    price: Option[String],
                    name: Option[String] ): Action[AnyContent] = Action { implicit request =>

      logger.info(s"  testHeader: ${testHeaderFromRequest()}")

      val products = Seq(
        Product(
          id = UUID.randomUUID().toString,
          name = "Burger",
          category = "Main",
          price = Price("USD", 3.99)
        ),
        Product(
          id = UUID.randomUUID().toString,
          name = "Fries",
          category = "Side",
          price = Price("USD", 1.99)
        )
      )

      testHeaderFromRequest() match {
        case Some("NOT_FOUND") =>
          NotFound(Json.toJson(ErrorResponse("User not found")))
        case Some("FORBIDDEN") =>
          Forbidden(Json.toJson(ErrorResponse("User not authorised to access this resource")))
        case Some("SERVER_ERROR") =>
          InternalServerError(Json.toJson(ErrorResponse("An Internal Server Error Occurred")))
        case None | Some("DEFAULT") | _ =>
          Ok(Json.toJson(products))

    }
  }

  private def testHeaderFromRequest()(implicit req: Request[AnyContent]): Option[String] = {
    req.headers.get("TEST_SCENARIO")
  }
}
