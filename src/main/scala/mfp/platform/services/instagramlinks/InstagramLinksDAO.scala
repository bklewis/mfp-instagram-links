package mfp.platform.services.instagramlinks

import scala.slick.driver.JdbcDriver.backend.Database
import java.sql.{SQLException, Timestamp}
import slick.jdbc.{StaticQuery => Q, GetResult}

trait InstagramLinksDAO {

  val table = "ig_links"
  val columns = "id, url, hashtag_id, ig_username, ig_postdate, status, admin_username, created_at, updated_at, starred, starred_expires_at"
  val columnsNoId = "url, hashtag_id, ig_username, ig_postdate, status, admin_username, created_at, updated_at, starred, starred_expires_at"

  //[Admin]
  def addNewIgLink(igLink: InstagramLink)(implicit db: Database): Int

  //[Admin]
  def updatedIgLinkById(igLink: InstagramLink)(implicit db: Database): Unit

  //[Admin]
  def allIgLinks(implicit db: Database): Seq[InstagramLink]

  //[Admin]
  def igLinkById(id: Int)(implicit db: Database): InstagramLink

  //[Demo/Admin]
  def igLinksByHashtagApproved(hashtag: String)(implicit db: Database): Seq[InstagramLink]

  //[Admin]
  def igLinksByHashtagAll(hashtag: String)(implicit db: Database): Seq[InstagramLink]

  //[Admin]
  def igLinksByHashtagBanned(hashtag: String)(implicit db: Database): Seq[InstagramLink]

  //[Admin]
  def countAllIgLinks(implicit db: Database): Int

  //[Admin]
  def countIgLinksByHashtag(hashtag:String)(implicit db: Database): Int

  //[Admin]
  def countBannedIgLinksByHashtag(hashtag:String)(implicit db: Database): Int

}

class DefaultIgLinksDAO extends InstagramLinksDAO {

  def addNewIgLink(igLink: InstagramLink)(implicit db: Database): Int = {
    db.withSession(
    implicit session =>
      (Q.u + "INSERT INTO " + table + " (" +? columnsNoId +? ") VALUES ("
        +? igLink.url +? ", "
        +? igLink.hashtagId +? ", "
        +? igLink.igUsername +? ", "
        +? new java.sql.Timestamp(igLink.igPostdate.getTime) +? ", "
        +? igLink.status +? ", "
        +? igLink.adminUsername +? ", "
        +? new java.sql.Timestamp(igLink.createdAt.getTime) +? ", "
        +? new java.sql.Timestamp(igLink.updatedAt.getTime) +? ", "
        +? igLink.starred +? ", "
        +? igLink.starredExpiresAt.map(d => new java.sql.Timestamp(d.getTime))
        +? ")").first //list?  //run? //execute?
    )
  }

  def updatedIgLinkById(igLink: InstagramLink)(implicit db: Database): Unit = {
    db.withSession(
      implicit session =>
        (Q.u + "UPDATE " + table + " SET "
          +? "url = " +? igLink.url +? ", "
          +? "hashtag_id = " +? igLink.hashtagId +? ", "
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

  def allIgLinks(implicit db: Database): Seq[InstagramLink] = {
    db.withSession(
      implicit session =>
        Q.queryNA[InstagramLink]("SELECT " + columns + " FROM " + table).list
    )
  }

  def igLinkById(id: Int)(implicit db: Database): InstagramLink = {
    db.withSession(
      implicit session =>
        Q.query[(Int), InstagramLink]("SELECT " + columns + " FROM " + table + " WHERE id = ?").first(id)
    )
  }

  def igLinksByHashtagApproved(hashtag: String)(implicit db: Database): Seq[InstagramLink] = {
    db.withSession(
      implicit session =>
        Q.queryNA[InstagramLink]
          ("SELECT " + columns + " FROM " + table + " AS igl INNER JOIN hashtags AS h ON igl.hashtag_id = h.id" +
            " WHERE h.hashtag = " + hashtag + " AND status = 'approved' ORDER BY igl.created_at DESC LIMIT 20 OFFSET X").list
    )
  }

  def igLinksByHashtagAll(hashtag: String)(implicit db: Database): Seq[InstagramLink] = {
    db.withSession(
      implicit session =>
        /*Q.query[(String), InstagramLink]("SELECT " + columns + " FROM ig_links where id = ?" + hashtag).list(hashtag)*/
        Q.query[String, InstagramLink]
          ("SELECT " + columns + " FROM " + table + " AS igl INNER JOIN hashtags AS h ON igl.hashtag_id = h.id" +
            " WHERE h.hashtag = ? ORDER BY igl.created_at DESC LIMIT 20 OFFSET X").list(hashtag)
    )
  }

  def igLinksByHashtagBanned(hashtag: String)(implicit db: Database): Seq[InstagramLink] = {
    db.withSession(
      implicit session =>
        Q.queryNA[InstagramLink]
          ("SELECT " + columns + " FROM " + table + " AS igl INNER JOIN hashtags AS h ON igl.hashtag_id = h.id" +
            " WHERE h.hashtag = " + hashtag + " AND status = 'banned' ORDER BY igl.created_at DESC LIMIT 20 OFFSET X").list
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
        Q.query[String, Int]("SELECT COUNT(*) FROM " + table + " WHERE hashtag = ?").first(hashtag)
    )
  }

  def countBannedIgLinksByHashtag(hashtag:String)(implicit db: Database): Int = {
    db.withSession(
      implicit session =>
        Q.query[String, Int]("SELECT COUNT(*) FROM " + table + " WHERE hashtag = ? AND status = 'banned'").first(hashtag)
    )
  }

  implicit val getIgLinksResult = GetResult(r => InstagramLink(r.<<, r.<<, r.<<, r.<<, new java.sql.Timestamp(r.<<), r.<<, r.<<, new java.sql.Timestamp(r.<<), new java.sql.Timestamp(r.<<), r.<<, r.<<))

}