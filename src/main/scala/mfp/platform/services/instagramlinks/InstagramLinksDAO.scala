package mfp.platform.services.instagramlinks

import scala.slick.driver.JdbcDriver.backend.Database
import java.sql.{SQLException, Timestamp}
import slick.jdbc.{StaticQuery => Q, GetResult}

trait InstagramLinksDAO {


  val columns = "id, url, hashtag_id, ig_username, ig_postdate, status, admin_username, created_at, updated_at, starred, starred_expires_at"
  val columnsNoId = "url, hashtag_id, ig_username, ig_postdate, status, admin_username, created_at, updated_at, starred, starred_expires_at"

  //[Admin]
  //Post new link to table
  def addNewIgLink(igLink: InstagramLink)(implicit db: Database): Int

  //[Admin]
  //Update link in database
  def updatedIgLinkById(igLink: InstagramLink)(implicit db: Database): Unit

  //[Admin]
  //Get all links
  def allIgLinks(implicit db: Database): Seq[InstagramLink]

  //[Admin]
  //Get link by id
  def igLinkById(id: Int)(implicit db: Database): InstagramLink

  //[Demo/Admin]
  //Get a list of approved links by hashtag
  def igLinksByHashtagApproved(hashtag: String)(implicit db: Database): Seq[InstagramLink]

  //[Admin]
  //Get a list of all links by hashtag
  def igLinksByHashtagAll(hashtag: String)(implicit db: Database): Seq[InstagramLink]

  //[Admin]
  //Get a list of banned links by hashtag
  def igLinksByHashtagBanned(hashtag: String)(implicit db: Database): Seq[InstagramLink]

  //[Admin]
  //Count the total number of links
  def countAllIgLinks(implicit db: Database): Int

  //[Admin]
  //Count the number of links by hashtag
  def countIgLinksByHashtag(hashtag:String)(implicit db: Database): Int

  //[Admin]
  //Count the number of banned links by reason by hashtag
  def countBannedIgLinksByHashtag(hashtag:String)(implicit db: Database): Int

}

class DefaultIgLinksDAO extends InstagramLinksDAO {

  //[Admin]
  //Post new link to table
  def addNewIgLink(igLink: InstagramLink)(implicit db: Database): Int = {
    db.withSession(
    implicit session =>
      (Q.u + "INSERT INTO ig_links (" +? columnsNoId +? ") VALUES ("
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

  //[Admin]
  //Update link in database
  def updatedIgLinkById(igLink: InstagramLink)(implicit db: Database): Unit = {
    db.withSession(
      implicit session =>
        (Q.u + "UPDATE ig_links SET "
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

  //[Admin]
  //Get all links
  def allIgLinks(implicit db: Database): Seq[InstagramLink] = {
    db.withSession(
      implicit session =>
        Q.queryNA[InstagramLink]("SELECT " + columns + " FROM ig_links").list
    )
  }

  //[Admin]
  //Get link by id
  def igLinkById(id: Int)(implicit db: Database): InstagramLink = {
    db.withSession(
      implicit session =>
        Q.query[(Int), InstagramLink]("SELECT " + columns + " FROM ig_links where id = ?" + id).first(id)
        //sql"SELECT * FROM ig_links WHERE id = $id".as[InstagramLink].firstOption
    )
  }

  //[Demo/Admin]
  //Get a list of approved links by hashtag
  def igLinksByHashtagApproved(hashtag: String)(implicit db: Database): Seq[InstagramLink] = {
    db.withSession(
      implicit session =>
        Q.queryNA[InstagramLink]
          ("SELECT " + columns + " FROM ig_links AS igl INNER JOIN hashtags AS h ON igl.hashtag_id = h.id" +
            " WHERE h.hashtag = " + hashtag + " AND status = 'approved' ORDER BY igl.created_at DESC LIMIT 20 OFFSET X").list
    )
  }

  //[Admin]
  //Get a list of all links by hashtag
  def igLinksByHashtagAll(hashtag: String)(implicit db: Database): Seq[InstagramLink] = {
    db.withSession(
      implicit session =>
        /*Q.query[(String), InstagramLink]("SELECT " + columns + " FROM ig_links where id = ?" + hashtag).list(hashtag)*/
        Q.query[String, InstagramLink]
          ("SELECT " + columns + " FROM ig_links AS igl INNER JOIN hashtags AS h ON igl.hashtag_id = h.id" +
            " WHERE h.hashtag = ? ORDER BY igl.created_at DESC LIMIT 20 OFFSET X").list(hashtag)
    )
  }


  //[Admin]
  //Get a list of banned links by hashtag
  def igLinksByHashtagBanned(hashtag: String)(implicit db: Database): Seq[InstagramLink] = {
    db.withSession(
      implicit session =>
        Q.queryNA[InstagramLink]
          ("SELECT " + columns + " FROM ig_links AS igl INNER JOIN hashtags AS h ON igl.hashtag_id = h.id" +
            " WHERE h.hashtag = " + hashtag + " AND status = 'banned' ORDER BY igl.created_at DESC LIMIT 20 OFFSET X").list
    )
  }


  //[Admin]
  //Count the total number of links
  def countAllIgLinks(implicit db: Database): Int = {
    db.withSession(
      implicit session =>
        Q.queryNA[Int]("SELECT COUNT(*) FROM ig_links").first
    )
  }

  //[Admin]
  //Count the number of links by hashtag
  def countIgLinksByHashtag(hashtag:String)(implicit db: Database): Int = {
    db.withSession(
      implicit session =>
        Q.query[String, Int]("SELECT COUNT(*) FROM ig_links WHERE hashtag = ?").first(hashtag)
    )
  }

  //[Admin]
  //Count the number of banned links by reason by hashtag
  def countBannedIgLinksByHashtag(hashtag:String)(implicit db: Database): Int = {
    db.withSession(
      implicit session =>
        Q.query[String, Int]("SELECT COUNT(*) FROM ig_links WHERE hashtag = ? AND status = 'banned'").first(hashtag)
    )
  }

  implicit val getIgLinksResult = GetResult(r => InstagramLink(r.<<, r.<<, r.<<, r.<<, new java.sql.Timestamp(r.<<), r.<<, r.<<, new java.sql.Timestamp(r.<<), new java.sql.Timestamp(r.<<), r.<<, r.<<))

}