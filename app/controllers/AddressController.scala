package controllers

import play.api._
import play.api.mvc._
import play.api.i18n._
import play.api.data.Form
import play.api.data.Forms._
import play.api.data.validation.Constraints._
import play.api.libs.json.Json
import models._
import dal._

import scala.concurrent.{ ExecutionContext, Future }

import javax.inject._

class AddressController @Inject() (repo: AddressRepository, val messagesApi: MessagesApi)
                                  (implicit ec: ExecutionContext) extends Controller with I18nSupport {

  val addressForm: Form[CreateAddressForm] = Form {
    mapping(
      "First Name" -> nonEmptyText,
      "Last Name" -> nonEmptyText,
      "Street" -> nonEmptyText,
      "City" -> nonEmptyText,
      "State" -> nonEmptyText,
      "Zip" -> number,
      "Telephone" -> nonEmptyText
    )(CreateAddressForm.apply)(CreateAddressForm.unapply)
  }

  def index = Action {
    Ok(views.html.index(addressForm))
  }

  def addAddress = Action.async { implicit request =>
    addressForm.bindFromRequest.fold(
      errorForm => {
        Future.successful(Ok(views.html.index(errorForm)))
      },
      address => {
        repo.create(address.firstName, address.lastName, address.street, address.city, address.state, address.zip, address.telephone, "CREATE").map { _ =>
          Redirect(routes.AddressController.index)
        }
      }
    )
  }

  def getAddresses = Action.async {
    repo.list().map { address =>
      Ok(Json.toJson(address))
    }
  }
}

case class CreateAddressForm(firstName: String, lastName: String, street: String, city: String, state: String, zip: Int, telephone: String)
