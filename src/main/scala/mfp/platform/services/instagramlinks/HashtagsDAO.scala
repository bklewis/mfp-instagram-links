package mfp.platform.services.instagramlinks

import scala.slick.driver.JdbcDriver.backend.Database
import slick.jdbc.{StaticQuery => Q, GetResult}


trait HashtagsDAO {

  val table = "hashtags"
  val columns = "id, hashtag, admin_username, created_at, updated_at"
  val columnsNoId = "hashtag, admin_username, created_at, updated_at"


  def createNewHashtag(hashtag: Hashtag)(implicit db: Database): Int

  def updateHashtagById(hashtag: Hashtag)(implicit db: Database): Unit

  def getAllHashtags(implicit db: Database): Seq[Hashtag]

  def getHashtagById(id: Int)(implicit db: Database): Hashtag

  def getHashtagByHashtag(hashtag: String)(implicit db: Database): Hashtag

  def countAllHashtags(implicit db: Database): Int

}


class DefaultHashtagsDAO extends HashtagsDAO {

  def createNewHashtag(hashtag: Hashtag)(implicit db: Database): Int = {
    db.withSession(
      implicit session =>
        (Q.u + "INSERT INTO " + table + " (" +? columnsNoId +? ") VALUES ("
          +? hashtag.hashtag +? ", "
          +? hashtag.adminUsername +? ", "
          +? new java.sql.Timestamp(hashtag.createdAt.getTime) +? ", "
          +? new java.sql.Timestamp(hashtag.updatedAt.getTime)
          +? ")").first
    )
  }

  def updateHashtagById(hashtag: Hashtag)(implicit db: Database): Unit = {
    db.withSession(
      implicit session =>
        (Q.u + "UPDATE " + table + " (" +? columnsNoId +? ") VALUES ("
          +? hashtag.hashtag +? ", "
          +? hashtag.adminUsername +? ", "
          +? new java.sql.Timestamp(hashtag.createdAt.getTime) +? ", "
          +? new java.sql.Timestamp(hashtag.updatedAt.getTime)
          +? ")").first
    )
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