//package mfp.platform.services.instagramlinks
//
//import mfp.platform.db.DbActor.DbRequest
//import mfp.platform.services.UIDFactory
//import akka.actor.ActorRef
//
//
//class DefaultBannedUsersDbOperations (val databases: Databases,
//                                      val dbActor: ActorRef) extends BannedUsersDbOperations {
//
//  override def createBannedUser(user: NewBannedUser, replyTo: ActorRef) = {
//    dbActor ! new DbRequest(databases.igLinksDb, createBannedUserAction(user),
//      Some(replyTo))
//  }
//
//  override def updateBannedUser(user: BannedUser, replyTo: ActorRef) = {
//    dbActor ! new DbRequest(databases.igLinksDb, updateBannedUserAction(user),
//      Some(replyTo))
//  }
//
//  override def getAllBannedUsers(replyTo: ActorRef) = {
//    dbActor ! new DbRequest(databases.igLinksDb, getAllBannedUsersAction,
//      Some(replyTo))
//  }
//
//  override def getBannedUserById(id: Int, replyTo: ActorRef) = {
//    dbActor ! new DbRequest(databases.igLinksDb, getBannedUserByIdAction(id),
//      Some(replyTo))
//  }
//
//  override def getBannedUserByUsername(username: String, replyTo: ActorRef) = {
//    dbActor ! new DbRequest(databases.igLinksDb, getBannedUserByUsernameAction(username),
//      Some(replyTo))
//  }
//
//  override def countAllBannedUsers(replyTo: ActorRef) = {
//    dbActor ! new DbRequest(databases.igLinksDb, countAllBannedUsersAction,
//      Some(replyTo))
//  }
//
//  override def deleteBannedUser(user: BannedUser, replyTo: ActorRef) = {
//    dbActor ! new DbRequest(databases.igLinksDb, deleteBannedUserAction(user),
//      Some(replyTo))
//  }
//
//}
