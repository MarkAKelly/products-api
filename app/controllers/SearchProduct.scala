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

  def searchProduct(price: Option[String],
                    category: Option[String],
                    name: Option[String]): Action[AnyContent] = Action { implicit request =>

    logger.info(s"request params: category:[$category], price:[$price] testHeader: ${testHeaderFromRequest()}")

    lazy val failOnValidParams: Option[Result] = {

      val badRequestResponse = BadRequest(Json.toJson(ErrorResponse(s"An invalid parameter was passed")))

      val cat = category match {
        case Some("Main") | Some("Fries") | Some("Drink") | None => None
        case _ =>
          Some(badRequestResponse)
      }
      val prc = price match {
        case Some(prc) if !prc.matches("^\\d+(.\\d{1,2})?$") =>
          logger.warn(s"an invalid price parameter was submitted: $price")
          Some(badRequestResponse)
        case _ =>
          None
      }

      cat orElse prc
    }

    val result: Result = {
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
          NotFound(Json.toJson(ErrorResponse("A product could not be found with the information supplied")))
        case Some("FORBIDDEN") =>
          Forbidden(Json.toJson(ErrorResponse("User not authorised to access this resource")))
        case Some("SERVER_ERROR") =>
          InternalServerError(Json.toJson(ErrorResponse("An Internal Server Error Occurred")))
        case None | Some("DEFAULT") | _ =>
          Ok(Json.toJson(products))
      }
    }

    failOnValidParams.fold(result)(validationResult => validationResult)
  }

  private def testHeaderFromRequest()(implicit req: Request[AnyContent]): Option[String] = {
    req.headers.get("TEST_SCENARIO")
  }
}
