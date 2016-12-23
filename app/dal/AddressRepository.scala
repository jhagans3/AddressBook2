package dal

import javax.inject.{Inject, Singleton}

import models.Address
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class AddressRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)(implicit ec: ExecutionContext) {
  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  import dbConfig._
  import driver.api._

  private class AddressTable(tag: Tag) extends Table[Address](tag, "address") {

    def id = column[Long]("id", O.PrimaryKey, O.AutoInc)

    def firstName = column[String]("firstName")
    def lastName = column[String]("lastName")
    def street = column[String]("street")
    def city = column[String]("city")
    def state = column[String]("state")
    def zip = column[Int]("zip")
    def telephone = column[String]("telephone")
    def event = column[String]("event")

    def * = (id, firstName, lastName, street, city, state, zip, telephone, event) <> ((Address.apply _).tupled, Address.unapply)
  }

  private val addresses = TableQuery[AddressTable]

  def create(firstName: String, lastName: String, street: String, city: String, state: String, zip: Int, telephone: String, event: String): Future[Address] = db.run {
    (addresses.map(a => (a.firstName, a.lastName, a.street, a.city, a.state, a.zip, a.telephone, a.event))
      returning addresses.map(_.id)
      into ((add, id) => Address(id, add._1, add._2, add._3, add._4, add._5, add._6, add._7, add._8))
      ) += (firstName, lastName, street, city, state, zip, telephone, event)
  }

  def list(): Future[Seq[Address]] = db.run {
    addresses.sortBy(_.id.desc).result
  }

  def selectId(id: Long): Future[Seq[Address]] = db.run {
    addresses.filter(_.id === id).result
  }
}
