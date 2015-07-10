package mfp.platform.services.instagramlinks

import akka.actor.ActorRef
import mfp.platform.db.DbAction

import scala.slick.jdbc.{StaticQuery => Q, GetResult}

trait DbOperations {

  // ************************* TABLE MACROS *************************

  // ---------- Hashtags Table ----------

  val hashtagsTable = "hashtags"
  val columns = "id, hashtag, admin_username, created_at, updated_at"
  val columnsCreate = "hashtag, admin_username, updated_at"

  val getHashtagsResult = GetResult(r => Hashtag(r.<<, r.<<, r.<<, new java.sql.Timestamp(r.<<), new java.sql.Timestamp(r.<<)))


  // ---------- IgLinks Table ----------

  val igLinksTable = "ig_links"
  val igLinksColumns = "id, url, hashtag_id, ig_username, ig_postdate, status, admin_username, created_at, updated_at, starred, starred_expires_at"
  val igLinksColumnsCreate = "url, hashtag_id, ig_username, ig_postdate, status, admin_username, updated_at, starred, starred_expires_at"
  val igLinksAndHashtagColumns = "i.id, i.url, i.hashtag_id, h.hashtag, h.admin_username, h.created_at, h.updated_at, i.ig_username, i.ig_postdate, i.status, i.admin_username, i.created_at, i.updated_at, i.starred, i.starred_expires_at"

  val getIgLinksResult = GetResult(r => InstagramLink(r.<<, r.<<, new Hashtag(r.<<, r.<<, r.<<, r.<<, r.<<), r.<<, new java.sql.Timestamp(r.<<), r.<<, r.<<, new java.sql.Timestamp(r.<<), new java.sql.Timestamp(r.<<), r.<<, r.<<))


  // ---------- BannedUsers Table ----------

  val bannedUsersTable = "banned_users"
  val bannedUsersColumns = "id, ig_username, ban_reason, admin_username, created_at, updated_at"
  val bannedUsersColumnsCreate = "ig_username, ban_reason, admin_username, updated_at"

  val getBannedUsersResult = GetResult(r => BannedUser(r.<<, r.<<, r.<<, r.<<, new java.sql.Timestamp(r.<<), new java.sql.Timestamp(r.<<)))



  // ************************* TABLE FUNCTIONS *************************

  // ---------- Hashtags DB Functions ----------
  // All functions are admin-accessible only

  def createHashtag(hashtag: NewHashtag, replyTo: ActorRef): Unit

  def updateHashtag(hashtag: Hashtag, replyTo: ActorRef): Unit

  def getAllHashtags(replyTo: ActorRef): Unit

  def getHashtagById(id: Int, replyTo: ActorRef): Unit

  // Would we ever use this?
  def getHashtagByHashtag(hashtag: String, replyTo: ActorRef): Unit

  // Use cascade delete here?  Kill simplistic delete?
  def deleteHashtag(hashtag: Hashtag, replyTo: ActorRef): Unit

  def countAllHashtags(replyTo: ActorRef): Unit


  // ---------- IgLinks DB Functions ----------

  //Joint Function
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


  // ---------- BannedUsers DB Functions ----------
  // All functions are admin-accessible only

  def createBannedUser(user: NewBannedUser, replyTo: ActorRef): Unit

  def updateBannedUser(user: BannedUser, replyTo: ActorRef): Unit

  def getAllBannedUsers(replyTo: ActorRef): Unit

  def getBannedUserById(id: Int, replyTo: ActorRef): Unit

  def getBannedUserByUsername(igUsername: String, replyTo: ActorRef): Unit

  def deleteBannedUser(user: BannedUser, replyTo: ActorRef): Unit

  def countAllBannedUsers(replyTo: ActorRef): Unit



  // ************************* TABLE ACTIONS *************************

  // ---------- Hashtags DbActions ----------

  protected def createHashtagAction(hashtag: NewHashtag) =
    DbAction[Unit](implicit session => {
      implicit val rowMap = getHashtagsResult
      (Q.u + "INSERT INTO " + hashtagsTable + " (" + columnsCreate + ") VALUES ("
        +? hashtag.hashtag.toLowerCase + ","
        +? hashtag.adminUsername + ","
        +? new java.sql.Timestamp(System.currentTimeMillis()).toString +")").execute
    })

  protected def updateHashtagAction(hashtag: Hashtag) =
    DbAction[Unit](implicit session => {
      implicit val rowMap = getHashtagsResult
      (Q.u + "UPDATE " + hashtagsTable
        + " SET admin_username=" +? hashtag.adminUsername
        + ", updated_at=" +? new java.sql.Timestamp(System.currentTimeMillis()).toString
        + " WHERE id=" +? hashtag.id).execute
    })

  protected def getAllHashtagsAction =
    DbAction[Seq[Hashtag]](implicit session => {
      implicit val rowMap = getHashtagsResult
      Q.queryNA[Hashtag]("SELECT " + columns + " FROM " + hashtagsTable).list
    })

  protected def getHashtagByIdAction(id: Int) =
    DbAction[Hashtag](implicit session => {
      implicit val rowMap = getHashtagsResult
      Q.query[(Int), Hashtag]("SELECT " + columns + " FROM " + hashtagsTable + " where id = ?").first(id)
    })

  // TODO: NOT ACCESSIBLE
  protected def getHashtagByHashtagAction(hashtag: String) =
    DbAction[Hashtag](implicit session => {
      implicit val rowMap = getHashtagsResult
      Q.query[(String), Hashtag]("SELECT " + columns + " FROM " + hashtagsTable + " where hashtag = ?").first(hashtag.toLowerCase)
    })

  // TODO : NOT ACCESSIBLE
  protected def deleteHashtagAction(hashtag: Hashtag) =
    DbAction[Unit](implicit session => {
      (Q.u + "DELETE FROM " + hashtagsTable + " WHERE id=" +? hashtag.id).execute
    })

  protected def countAllHashtagsAction =
    DbAction[Int](implicit session => {
      Q.queryNA[Int]("SELECT COUNT(*) FROM " + hashtagsTable).first
    })

  // Joint Function
  protected def deleteHashtagCascadeAction(hashtag: Hashtag) =
    deleteHashtagAction(hashtag).map(_ => deleteIgLinksByHashtagIdAction(hashtag.id))



  // ---------- IgLink DbActions ----------

  // Joint Function
  protected def createIgLinkAction(igLink: NewInstagramLink) =
    getHashtagByHashtagAction(igLink.hashtag).map(hashtag => insertIgLinkAction(igLink, hashtag))

  protected def insertIgLinkAction(igLink: NewInstagramLink, hashtag: Hashtag) =
    DbAction[Unit](implicit session => {
      implicit val rowMap = getIgLinksResult
      (Q.u + "INSERT INTO " + igLinksTable + " (" + columnsCreate + ") VALUES ("
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

  protected def updateIgLinkAction(igLink: InstagramLink) =
    DbAction[Unit](implicit session => {
      implicit val rowMap = getIgLinksResult
      (Q.u + "UPDATE " + igLinksTable
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
      implicit val rowMap = getIgLinksResult
      Q.queryNA[InstagramLink]("SELECT " + igLinksAndHashtagColumns + " FROM " + igLinksTable + " AS i INNER JOIN " + hashtagsTable
        + " AS h ON i.hashtag_id = h.id").list
    })

  protected def getIgLinkByIdAction(id: Int) =
    DbAction[InstagramLink](implicit session => {
      implicit val rowMap = getIgLinksResult
      Q.query[(Int), InstagramLink]("SELECT " + igLinksAndHashtagColumns + " FROM " + igLinksTable + " AS i INNER JOIN " + hashtagsTable
        + " AS h ON i.hashtag_id = h.id WHERE i.id = ?").first(id)
    })

  protected def getIgLinksByHashtagApprovedAction(hashtag: String) =
    DbAction[Seq[InstagramLink]](implicit session => {
      implicit val rowMap = getIgLinksResult
      Q.query[(String), InstagramLink](
        "SELECT " + igLinksAndHashtagColumns + " FROM " + igLinksTable + " AS i INNER JOIN " + hashtagsTable + " AS h ON i.hashtag_id = h.id" +
          " WHERE h.hashtag = ? AND status = 'approved' ORDER BY i.created_at DESC").list(hashtag)
    })

  protected def getIgLinksByHashtagAllAction(hashtag: String) =
    DbAction[Seq[InstagramLink]](implicit session => {
      implicit val rowMap = getIgLinksResult
      Q.query[String, InstagramLink] (
        "SELECT " + igLinksAndHashtagColumns + " FROM " + igLinksTable + " AS i INNER JOIN " + hashtagsTable + " AS h ON i.hashtag_id = h.id" +
          " WHERE h.hashtag = ? ORDER BY i.created_at DESC").list(hashtag)
    })

  protected def getIgLinksByHashtagBannedAction(hashtag: String) =
    DbAction[Seq[InstagramLink]](implicit session => {
      implicit val rowMap = getIgLinksResult
      Q.query[String, InstagramLink] (
        "SELECT " + igLinksAndHashtagColumns + " FROM " + igLinksTable + " AS i INNER JOIN " + hashtagsTable + " AS h ON i.hashtag_id = h.id" +
          " WHERE h.hashtag = ? AND status = 'banned' ORDER BY i.created_at DESC").list(hashtag)
    })

  // TODO: NOT ACCESSIBLE
  // Delete all links associated with a hashtag (id), to be used to create a cascading delete when a hashtag is deleted
  protected def deleteIgLinksByHashtagIdAction(hashtagId: Int) =
    DbAction[Unit](implicit session => {
      (Q.u + "DELETE FROM " + igLinksTable + " WHERE hashtag_id=" +? hashtagId).execute
    })

  // Delete individual link, to be used if url expires
  protected def deleteIgLinkAction(igLink: InstagramLink) =
    DbAction[Unit](implicit session => {
      (Q.u + "DELETE FROM " + igLinksTable + " WHERE id=" +? igLink.id).first
    })

  def countAllIgLinksAction =
    DbAction[Int](implicit session => {
      Q.queryNA[Int]("SELECT COUNT(*) FROM " + igLinksTable).first
    })

  def countIgLinksByHashtagAction(hashtag: String) =
    DbAction[Int](implicit session => {
      Q.query[String, Int]("SELECT COUNT(*) FROM " + igLinksTable + " AS i INNER JOIN " + hashtagsTable
        + " AS h ON i.hashtag_id = h.id" + " WHERE h.hashtag = ?").first(hashtag)
    })

  def countBannedIgLinksByHashtagAction(hashtag: String) =
    DbAction[Int](implicit session => {
      Q.query[String, Int]("SELECT COUNT(*) FROM " + igLinksTable + " AS i INNER JOIN " + hashtagsTable
        + " AS h ON i.hashtag_id = h.id" + " WHERE h.hashtag = ? AND status = 'banned'").first(hashtag)
    })


  // ---------- BannedUsers DbActions ----------

  protected def createBannedUserAction(user: NewBannedUser) =
    DbAction[Unit](implicit session => {
      implicit val rowMap = getBannedUsersResult
      (Q.u + "INSERT INTO " + bannedUsersTable + " (" + columnsCreate + ") VALUES ("
        +? user.igUsername + ","
        +? user.banReason + ","
        +? user.adminUsername + ","
        +? new java.sql.Timestamp(System.currentTimeMillis()).toString +")").execute
    })

  protected def updateBannedUserAction(user: BannedUser) =
    DbAction[Unit](implicit session => {
      implicit val rowMap = getBannedUsersResult
      (Q.u + "UPDATE " + bannedUsersTable
        + " SET ig_username=" +? user.igUsername
        + ", ban_reason=" +? user.banReason
        + ", admin_username=" +? user.adminUsername
        + ", updated_at=" +? new java.sql.Timestamp(System.currentTimeMillis()).toString
        + " WHERE id=" +? user.id).execute
    })

  protected def getAllBannedUsersAction =
    DbAction[Seq[BannedUser]](implicit session => {
      implicit val rowMap = getBannedUsersResult
      Q.queryNA[BannedUser]("SELECT " + columns + " FROM " + bannedUsersTable).list
    })

  protected def getBannedUserByIdAction(id: Int) =
    DbAction[BannedUser](implicit session => {
      implicit val rowMap = getBannedUsersResult
      Q.query[(Int), BannedUser]("SELECT " + columns + " FROM " + bannedUsersTable + " WHERE id = ?").first(id)
    })

  protected def getBannedUserByUsernameAction(username: String) =
    DbAction[BannedUser](implicit session => {
      implicit val rowMap = getBannedUsersResult
      Q.query[(String), BannedUser]("SELECT " + columns + " FROM " + bannedUsersTable + " WHERE ig_username = ?").first(username)
    })

  //Deleting a banned user unbans them
  protected def deleteBannedUserAction(user: BannedUser) =
    DbAction[Unit](implicit session => {
      (Q.u + "DELETE FROM " + bannedUsersTable + " WHERE id=" +? user.id).execute
    })

  def countAllBannedUsersAction =
    DbAction[Int](implicit session => {
      Q.queryNA[Int]("SELECT COUNT(*) FROM " + bannedUsersTable).first
    })

}
