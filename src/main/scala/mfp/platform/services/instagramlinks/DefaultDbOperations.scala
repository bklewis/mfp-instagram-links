package mfp.platform.services.instagramlinks

import akka.actor.ActorRef
import mfp.platform.db.DbActor.DbRequest


class DefaultDbOperations (val databases: Databases,
                           val dbActor: ActorRef ) extends DbOperations {

  // ---------- Default Hashtags Db Operations ----------

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

  //DO WE NEED THIS?
  override def getHashtagByHashtag(hashtag: String, replyTo: ActorRef) = {
    dbActor ! new DbRequest(databases.igLinksDb, getHashtagByHashtagAction(hashtag),
      Some(replyTo))
  }

  override def countAllHashtags(replyTo: ActorRef) = {
    dbActor ! new DbRequest(databases.igLinksDb, countAllHashtagsAction,
      Some(replyTo))
  }

  override def deleteHashtag(hashtag: Hashtag, replyTo: ActorRef) = {
    dbActor ! new DbRequest(databases.igLinksDb, deleteHashtagCascadeAction(hashtag),
      Some(replyTo))
  }


  // ---------- Default IgLinks Db Operations ----------

  // This function calls getHashtagByHashtag on the hashtag string from NewInstagramLink object
  // to get the corresponding Hashtag object from the database, and use it to create the new InstagramLink Object in the ig-links table
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


  // ---------- Default BannedUsers Db Operations ----------


  override def createBannedUser(user: NewBannedUser, replyTo: ActorRef) = {
    dbActor ! new DbRequest(databases.igLinksDb, createBannedUserAction(user),
      Some(replyTo))
  }

  override def updateBannedUser(user: BannedUser, replyTo: ActorRef) = {
    dbActor ! new DbRequest(databases.igLinksDb, updateBannedUserAction(user),
      Some(replyTo))
  }

  override def getAllBannedUsers(replyTo: ActorRef) = {
    dbActor ! new DbRequest(databases.igLinksDb, getAllBannedUsersAction,
      Some(replyTo))
  }

  override def getBannedUserById(id: Int, replyTo: ActorRef) = {
    dbActor ! new DbRequest(databases.igLinksDb, getBannedUserByIdAction(id),
      Some(replyTo))
  }

  override def getBannedUserByUsername(username: String, replyTo: ActorRef) = {
    dbActor ! new DbRequest(databases.igLinksDb, getBannedUserByUsernameAction(username),
      Some(replyTo))
  }

  override def countAllBannedUsers(replyTo: ActorRef) = {
    dbActor ! new DbRequest(databases.igLinksDb, countAllBannedUsersAction,
      Some(replyTo))
  }

  override def deleteBannedUser(user: BannedUser, replyTo: ActorRef) = {
    dbActor ! new DbRequest(databases.igLinksDb, deleteBannedUserAction(user),
      Some(replyTo))
  }


}
