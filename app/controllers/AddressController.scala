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

class AddressController @Inject()
  (repo: AddressRepository, val messagesApi: MessagesApi)
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

  //function for calling index.scala.html
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
          Redirect(routes.AddressController.index())
        }
      }
    )
  }

  def getAddresses: Action[AnyContent] = Action.async {
    repo.list().map { address =>
      Ok(Json.toJson(address))
    }
  }

  def deleteAddress(id: Long) = Action.async { implicit request =>
    repo.selectId(id).map { address =>
      val a = address.head
      repo.create(a.firstName, a.lastName, a.street, a.city, a.state, a.zip, a.telephone, s"DELETE ${a.id}")
      Ok(s"Got $id $address")
    }
  }



}

case class CreateAddressForm(firstName: String, lastName: String, street: String, city: String, state: String, zip: Int, telephone: String)
