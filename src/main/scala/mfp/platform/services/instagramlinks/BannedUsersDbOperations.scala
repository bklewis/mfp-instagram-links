package mfp.platform.services.instagramlinks

import akka.actor.ActorRef
import mfp.platform.db.DbAction
import slick.jdbc.{StaticQuery => Q, GetResult}


trait BannedUsersDbOperations extends DbOperations{

  val table = "banned_users"
  val columns = "id, ig_username, ban_reason, admin_username, created_at, updated_at"
  val columnsCreate = "ig_username, ban_reason, admin_username, updated_at"

  val getResult = GetResult(r => BannedUser(r.<<, r.<<, r.<<, r.<<, new java.sql.Timestamp(r.<<), new java.sql.Timestamp(r.<<)))

  protected def createBannedUserAction(user: NewBannedUser) =
    DbAction[Unit](implicit session => {
      implicit val rowMap = getResult
      (Q.u + "INSERT INTO " + table + " (" + columnsCreate + ") VALUES ("
        +? user.igUsername + ","
        +? user.banReason + ","
        +? user.adminUsername + ","
        +? new java.sql.Timestamp(System.currentTimeMillis()).toString +")").execute
    })

  protected def updateBannedUserAction(user: BannedUser) =
    DbAction[Unit](implicit session => {
      implicit val rowMap = getResult
      (Q.u + "UPDATE " + table
        + " SET ig_username=" +? user.igUsername
        + ", ban_reason=" +? user.banReason
        + ", admin_username=" +? user.adminUsername
        + ", updated_at=" +? new java.sql.Timestamp(System.currentTimeMillis()).toString
        + " WHERE id=" +? user.id).execute
    })

  protected def getAllBannedUsersAction =
    DbAction[Seq[BannedUser]](implicit session => {
      implicit val rowMap = getResult
      Q.queryNA[BannedUser]("SELECT " + columns + " FROM " + table).list
    })

  protected def getBannedUserByIdAction(id: Int) =
    DbAction[BannedUser](implicit session => {
      implicit val rowMap = getResult
      Q.query[(Int), BannedUser]("SELECT " + columns + " FROM " + table + " WHERE id = ?").first(id)
    })

  protected def getBannedUserByUsernameAction(username: String) =
    DbAction[BannedUser](implicit session => {
      implicit val rowMap = getResult
      Q.query[(String), BannedUser]("SELECT " + columns + " FROM " + table + " WHERE ig_username = ?").first(username)
    })

  //Deleting a banned user unbans them
  protected def deleteBannedUserAction(user: BannedUser) =
    DbAction[Unit](implicit session => {
      //Q.update[Int, Int]("DELETE FROM " + table + " WHERE id=?").(hashtag.id)
      (Q.u + "DELETE FROM " + table + " WHERE id=" +? user.id).execute
    })

  def countAllBannedUsersAction =
    DbAction[Int](implicit session => {
      Q.queryNA[Int]("SELECT COUNT(*) FROM " + table).first
    })


  def createBannedUser(user: NewBannedUser, replyTo: ActorRef): Unit

  def updateBannedUser(user: BannedUser, replyTo: ActorRef): Unit

  def getAllBannedUsers(replyTo: ActorRef): Unit

  def getBannedUserById(id: Int, replyTo: ActorRef): Unit

  def getBannedUserByUsername(igUsername: String, replyTo: ActorRef): Unit

  def deleteBannedUser(user: BannedUser, replyTo: ActorRef): Unit

  def countAllBannedUsers(replyTo: ActorRef): Unit

}