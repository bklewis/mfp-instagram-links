package mfp.platform.services.instagramlinks

import scala.slick.driver.JdbcDriver.backend.Database
import slick.jdbc.{StaticQuery => Q, GetResult}

import mfp.platform.services.instagramlinks.InstagramLinksDAO


trait HashtagsDAO {

  val table = "hashtags"
  val columns = "id, hashtag, admin_username, created_at, updated_at"
  val columnsCreate = "hashtag, admin_username, created_at, updated_at"
  val columnsUpdate = "admin_username, updated_at"

  def createNewHashtag(hashtag: NewHashtag)(implicit db: Database): Unit

  def updateHashtagById(hashtag: Hashtag)(implicit db: Database): Unit

  def getAllHashtags(implicit db: Database): Seq[Hashtag]

  def getHashtagById(id: Int)(implicit db: Database): Hashtag

  def getHashtagByHashtag(hashtag: String)(implicit db: Database): Hashtag

  def countAllHashtags(implicit db: Database): Int

}


class DefaultHashtagsDAO extends HashtagsDAO {

  /*def createNewHashtag(hashtag: Hashtag)(implicit db: Database): Unit = {
      db.withSession(
        implicit session =>
          (Q.u + "INSERT INTO " + table + " (" + columnsNoId + ") VALUES ('" //+ hashtag.hashtag + "', 'admin1', NULL,NULL)").execute
            + hashtag.hashtag.toLowerCase + "','"
            + hashtag.adminUsername + "','"
            + new java.sql.Timestamp(hashtag.createdAt.getTime).toString + "','"
            + new java.sql.Timestamp(hashtag.updatedAt.getTime).toString
            + "')").execute
      )
  }*/

  def createNewHashtag(hashtag: NewHashtag)(implicit db: Database): Unit = {
    db.withSession(
      implicit session =>
        Q.update[(String, String, String)](
          "INSERT INTO " + table + " (" + columnsCreate + ") VALUES (?,?,?)").execute( //+ hashtag.hashtag + "', 'admin1', NULL,NULL)").execute
          hashtag.hashtag.toLowerCase,
          hashtag.adminUsername,
          new java.sql.Timestamp(hashtag.updatedAt.getTime).toString))
  }

  def updateHashtagById(hashtag: Hashtag)(implicit db: Database): Unit = {
    db.withSession(
      implicit session =>
        Q.update[(String, String, Int)](
          "UPDATE " + table + " SET admin_username=?, updated_at=? WHERE id=?").first(
            hashtag.adminUsername,
            new java.sql.Timestamp(hashtag.updatedAt.getTime).toString,
            hashtag.id))
  }

  def getAllHashtags(implicit db: Database): Seq[Hashtag] = {
    db.withSession(
      implicit session =>
        Q.queryNA[Hashtag]("SELECT " + columns + " FROM " + table).list
    )
  }

  def getHashtagById(id: Int)(implicit db: Database): Hashtag = {
    db.withSession(
      implicit session =>
        Q.query[(Int), Hashtag]("SELECT " + columns + " FROM " + table + " where id = ?").first(id)
    )
  }

  def getHashtagByHashtag(hashtag: String)(implicit db: Database): Hashtag = {
    db.withSession(
      implicit session =>
        Q.query[(String), Hashtag]("SELECT " + columns + " FROM " + table + " where hashtag = ?").first(hashtag)
    )
  }

  def countAllHashtags(implicit db: Database): Int = {
    db.withSession(
      implicit session =>
        Q.queryNA[Int]("SELECT COUNT(*) FROM " + table).first
    )
  }

  implicit val getHashtagsResult = GetResult(r => Hashtag(r.<<, r.<<, r.<<, new java.sql.Timestamp(r.<<), new java.sql.Timestamp(r.<<)))

}