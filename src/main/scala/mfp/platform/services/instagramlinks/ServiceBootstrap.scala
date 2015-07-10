package mfp.platform.services.instagramlinks

import akka.event.Logging
import mfp.platform.db.DbActor.DbResponse
import mfp.platform.db.{DbActor, DbConfig, PooledDatabaseProvider}
import akka.actor._
import java.sql.Timestamp

object ServiceBootstrap extends App {

  println("Hello, world!")

  // Response Handler actor
  class ResponseHandler extends Actor {
    override def receive = {
      case r: DbResponse => println(r.result.toString)
    }
  }

  // Connect to the database
  val databases = new DefaultDatabases with PooledDatabaseProvider with DbConfig
  //Database.forURL("jdbc:mysql://localhost:8889/ig_links", user = "blewis", password = "testdb")
  val db = databases.igLinksDb

  var currentTimestamp = new java.sql.Timestamp(System.currentTimeMillis())

  // Create the actor system
  val system = ActorSystem("foo")
  sys.addShutdownHook(system.shutdown())

  // Create the DB Actor
  val dbActor = system.actorOf(Props[DbActor], "db-actor")
  val rh = system.actorOf(Props[ResponseHandler])

  // Create + test hashtags table operations
  val igLinksDbOps = new DefaultInstagramLinksDbOperations(databases, hashtagsDbOps dbActor)
  val hashtagsDbOps = new DefaultHashtagsDbOperations(databases, dbActor, igLinksDbOps)
  val bannedUsersDbOps = new DefaultBannedUsersDbOperations(databases, dbActor)

  //hashtagsDbOps.countAllHashtags(rh)
  //hashtagsDbOps.createHashtag(new NewHashtag("tREeBeaRd", "admin3", currentTimestamp), rh)
  //hashtagsDbOps.updateHashtag(new Hashtag(29, "Studmuffin", "admin4", currentTimestamp, currentTimestamp), rh)
  //hashtagsDbOps.getAllHashtags(rh)
  //hashtagsDbOps.getHashtagByHashtag("treebeard", rh)
  //hashtagsDbOps.getHashtagById(29, rh)
  //val hashtag = hashtagsDbOps.getHashtagByHashtag("lionstigersbearsohmy")
  //hashtagsDbOps.deleteHashtag(new Hashtag(16, "lionstigersbearsohmy", "admin1", currentTimestamp, currentTimestamp), rh)

  // Create + test banned users table operations

  //bannedUsersDbOps.countAllBannedUsers(rh)
  //bannedUsersDbOps.createBannedUser(new NewBannedUser("Tim", "profanity", "admin1", currentTimestamp), rh)
  //bannedUsersDbOps.createBannedUser(new NewBannedUser("Brian", "spam", "admin1", currentTimestamp), rh)
  //bannedUsersDbOps.updateBannedUser(new BannedUser(3, "KnightOfNi", "spam", "admin2", currentTimestamp, currentTimestamp), rh)
  //bannedUsersDbOps.getAllBannedUsers(rh)
  //bannedUsersDbOps.getBannedUserById(2,rh)
  //bannedUsersDbOps.getBannedUserByUsername("Brian", rh)
  //bannedUsersDbOps.deleteBannedUser(new BannedUser(5, "Tim", "boop", "doop", currentTimestamp, currentTimestamp), rh)

  // Create + test banned users table operations

  //igLinksDbOps.countAllIgLinks(rh)
  //igLinksDbOps.countBannedIgLinksByHashtag("allbran", rh)
  //val hashtag = hDao.getHashtagByHashtag("treebeard")

  igLinksDbOps.countIgLinksByHashtag("jarjarbinks", rh)
  //igLinksDbOps.createIgLink(new NewInstagramLink("https://instagram.com/p/123456", hashtag, "princess", null, "approved", "admin3", currentTimestamp, false, currentTimestamp), rh)
  //igLinksDbOps.getAllIgLinks(rh)
  //igLinksDbOps.getIgLinkById(33, rh)
  //igLinksDbOps.getIgLinksByHashtagAll("allbran", rh)
  //igLinksDbOps.getIgLinksByHashtagBanned("allbran", rh)
  //igLinksDbOps.getIgLinksByHashtagApproved("allbran", rh)


  Thread.sleep(10000L)




  //var hashtag1 = new NewHashtag("", "admin1", currentTimestamp)
  //hDao.createNewHashtag(hashtag1)
  //var hashtag2 = hDao.getHashtagByHashtag("pinguinos")
  //var hashtag2id = hashtag2.id
  //hDao.updateHashtagById(new Hashtag(27, "pinguinos", "admin2", currentTimestamp, currentTimestamp))
  //iDao.createNewIgLink(igLink1)

  //println(hDao.countAllHashtags)
  //println(hDao.getAllHashtags mkString "\n")
  //println(hDao.getHashtagById(1).toString)
  //println(hDao.getHashtagByHashtag("jarjarbinks").toString)




  //var hashtag2 = hDao.getHashtagById(13)
  //var igLink1 = new NewInstagramLink("https://instagram.com/p/4XIdsCGqq6/", hashtag1, "josiemurs", new java.sql.Timestamp(1435255295), "banned", "admin1", currentTimestamp, false, currentTimestamp)
  //var igLink2 = new NewInstagramLink("https://instagram.com/p/4W6rbwlysz/", hashtag1, "myfitnesspal", new java.sql.Timestamp(1435256229), "approved", "admin1", currentTimestamp, false, currentTimestamp)
  //var igLink3 = new NewInstagramLink("https://instagram.com/p/testtestte/", hashtag1, "kazzypops88", new java.sql.Timestamp(1435256333), "approved", "admin1", currentTimestamp, true, currentTimestamp)


  // Creating actor system
  //val system = ActorSystem("MySystem")
  //val dbActor = system.actorOf(Props(new DbActor), name = "dbActor")
  //dbActor ! hashtag1
  //dbActor ! new HashtagMsg(hashtag1)

  //val hashtagActor = system.actorOf(Props(new HashtagActor), name = "hashtagActor")
  //hashtagActor ! new CreateHashtag(new NewHashtag("Rattatouie", "admin3", currentTimestamp))


  //iDao.createNewIgLink(igLink1)
  //iDao.createNewIgLink(igLink2)
  //iDao.createNewIgLink(igLink3)
  //println(iDao.getAllIgLinks mkString "\n")
  //println(iDao.getIgLinkById(27).toString)
  //println(iDao.getIgLinksByHashtagAll("allbran").toString)
  //println(iDao.getIgLinksByHashtagApproved("allbran") mkString "\n")
  //println(iDao.countAllIgLinks)
  //println(iDao.countIgLinksByHashtag("allbran"))
  //println(iDao.countBannedIgLinksByHashtag("allbran"))
  //iDao.updateIgLinkById(new InstagramLink(33, "https://instagram.com/p/UPDATEDURL/", hashtag1, "josiemurs", new java.sql.Timestamp(1435255295), "banned", "admin2", currentTimestamp, currentTimestamp, false, currentTimestamp))
  //println(iDao.getIgLinkById(33).toString)

  //hDao.deleteHashtagById(3)

  //bDao.createNewBannedUser(new NewBannedUser("shoopdaboop", "profanity", "admin1", currentTimestamp))
  //bDao.createNewBannedUser(new NewBannedUser("yourfathersmeltofelderberries", "irrelevant", "admin1", currentTimestamp))
  //bDao.updateBannedUserById(new BannedUser(1, "yourmotherwasahamster", "profanity", "admin2", currentTimestamp, currentTimestamp))

  //println(bDao.getAllBannedUsers mkString "\n")
  //println(bDao.getBannedUserById(2).toString)
  //println(bDao.getBannedUserByUsername("yourmotherwasahamster").toString)
  //println(bDao.countAllBannedUsers)
  //println(bDao.deleteBannedUserById(3))

  println("Farewell, world!")
}
