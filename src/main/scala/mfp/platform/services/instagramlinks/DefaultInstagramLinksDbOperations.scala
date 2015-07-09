package mfp.platform.services.instagramlinks

import akka.actor.ActorRef
import mfp.platform.db.DbActor.DbRequest


class DefaultInstagramLinksDbOperations (val databases: Databases,
                                         val dbActor: ActorRef) extends InstagramLinksDbOperations {

  override def createIgLink(igLink: NewInstagramLink, replyTo: ActorRef) = {
    dbActor ! new DbRequest(databases.igLinksDb, createIgLinkAction(igLink),
      Some(replyTo))
  }

  override def updateIgLink(igLink: InstagramLink, replyTo: ActorRef) = {
    dbActor ! new DbRequest(databases.igLinksDb, updateIgLinkAction(igLink),
      Some(replyTo))
  }

  override def getAllIgLinks(replyTo: ActorRef) = {
    dbActor ! new DbRequest(databases.igLinksDb, getAllIgLinksAction,
      Some(replyTo))
  }

  override def getIgLinkById(id: Int, replyTo: ActorRef) = {
    dbActor ! new DbRequest(databases.igLinksDb, getIgLinkByIdAction(id),
      Some(replyTo))
  }

  override def getIgLinksByHashtagApproved(hashtag: String, replyTo: ActorRef) = {
    dbActor ! new DbRequest(databases.igLinksDb, getIgLinksByHashtagApprovedAction(hashtag),
      Some(replyTo))
  }

  override def getIgLinksByHashtagAll(hashtag: String, replyTo: ActorRef) = {
    dbActor ! new DbRequest(databases.igLinksDb, getIgLinksByHashtagAllAction(hashtag),
      Some(replyTo))
  }

  override def getIgLinksByHashtagBanned(hashtag: String, replyTo: ActorRef) = {
    dbActor ! new DbRequest(databases.igLinksDb, getIgLinksByHashtagBannedAction(hashtag),
      Some(replyTo))
  }


  override def deleteIgLink(igLink: InstagramLink, replyTo: ActorRef) = {
    dbActor ! new DbRequest(databases.igLinksDb, deleteIgLinkAction(igLink),
      Some(replyTo))
  }

  override def deleteIgLinksByHashtagId(hashtagId: Int, replyTo: ActorRef) = {
    dbActor ! new DbRequest(databases.igLinksDb, deleteIgLinksByHashtagIdAction(hashtagId),
      Some(replyTo))
  }


  override def countAllIgLinks(replyTo: ActorRef) = {
    dbActor ! new DbRequest(databases.igLinksDb, countAllIgLinksAction,
      Some(replyTo))
  }

  override def countIgLinksByHashtag(hashtag: String, replyTo: ActorRef) = {
    dbActor ! new DbRequest(databases.igLinksDb, countIgLinksByHashtagAction(hashtag),
      Some(replyTo))
  }

  override def countBannedIgLinksByHashtag(hashtag: String, replyTo: ActorRef) = {
    dbActor ! new DbRequest(databases.igLinksDb, countBannedIgLinksByHashtagAction(hashtag),
      Some(replyTo))
  }

}
