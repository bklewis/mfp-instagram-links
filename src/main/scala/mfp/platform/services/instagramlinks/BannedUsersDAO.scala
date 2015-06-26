package mfp.platform.services.instagramlinks

import scala.slick.driver.JdbcDriver.backend.Database
import slick.jdbc.{StaticQuery => Q, GetResult}


trait BannedUsersDAO {

  val table = "banned_users"
  val columns = "id, ig_username, ban_reason, admin_username, created_at, updated_at"
  val columnsNoId = "ig_username, ban_reason, admin_username, created_at, updated_at"


  def createNewBannedUser(user: NewBannedUser)(implicit db: Database): Unit

  def updateBannedUserById(user: BannedUser)(implicit db: Database): Unit

  def getAllBannedUsers(implicit db: Database): Seq[BannedUser]

  def getBannedUserById(id: Int)(implicit db: Database): BannedUser

  def getBannedUserByUsername(igUsername: String)(implicit db: Database): BannedUser

  def countAllBannedUsers(implicit db: Database): Int

}


class DefaultBannedUsersDAO extends BannedUsersDAO {

  def createNewBannedUser(user: NewBannedUser)(implicit db: Database): Unit = {
    db.withSession(
      implicit session =>
        Q.update[(String, String, String, String)]("INSERT INTO " + table + " (" + columnsNoId + ") VALUES (?,?,?,?)").execute(
          user.igUsername,
          user.banReason,
          user.adminUsername,
          new java.sql.Timestamp(user.updatedAt.getTime).toString))
  }

  def updateBannedUserById(user: BannedUser)(implicit db: Database): Unit = {
    db.withSession(
      implicit session =>
        Q.update[(String, String, String, String, Int)]("UPDATE " + table + " SET ig_username=?,ban_reason=?,admin_username=?,updated_at=? WHERE id=?").first(
          user.igUsername,
          user.banReason,
          user.adminUsername,
          new java.sql.Timestamp(user.updatedAt.getTime).toString,
          user.id))
  }

  /*def createNewBannedUser(user: BannedUser)(implicit db: Database): Int = {
    db.withSession(
      implicit session =>
        (Q.u + "INSERT INTO " + table + " (" + columnsNoId + ") VALUES ('"
          + user.igUsername + "','"
          + user.banReason + "','"
          + user.adminUsername + "','"
          + new java.sql.Timestamp(user.createdAt.getTime).toString + "','"
          + new java.sql.Timestamp(user.updatedAt.getTime).toString
          + "')").first
    )
  }

  def updateBannedUserById(user: BannedUser)(implicit db: Database): Unit = {
    db.withSession(
      implicit session =>
        (Q.u + "UPDATE " + table + " (" + columnsNoId + ") VALUES ("
          + user.igUsername + "','"
          + user.banReason + "','"
          + user.adminUsername + "','"
          + new java.sql.Timestamp(user.createdAt.getTime).toString + "','"
          + new java.sql.Timestamp(user.updatedAt.getTime).toString
          + "')").first
    )
  }*/

  def getAllBannedUsers(implicit db: Database): Seq[BannedUser] = {
    db.withSession(
      implicit session =>
        Q.queryNA[BannedUser]("SELECT " + columns + " FROM " + table).list
    )
  }

  def getBannedUserById(id: Int)(implicit db: Database): BannedUser = {
    db.withSession(
      implicit session =>
        Q.query[(Int), BannedUser]("SELECT " + columns + " FROM " + table + " where id = ?").first(id)
    )
  }

  def getBannedUserByUsername(igUsername: String)(implicit db: Database): BannedUser = {
    db.withSession(
      implicit session =>
        Q.query[(String), BannedUser]("SELECT " + columns + " FROM " + table + " where ig_username = ?").first(igUsername)
    )
  }


  //[Admin]
  def countAllBannedUsers(implicit db: Database): Int = {
    db.withSession(
      implicit session =>
        Q.queryNA[Int]("SELECT COUNT(*) FROM " + table).first
    )
  }

  implicit val getBannedUsersResult = GetResult(r => BannedUser(r.<<, r.<<, r.<<, r.<<, new java.sql.Timestamp(r.<<), new java.sql.Timestamp(r.<<)))

}