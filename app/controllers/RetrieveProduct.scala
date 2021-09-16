//package controllers
//
//import models.ErrorResponse
//import play.api.Logger
//import play.api.libs.json.Json
//import play.api.mvc._
//
//import javax.inject.{Inject, Singleton}
//
//@Singleton
//class RetrieveProduct @Inject()(cc: ControllerComponents) extends AbstractController(cc) {
//
//  lazy val logger: Logger = Logger(this.getClass)
//
//  def searchProduct(`type`: Option[String],
//                    price: Option[String],
//                    name: Option[String] ): Action[AnyContent] = Action { implicit request =>
//
//    def idIsValid(id: String): Boolean = {
//      val regex = "^[0-9a-fA-F]{8}-([0-9a-fA-F]{4})-([0-9a-fA-F]{4})-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$"
//      id.matches(regex)
//    }
//
//    if (!idIsValid(id)) {
//      logger.warn(s"parameter $id did not match regex")
//      BadRequest(Json.toJson(ErrorResponse("Invalid ID")))
//    } else {
//
//      logger.info(s"id received: $id   testHeader: ${testHeaderFromRequest()}")
//      val user = User(id, "Roland", "McDoland", 1)
//
//      testHeaderFromRequest() match {
//        case Some("NOT_FOUND") =>
//          NotFound(Json.toJson(ErrorResponse("User not found")))
//        case Some("FORBIDDEN") =>
//          Forbidden(Json.toJson(ErrorResponse("User not authorised to access this resource")))
//        case Some("SERVER_ERROR") =>
//          InternalServerError(Json.toJson(ErrorResponse("An Internal Server Error Occurred")))
//        case None | Some("DEFAULT") | _ =>
//          Ok(Json.toJson(user))
//      }
//    }
//  }
//
//  private def testHeaderFromRequest()(implicit req: Request[AnyContent]): Option[String] = {
//    req.headers.get("TEST_SCENARIO")
//  }
//}
