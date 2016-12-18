package models

import play.api.libs.json._

case class Address(id: Long, firstName: String, lastName: String, street: String, city: String, state: String, zip: Int, telephone: String, event: String)

object Address {
  implicit val addressFormat = Json.format[Address]
}

