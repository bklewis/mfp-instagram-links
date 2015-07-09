package mfp.platform.services.instagramlinks

import akka.actor.ActorRef
import mfp.platform.db.DbAction

import scala.slick.jdbc.{StaticQuery => Q, GetResult}

trait InstagramLinksDbOperations {

  val table = "ig_links"
  val columns = "id, url, hashtag_id, ig_username, ig_postdate, status, admin_username, created_at, updated_at, starred, starred_expires_at"
  val columnsCreate = "url, hashtag_id, ig_username, ig_postdate, status, admin_username, updated_at, starred, starred_expires_at"
  val columnsAndHashtagCols = "i.id, i.url, i.hashtag_id, h.hashtag, h.admin_username, h.created_at, h.updated_at, i.ig_username, i.ig_postdate, i.status, i.admin_username, i.created_at, i.updated_at, i.starred, i.starred_expires_at"

  val hashtagsTable = "hashtags"

  val igLinksResult = GetResult(r => InstagramLink(r.<<, r.<<, new Hashtag(r.<<, r.<<, r.<<, r.<<, r.<<), r.<<, new java.sql.Timestamp(r.<<), r.<<, r.<<, new java.sql.Timestamp(r.<<), new java.sql.Timestamp(r.<<), r.<<, r.<<))


  protected def createIgLinkAction(igLink: NewInstagramLink) =
    DbAction[Unit](implicit session => {
      implicit val rowMap = igLinksResult
      (Q.u + "INSERT INTO " + table + " (" + columnsCreate + ") VALUES ("
        +? igLink.url + ","
        +? igLink.hashtag.id + ","
        +? igLink.igUsername + ","
        +? new java.sql.Timestamp(igLink.igPostdate.getTime).toString + ","
        +? igLink.status + ","
        +? igLink.adminUsername + ","
        +? new java.sql.Timestamp(System.currentTimeMillis()).toString + ","
        +? (if(igLink.starred) 1 else 0) + ","
        +? new java.sql.Timestamp(igLink.starredExpiresAt.getTime).toString + ")").execute
    })

  protected def updateIgLinkAction(igLink: InstagramLink) =
    DbAction[Unit](implicit session => {
      implicit val rowMap = igLinksResult
      (Q.u + "UPDATE " + table
        + " SET url=" +? igLink.url
        + ", hashtag_id=" +? igLink.hashtag.id
        + ", ig_username=" +? igLink.igUsername
        + ", ig_postdate=" +? new java.sql.Timestamp(System.currentTimeMillis()).toString
        + ", status=" +? igLink.status
        + ", admin_username=" +? igLink.adminUsername
        + ", updated_at=" +? new java.sql.Timestamp(System.currentTimeMillis()).toString
        + ", starred=" +? (if(igLink.starred) 1 else 0)
        + ", starred_expires_at=" +? new java.sql.Timestamp(igLink.starredExpiresAt.getTime).toString
        + " WHERE id=" +? igLink.id).execute
    })

  protected def getAllIgLinksAction =
    DbAction[Seq[InstagramLink]](implicit session => {
      implicit val rowMap = igLinksResult
      Q.queryNA[InstagramLink]("SELECT " + columnsAndHashtagCols + " FROM " + table + " AS i INNER JOIN " + hashtagsTable
        + " AS h ON i.hashtag_id = h.id").list
    })


  protected def getIgLinkByIdAction(id: Int) =
    DbAction[InstagramLink](implicit session => {
      implicit val rowMap = igLinksResult
      Q.query[(Int), InstagramLink]("SELECT " + columnsAndHashtagCols + " FROM " + table + " AS i INNER JOIN " + hashtagsTable
        + " AS h ON i.hashtag_id = h.id WHERE i.id = ?").first(id)
    })


  protected def getIgLinksByHashtagApprovedAction(hashtag: String) =
    DbAction[Seq[InstagramLink]](implicit session => {
      implicit val rowMap = igLinksResult
      Q.query[(String), InstagramLink](
        "SELECT " + columnsAndHashtagCols + " FROM " + table + " AS i INNER JOIN " + hashtagsTable + " AS h ON i.hashtag_id = h.id" +
          " WHERE h.hashtag = ? AND status = 'approved' ORDER BY i.created_at DESC").list(hashtag)
    })

  protected def getIgLinksByHashtagAllAction(hashtag: String) =
    DbAction[Seq[InstagramLink]](implicit session => {
      implicit val rowMap = igLinksResult
      Q.query[String, InstagramLink] (
        "SELECT " + columnsAndHashtagCols + " FROM " + table + " AS i INNER JOIN " + hashtagsTable + " AS h ON i.hashtag_id = h.id" +
        " WHERE h.hashtag = ? ORDER BY i.created_at DESC").list(hashtag)
    })

  protected def getIgLinksByHashtagBannedAction(hashtag: String) =
    DbAction[Seq[InstagramLink]](implicit session => {
      implicit val rowMap = igLinksResult
      Q.query[String, InstagramLink] (
        "SELECT " + columnsAndHashtagCols + " FROM " + table + " AS i INNER JOIN " + hashtagsTable + " AS h ON i.hashtag_id = h.id" +
          " WHERE h.hashtag = ? AND status = 'banned' ORDER BY i.created_at DESC").list(hashtag)
    })

  // Delete all links associated with a hashtag (id), to be used to create a cascading delete when a hashtag is deleted
  protected def deleteIgLinksByHashtagIdAction(hashtagId: Int) =
    DbAction[Unit](implicit session => {
      (Q.u + "DELETE FROM " + table + " WHERE hashtag_id=" +? hashtagId).execute
    })

  // Delete individual link, to be used if url expires
  protected def deleteIgLinkAction(igLink: InstagramLink) =
    DbAction[Unit](implicit session => {
      (Q.u + "DELETE FROM " + table + " WHERE id=" +? igLink.id).first
    })


  def countAllIgLinksAction =
    DbAction[Int](implicit session => {
      Q.queryNA[Int]("SELECT COUNT(*) FROM " + table).first
    })


  def countIgLinksByHashtagAction(hashtag: String) =
    DbAction[Int](implicit session => {
      Q.query[String, Int]("SELECT COUNT(*) FROM " + table + " AS i INNER JOIN " + hashtagsTable
        + " AS h ON i.hashtag_id = h.id" + " WHERE h.hashtag = ?").first(hashtag)
    })

  def countBannedIgLinksByHashtagAction(hashtag: String) =
    DbAction[Int](implicit session => {
      Q.query[String, Int]("SELECT COUNT(*) FROM " + table + " AS i INNER JOIN " + hashtagsTable
        + " AS h ON i.hashtag_id = h.id" + " WHERE h.hashtag = ? AND status = 'banned'").first(hashtag)
    })



  //[Admin]
  def createIgLink(igLink: NewInstagramLink, replyTo: ActorRef): Unit

  //[Admin]
  def updateIgLink(igLink: InstagramLink, replyTo: ActorRef): Unit

  //[Admin]
  def getAllIgLinks(replyTo: ActorRef): Unit

  //[Admin]
  def getIgLinkById(id: Int, replyTo: ActorRef): Unit

  //[Demo/Admin]
  def getIgLinksByHashtagApproved(hashtag: String, replyTo: ActorRef): Unit

  //[Admin]
  def getIgLinksByHashtagAll(hashtag: String, replyTo: ActorRef): Unit

  //[Admin]
  def getIgLinksByHashtagBanned(hashtag: String, replyTo: ActorRef): Unit

  //[Admin]
  def countAllIgLinks(replyTo: ActorRef): Unit

  //[Admin]
  def countIgLinksByHashtag(hashtag:String, replyTo: ActorRef): Unit

  //[Admin]
  def countBannedIgLinksByHashtag(hashtag:String, replyTo: ActorRef): Unit

  //[Admin]
  def deleteIgLinksByHashtagId(hashtagId:Int, replyTo: ActorRef): Unit

  //[Admin]
  def deleteIgLink(igLink: InstagramLink, replyTo: ActorRef): Unit

}