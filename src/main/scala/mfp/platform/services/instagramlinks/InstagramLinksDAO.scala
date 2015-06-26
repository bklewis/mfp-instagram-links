package mfp.platform.services.instagramlinks

import scala.slick.driver.JdbcDriver.backend.Database
import java.sql.{SQLException, Timestamp}
import slick.jdbc.{StaticQuery => Q, GetResult}

import mfp.platform.services.instagramlinks.DefaultHashtagsDAO

trait InstagramLinksDAO {

  val table = "ig_links"
  val columns = "id, url, hashtag_id, ig_username, ig_postdate, status, admin_username, created_at, updated_at, starred, starred_expires_at"
  val columnsCreate = "url, hashtag_id, ig_username, ig_postdate, status, admin_username, created_at, updated_at, starred, starred_expires_at"
  val columnsAndHashtagCols = "i.id, i.url, i.hashtag_id, h.hashtag, h.admin_username, h.created_at, h.updated_at, i.ig_username, i.ig_postdate, i.status, i.admin_username, i.created_at, i.updated_at, i.starred, i.starred_expires_at"

  val hashtagsDAO = new DefaultHashtagsDAO

  //[Admin]
  def createNewIgLink(igLink: NewInstagramLink)(implicit db: Database): Unit

  //[Admin]
  def updateIgLinkById(igLink: InstagramLink)(implicit db: Database): Unit

  //[Admin]
  def getAllIgLinks(implicit db: Database): Seq[InstagramLink]

  //[Admin]
  def getIgLinkById(id: Int)(implicit db: Database): InstagramLink

  //[Demo/Admin]
  def getIgLinksByHashtagApproved(hashtag: String)(implicit db: Database): Seq[InstagramLink]

  //[Admin]
  def getIgLinksByHashtagAll(hashtag: String)(implicit db: Database): Seq[InstagramLink]

  //[Admin]
  def getIgLinksByHashtagBanned(hashtag: String)(implicit db: Database): Seq[InstagramLink]

  //[Admin]
  def countAllIgLinks(implicit db: Database): Int

  //[Admin]
  def countIgLinksByHashtag(hashtag:String)(implicit db: Database): Int

  //[Admin]
  def countBannedIgLinksByHashtag(hashtag:String)(implicit db: Database): Int

}

class DefaultIgLinksDAO extends InstagramLinksDAO {

  def createNewIgLink(igLink: NewInstagramLink)(implicit db: Database): Unit = {
    db.withSession(
    implicit session =>
      Q.update[(String, Int, String, String, String, String, String, Int, String)](
        "INSERT INTO " + table + " (" + columnsCreate + ") VALUES (?,?,?)").execute(
        igLink.url,
        igLink.hashtag.id,
        igLink.igUsername,
        new java.sql.Timestamp(igLink.igPostdate.getTime).toString,
        igLink.status,
        igLink.adminUsername,
        new java.sql.Timestamp(igLink.updatedAt.getTime).toString,
        if(igLink.starred) 1 else 0,
        new java.sql.Timestamp(igLink.starredExpiresAt.getTime).toString))
  }


  def updateIgLinkById(igLink: InstagramLink)(implicit db: Database): Unit = {
    db.withSession(
      implicit session =>
        Q.update[(String, Int, String, String, String, String, String, Int, String, Int)](
          "UPDATE " + table + " SET url=?,hashtag_id=?,ig_username=?,ig_postdate=?,status=?,admin_username=?,updated_at=?,starred=?,starred_expires_at WHERE id=?").first(
            //"url",
            igLink.url,
            //"hashtag_id",
            igLink.hashtag.id,
            //"ig_username",
            igLink.igUsername,
            //"ig_postdate",
            new java.sql.Timestamp(igLink.igPostdate.getTime).toString,
            igLink.status,
            igLink.adminUsername,
            new java.sql.Timestamp(igLink.updatedAt.getTime).toString,
            if(igLink.starred) 1 else 0,
            new java.sql.Timestamp(igLink.starredExpiresAt.getTime).toString,
            igLink.id))
  }

  def getAllIgLinks(implicit db: Database): Seq[InstagramLink] = {
    db.withSession(
      implicit session =>
        Q.queryNA[InstagramLink]("SELECT " + columnsAndHashtagCols + " FROM " + table + "AS i INNER JOIN " + hashtagsDAO.table
        + " AS h ON i.hashtag_id = h.id").list
    )
  }

  def getIgLinkById(id: Int)(implicit db: Database): InstagramLink = {
    db.withSession(
      implicit session =>
        Q.query[(Int), InstagramLink]("SELECT " + columnsAndHashtagCols + " FROM " + table + "AS i INNER JOIN " + hashtagsDAO.table
          + " AS h ON i.hashtag_id = h.id WHERE i.id = ?").first(id)
    )
  }

  def getIgLinksByHashtagApproved(hashtag: String)(implicit db: Database): Seq[InstagramLink] = {
    db.withSession(
      implicit session =>
        Q.query[String, InstagramLink]
          ("SELECT " + columnsAndHashtagCols + " FROM " + table + " AS i INNER JOIN " + hashtagsDAO.table + " AS h ON i.hashtag_id = h.id" +
            " WHERE h.hashtag = ? AND status = 'approved' ORDER BY i.created_at DESC").list(hashtag)
    )
  }

  def getIgLinksByHashtagAll(hashtag: String)(implicit db: Database): Seq[InstagramLink] = {
    db.withSession(
      implicit session =>
        Q.query[String, InstagramLink]
          ("SELECT " + columnsAndHashtagCols + " FROM " + table + " AS i INNER JOIN " + hashtagsDAO.table + " AS h ON i.hashtag_id = h.id" +
            " WHERE h.hashtag = ? ORDER BY i.created_at DESC").list(hashtag)
    )
  }

  def getIgLinksByHashtagBanned(hashtag: String)(implicit db: Database): Seq[InstagramLink] = {
    db.withSession(
      implicit session =>
        Q.query[String, InstagramLink]
          ("SELECT " + columnsAndHashtagCols + " FROM " + table + " AS i INNER JOIN " + hashtagsDAO.table + " AS h ON igl.hashtag_id = h.id" +
            " WHERE h.hashtag = ? AND status = 'banned' ORDER BY igl.created_at DESC").list(hashtag)
    )
  }

  def countAllIgLinks(implicit db: Database): Int = {
    db.withSession(
      implicit session =>
        Q.queryNA[Int]("SELECT COUNT(*) FROM " + table).first
    )
  }

  def countIgLinksByHashtag(hashtag:String)(implicit db: Database): Int = {
    db.withSession(
      implicit session =>
        Q.query[String, Int]("SELECT COUNT(*) FROM " + table + "AS i INNER JOIN " + hashtagsDAO.table
          + " AS h ON i.hashtag_id = h.id" + " WHERE h.hashtag = ?").first(hashtag)
    )
  }

  def countBannedIgLinksByHashtag(hashtag:String)(implicit db: Database): Int = {
    db.withSession(
      implicit session =>
        Q.query[String, Int]("SELECT COUNT(*) FROM " + table + "AS i INNER JOIN " + hashtagsDAO.table
          + " AS h ON i.hashtag_id = h.id" + " WHERE h.hashtag = ? AND status = 'banned'").first(hashtag)
    )
  }

  implicit val getIgLinksResult = GetResult(r => InstagramLink(r.<<, r.<<, new Hashtag(r.<<, r.<<, r.<<, r.<<, r.<<), r.<<, new java.sql.Timestamp(r.<<), r.<<, r.<<, new java.sql.Timestamp(r.<<), new java.sql.Timestamp(r.<<), r.<<, r.<<))

}