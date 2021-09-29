package controllers

import models.{ErrorResponse, Price, Product}
import play.api.Logger
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._

import java.util.UUID
import javax.inject.{Inject, Singleton}

@Singleton
class CreateProduct @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  lazy val logger: Logger = Logger(this.getClass)

  def create(): Action[JsValue] = Action(parse.json) { implicit request =>

    logger.info(s"testHeader: ${testHeaderFromRequest()}")

      val body = request.body.validate[Product].asOpt

      testHeaderFromRequest() match {
        case Some("CONFLICT") =>
          Conflict(Json.toJson(ErrorResponse("An existing item already exists for this id")))
        case Some("FORBIDDEN") =>
          Forbidden(Json.toJson(ErrorResponse("User not authorised to access this resource")))
        case Some("SERVER_ERROR") =>
          InternalServerError(Json.toJson(ErrorResponse("An Internal Server Error Occurred")))
        case None | Some("DEFAULT") | _ => {
          body.fold(
            BadRequest(Json.toJson(ErrorResponse("An incorrect body was supplied")))
          )( p => Created(Json.toJson(p)))
        }
      }
  }

  private def testHeaderFromRequest()(implicit req: Request[JsValue]): Option[String] = {
    req.headers.get("TEST_SCENARIO")
  }
}
