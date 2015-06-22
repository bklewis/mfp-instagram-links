package mfp.platform.services.instagramlinks

import akka.actor.ActorRef
import mfp.platform.services.UIDFactory
import mfp.platform.db.DbActor.DbRequest
import mfp.platform.db.DbActor.DbResponse
import scala.slick.driver.H2Driver.simple._
//import mfp.platform.services.instagramlinks.DefaultUserActivityDbOperations

import scala.util.Success

class TestInstagramLinksDbOperations(override val dbActor: ActorRef,
                                   override val databases: Databases,
                                   override val uidGenerator: UIDFactory)
  extends DefaultUserActivityDbOperations(dbActor, databases, uidGenerator) {

  override def fetchFoodDescriptionsById(foodIds: List[String], replyTo: ActorRef): Unit =
    super.fetchFoodDescriptionsById(foodIds, replyTo)

  override def fetchRecentDiaryEntries(userId: String, replyTo: ActorRef): Unit = {
    val dummyReq = new DbRequest(databases.shardDb("1"),
      fetchRecentDiaryEntriesAction(1), Some(replyTo)) with RecentDiaryEntriesRequest

    if (userId == "23654019559212") replyTo ! DbResponse(dummyReq, Success(List[UserFoodEntry]()))
    else super.fetchRecentDiaryEntries(userId, replyTo)
  }
}
