package mfp.platform.services.instagramlinks

import scala.slick.driver.JdbcDriver.backend.Database
import java.sql.{SQLException, Timestamp}
import slick.jdbc.{StaticQuery => Q, GetResult}

import mfp.platform.services.instagramlinks.DefaultHashtagsDAO

trait InstagramLinksDAO {

  val table = "ig_links"
  val columns = "id, url, hashtag_id, ig_username, ig_postdate, status, admin_username, created_at, updated_at, starred, starred_expires_at"
  val columnsNoId = "url, hashtag_id, ig_username, ig_postdate, status, admin_username, created_at, updated_at, starred, starred_expires_at"
  val columnsAndHashtagCols = "i.id, i.url, i.hashtag_id, h.hashtag, h.admin_username, h.created_at, h.updated_at, i.ig_username, i.ig_postdate, i.status, i.admin_username, i.created_at, i.updated_at, i.starred, i.starred_expires_at"

  val hashtagsDAO = new DefaultHashtagsDAO

  //[Admin]
  def createNewIgLink(igLink: InstagramLink)(implicit db: Database): Int

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

  def createNewIgLink(igLink: InstagramLink)(implicit db: Database): Int = {
    db.withSession(
    implicit session =>
      (Q.u + "INSERT INTO " + table + " (" +? columnsNoId +? ") VALUES ('"
        +? igLink.url +? "', "
        +? igLink.hashtag.id +? ", '"
        +? igLink.igUsername +? "', "
        +? new java.sql.Timestamp(igLink.igPostdate.getTime) +? ", '"
        +? igLink.status +? "', '"
        +? igLink.adminUsername +? "', "
        +? new java.sql.Timestamp(igLink.createdAt.getTime) +? ", "
        +? new java.sql.Timestamp(igLink.updatedAt.getTime) +? ", "
        +? (if(igLink.starred) 1 else 0) +? ", "
        +? igLink.starredExpiresAt.map(d => new java.sql.Timestamp(d.getTime))
        +? ");").first //list?  //run? //execute?
    )
  }

  def updateIgLinkById(igLink: InstagramLink)(implicit db: Database): Unit = {
    db.withSession(
      implicit session =>
        (Q.u + "UPDATE " + table + " SET "
          +? "url = " +? igLink.url +? ", "
          +? "hashtag_id = " +? igLink.hashtag.id +? ", "
          +? "ig_username = " +? igLink.igUsername +? ", "
          +? "ig_postdate = " +? new java.sql.Timestamp(igLink.igPostdate.getTime) +? ", "
          +? "status = " +? igLink.status +? ", "
          +? "admin_username = " +? igLink.adminUsername +? ", "
          +? "created_at = " +? new java.sql.Timestamp(igLink.createdAt.getTime) +? ", "
          +? "updated_at = " +? new java.sql.Timestamp(igLink.updatedAt.getTime) +? ", "
          +? "starred = " +? igLink.starred +? ", "
          +? "starred_expires_at = " +? igLink.starredExpiresAt.map(d => new java.sql.Timestamp(d.getTime))
          +? " WHERE id = " +? igLink.id).first //list?  //run? //execute?
    )
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