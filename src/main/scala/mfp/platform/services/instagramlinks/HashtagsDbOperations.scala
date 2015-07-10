package mfp.platform.services.instagramlinks

import mfp.platform.db.DbAction
import akka.actor.ActorRef
import scala.slick.jdbc.{GetResult, StaticQuery => Q}


trait HashtagsDbOperations {

  val table = "hashtags"
  val columns = "id, hashtag, admin_username, created_at, updated_at"
  val columnsCreate = "hashtag, admin_username, updated_at"

  val hashtagResults = GetResult(r => Hashtag(r.<<, r.<<, r.<<, new java.sql.Timestamp(r.<<), new java.sql.Timestamp(r.<<)))

  //val iDao = new DefaultIgLinksDAO

  protected def createHashtagAction(hashtag: NewHashtag) =
    DbAction[Unit](implicit session => {
      implicit val rowMap = hashtagResults
      (Q.u + "INSERT INTO " + table + " (" + columnsCreate + ") VALUES ("
        +? hashtag.hashtag.toLowerCase + ","
        +? hashtag.adminUsername + ","
        +? new java.sql.Timestamp(System.currentTimeMillis()).toString +")").execute
    })

  protected def updateHashtagAction(hashtag: Hashtag) =
    DbAction[Unit](implicit session => {
      implicit val rowMap = hashtagResults
      (Q.u + "UPDATE " + table
        + " SET admin_username=" +? hashtag.adminUsername
        + ", updated_at=" +? new java.sql.Timestamp(System.currentTimeMillis()).toString
        + " WHERE id=" +? hashtag.id).execute
    })

  protected def getAllHashtagsAction =
    DbAction[Seq[Hashtag]](implicit session => {
      implicit val rowMap = hashtagResults
      //(Q.u + "SELECT " + columns + " FROM " + table).list
      Q.queryNA[Hashtag]("SELECT " + columns + " FROM " + table).list
    })

  protected def getHashtagByIdAction(id: Int) =
    DbAction[Hashtag](implicit session => {
      implicit val rowMap = hashtagResults
      Q.query[(Int), Hashtag]("SELECT " + columns + " FROM " + table + " where id = ?").first(id)
    })

  def getHashtagByHashtagAction(hashtag: String) =
    DbAction[Hashtag](implicit session => {
      implicit val rowMap = hashtagResults
      Q.query[(String), Hashtag]("SELECT " + columns + " FROM " + table + " where hashtag = ?").first(hashtag.toLowerCase)
    })

  protected def deleteHashtagAction(hashtag: Hashtag) =
    //iDao.deleteIgLinksByHashtagId(hashtag.id)
    DbAction[Unit](implicit session => {
      (Q.u + "DELETE FROM " + table + " WHERE id=" +? hashtag.id).execute
    })

  def countAllHashtagsAction = 
    DbAction[Int](implicit session => {
      Q.queryNA[Int]("SELECT COUNT(*) FROM " + table).first
    })
  

  def createHashtag(hashtag: NewHashtag, replyTo: ActorRef): Unit

  def updateHashtag(hashtag: Hashtag, replyTo: ActorRef): Unit

  def getAllHashtags(replyTo: ActorRef): Unit

  def getHashtagById(id: Int, replyTo: ActorRef): Unit

  def getHashtagByHashtag(hashtag: String, replyTo: ActorRef): Unit

  def deleteHashtag(hashtag: Hashtag, replyTo: ActorRef): Unit

  def countAllHashtags(replyTo: ActorRef): Unit

}