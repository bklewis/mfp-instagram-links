package mfp.platform.services.instagramlinks

import scala.slick.driver.JdbcDriver.backend.Database
import slick.jdbc.{StaticQuery => Q, GetResult}


trait HashtagsDAO {

  val columns = "id, hashtag, admin_username, created_at, updated_at"
  val columnsNoId = "hashtag, admin_username, created_at, updated_at"


  //[Admin]
  //Post new link to table
  def createNewHashtag(hashtag: Hashtag)(implicit db: Database): Int

  //[Admin]
  //Update link in database
  def updateHashtagById(hashtag: Hashtag)(implicit db: Database): Unit

  //[Admin]
  //Get all links
  def getAllHashtags(implicit db: Database): Seq[Hashtag]

  //[Admin]
  //Get link by id
  def getHashtagById(id: Int)(implicit db: Database): Hashtag

  //[Admin]
  //Count the total number of hashtags
  def countAllHashtags(implicit db: Database): Int

}


class DefaultHashtagsDAO extends HashtagsDAO {

  //[Admin]
  //Post new link to table
  def createNewHashtag(hashtag: Hashtag)(implicit db: Database): Int = {
    db.withSession(
      implicit session =>
        (Q.u + "INSERT INTO hashtags (" +? columnsNoId +? ") VALUES ("
          +? hashtag.hashtag +? ", "
          +? hashtag.adminUsername +? ", "
          +? new java.sql.Timestamp(hashtag.createdAt.getTime) +? ", "
          +? new java.sql.Timestamp(hashtag.updatedAt.getTime)
          +? ")").first
    )
  }

  //[Admin]
  //Update link in database
  def updateHashtagById(hashtag: Hashtag)(implicit db: Database): Unit = {
    db.withSession(
      implicit session =>
        (Q.u + "UPDATE hashtags (" +? columnsNoId +? ") VALUES ("
          +? hashtag.hashtag +? ", "
          +? hashtag.adminUsername +? ", "
          +? new java.sql.Timestamp(hashtag.createdAt.getTime) +? ", "
          +? new java.sql.Timestamp(hashtag.updatedAt.getTime)
          +? ")").first
    )
  }

  //[Admin]
  //Get all links
  def getAllHashtags(implicit db: Database): Seq[Hashtag] = {
    db.withSession(
      implicit session =>
        Q.queryNA[Hashtag]("SELECT " + columns + " FROM hashtags").list
    )
  }

  //[Admin]
  //Get link by id
  def getHashtagById(id: Int)(implicit db: Database): Hashtag = {
    db.withSession(
      implicit session =>
        Q.query[(Int), Hashtag]("SELECT " + columns + " FROM hashtags where id = ?" + id).first(id)
      //sql"SELECT * FROM ig_links WHERE id = $id".as[InstagramLink].firstOption
    )
  }

  //[Admin]
  //Count the total number of links
  def countAllHashtags(implicit db: Database): Int = {
    db.withSession(
      implicit session =>
        Q.queryNA[Int]("SELECT COUNT(*) FROM hashtags").first
    )
  }

  implicit val getHashtagsResult = GetResult(r => Hashtag(r.<<, r.<<, r.<<, new java.sql.Timestamp(r.<<), new java.sql.Timestamp(r.<<)))

}