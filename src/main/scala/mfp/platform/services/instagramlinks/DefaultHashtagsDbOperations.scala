package mfp.platform.services.instagramlinks

import mfp.platform.db.DbActor.DbRequest
import mfp.platform.services.UIDFactory
import akka.actor.ActorRef
import mfp.platform.services.instagramlinks.DefaultInstagramLinksDbOperations

class DefaultHashtagsDbOperations ( val databases: Databases,
                                    val dbActor: ActorRef,
                                    igLinksOps: => DefaultInstagramLinksDbOperations) extends HashtagsDbOperations{

  override def createHashtag(hashtag: NewHashtag, replyTo: ActorRef) = {
    dbActor ! new DbRequest(databases.igLinksDb, createHashtagAction(hashtag),
      Some(replyTo))
  }

  override def updateHashtag(hashtag: Hashtag, replyTo: ActorRef) = {
    dbActor ! new DbRequest(databases.igLinksDb, updateHashtagAction(hashtag),
      Some(replyTo))
  }

  override def getAllHashtags(replyTo: ActorRef) = {
    dbActor ! new DbRequest(databases.igLinksDb, getAllHashtagsAction,
      Some(replyTo))
  }

  override def getHashtagById(id: Int, replyTo: ActorRef) = {
    dbActor ! new DbRequest(databases.igLinksDb, getHashtagByIdAction(id),
      Some(replyTo))
  }

  override def getHashtagByHashtag(hashtag: String, replyTo: ActorRef) = {
    dbActor ! new DbRequest(databases.igLinksDb, getHashtagByHashtagAction(hashtag),
      Some(replyTo))
  }

  override def countAllHashtags(replyTo: ActorRef) = {
    dbActor ! new DbRequest(databases.igLinksDb, countAllHashtagsAction,
      Some(replyTo))
  }

  override def deleteHashtag(hashtag: Hashtag, replyTo: ActorRef) = {
    dbActor ! new DbRequest(databases.igLinksDb, deleteHashtagAction(hashtag),
      Some(replyTo))
  }

  def deleteHashtagCascade(hashtag: Hashtag, replyTo: ActorRef) = {
    dbActor ! new DbRequest(databases.igLinksDb, deleteHashtagAction(hashtag).map(_ => igLinksOps.deleteIgLinksByHashtagIdAction(hashtag.id)),
      Some(replyTo))
  }

}
