package mfp.platform.services.instagramlinks

import mfp.platform.db.DbAction

import scala.slick.jdbc.{StaticQuery => Q, GetResult}

trait DbOperations {

  val table: String
  val columns: String
  val columnsCreate: String

  val getResult: AnyRef// = GetResult(r => Hashtag(r.<<, r.<<, r.<<, new java.sql.Timestamp(r.<<), new java.sql.Timestamp(r.<<)))



  protected def insertIgLinkAction(igLink: NewInstagramLink, hashtag: Hashtag) =
    DbAction[Unit](implicit session => {
      implicit val rowMap = getResult
      (Q.u + "INSERT INTO " + table + " (" + columnsCreate + ") VALUES ("
        +? igLink.url + ","
        +? hashtag.id + ","
        +? igLink.igUsername + ","
        +? new java.sql.Timestamp(igLink.igPostdate.getTime).toString + ","
        +? igLink.status + ","
        +? igLink.adminUsername + ","
        +? new java.sql.Timestamp(System.currentTimeMillis()).toString + ","
        +? (if(igLink.starred) 1 else 0) + ","
        +? new java.sql.Timestamp(igLink.starredExpiresAt.getTime).toString + ")").execute
    })

  // Delete all links associated with a hashtag (id), to be used to create a cascading delete when a hashtag is deleted
  protected def deleteIgLinksByHashtagIdAction(hashtagId: Int) =
    DbAction[Unit](implicit session => {
      (Q.u + "DELETE FROM " + table + " WHERE hashtag_id=" +? hashtagId).execute
    })


  protected def getHashtagByHashtagAction(hashtag: String) =
    DbAction[Hashtag](implicit session => {
      implicit val rowMap = getResult
      Q.query[(String), Hashtag]("SELECT " + columns + " FROM " + table + " where hashtag = ?").first(hashtag.toLowerCase)
    })


  protected def deleteHashtagAction(hashtag: Hashtag) =
    DbAction[Unit](implicit session => {
      (Q.u + "DELETE FROM " + table + " WHERE id=" +? hashtag.id).execute
    })


  protected def deleteHashtagCascadeAction(hashtag: Hashtag) =
    deleteHashtagAction(hashtag).map(_ => deleteIgLinksByHashtagIdAction(hashtag.id))

  protected def createIgLinkAction(igLink: NewInstagramLink) =
    getHashtagByHashtagAction(igLink.hashtag).map(hashtag => insertIgLinkAction(igLink, hashtag))


  /*val hashtagsTable = "hashtags"
  val hashtagsColumns = "id, hashtag, admin_username, created_at, updated_at"
  val hashtagsColumnsCreate = "hashtag, admin_username, updated_at"

  val hashtagResults = GetResult(r => Hashtag(r.<<, r.<<, r.<<, new java.sql.Timestamp(r.<<), new java.sql.Timestamp(r.<<)))


  val igLinksTable = "ig_links"
  val igLinksColumns = "id, url, hashtag_id, ig_username, ig_postdate, status, admin_username, created_at, updated_at, starred, starred_expires_at"
  val igLinksColumnsCreate = "url, hashtag_id, ig_username, ig_postdate, status, admin_username, updated_at, starred, starred_expires_at"
  //val igLinksAndHashtagCols = "i.id, i.url, i.hashtag_id, h.hashtag, h.admin_username, h.created_at, h.updated_at, i.ig_username, i.ig_postdate, i.status, i.admin_username, i.created_at, i.updated_at, i.starred, i.starred_expires_at"

  val igLinksResult = GetResult(r => InstagramLink(r.<<, r.<<, new Hashtag(r.<<, r.<<, r.<<, r.<<, r.<<), r.<<, new java.sql.Timestamp(r.<<), r.<<, r.<<, new java.sql.Timestamp(r.<<), new java.sql.Timestamp(r.<<), r.<<, r.<<))


  protected def insertIgLinkAction(igLink: NewInstagramLink, hashtag: Hashtag) =
    DbAction[Unit](implicit session => {
      implicit val rowMap = igLinksResult
      (Q.u + "INSERT INTO " + igLinksTable + " (" + igLinksColumnsCreate + ") VALUES ("
        +? igLink.url + ","
        +? hashtag.id + ","
        +? igLink.igUsername + ","
        +? new java.sql.Timestamp(igLink.igPostdate.getTime).toString + ","
        +? igLink.status + ","
        +? igLink.adminUsername + ","
        +? new java.sql.Timestamp(System.currentTimeMillis()).toString + ","
        +? (if(igLink.starred) 1 else 0) + ","
        +? new java.sql.Timestamp(igLink.starredExpiresAt.getTime).toString + ")").execute
    })

  // Delete all links associated with a hashtag (id), to be used to create a cascading delete when a hashtag is deleted
  protected def deleteIgLinksByHashtagIdAction(hashtagId: Int) =
    DbAction[Unit](implicit session => {
      (Q.u + "DELETE FROM " + igLinksTable + " WHERE hashtag_id=" +? hashtagId).execute
    })


  protected def getHashtagByHashtagAction(hashtag: String) =
    DbAction[Hashtag](implicit session => {
      implicit val rowMap = hashtagResults
      Q.query[(String), Hashtag]("SELECT " + hashtagsColumns + " FROM " + hashtagsTable + " where hashtag = ?").first(hashtag.toLowerCase)
    })


  protected def deleteHashtagAction(hashtag: Hashtag) =
  //iDao.deleteIgLinksByHashtagId(hashtag.id)
    DbAction[Unit](implicit session => {
      (Q.u + "DELETE FROM " + hashtagsTable + " WHERE id=" +? hashtag.id).execute
    })


  protected def deleteHashtagCascadeAction(hashtag: Hashtag) =
    deleteHashtagAction(hashtag).map(_ => deleteIgLinksByHashtagIdAction(hashtag.id))

  protected def createIgLinkAction(igLink: NewInstagramLink) =
    getHashtagByHashtagAction(igLink.hashtag).map(hashtag => insertIgLinkAction(igLink, hashtag))

*/

}
