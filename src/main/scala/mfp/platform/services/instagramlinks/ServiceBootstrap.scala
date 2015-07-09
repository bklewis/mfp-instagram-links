package mfp.platform.services.instagramlinks

import java.text.SimpleDateFormat

import akka.event.Logging
import mfp.platform.db.DbActor.DbResponse
import mfp.platform.db.{DbActor, DbConfig, PooledDatabaseProvider}
import akka.actor._
import java.sql.Timestamp

import scala.concurrent.Future
import scala.slick.util.SQLBuilder.Result

/*class MyActor extends Actor {
  val log = Logging(context.system, this)
  def receive = {
    case "test" => log.info("received test")
    case _ => log.info("received unknown message")
  }
}*/

/*case class HashtagMsg(hashtag: Hashtag)
case class CreateHashtag(hashtag: NewHashtag)
case class ReadHashtag(id: Int)
case class UpdateHashtag(hashtag: Hashtag)
case class DeleteHashtag(hashtag: Hashtag)

class DbActor extends Actor with ActorLogging {
  def receive = {
    case Hashtag => {
      print("CASE HASHTAG: ")
      //log.info(Hashtag.toString)
      println(Hashtag.toString)
    }
    case HashtagMsg(hashtag) => {
      print("CASE HASHTAGMSG: ")
      println("2. " + hashtag.toString)
    }
    case _ => {
      println("NOT A HASHTAG")
    }
  }
}

class HashtagActor extends Actor with ActorLogging {
  var iDao = new DefaultIgLinksDAO
  var hDao = new DefaultHashtagsDAO
  var bDao = new DefaultBannedUsersDAO
  def receive = {
    case CreateHashtag(hashtag) =>
      println("Creating hashtag: " + hashtag.toString)
      hDao.createNewHashtag(hashtag)
    case ReadHashtag(id) =>
      val hashtag = hDao.getHashtagById(id)
      println("Reading hashtag: " + hashtag.toString)
    case UpdateHashtag(hashtag) =>
      println("Updating hashtag: " + hashtag.toString)
      hDao.updateHashtagById(hashtag)
    case DeleteHashtag(hashtag) =>
      println("Deleting hashtag: " + hashtag.toString)
      hDao.deleteHashtag(hashtag)
    case _ =>
      println("UNKNOWN MESSAGE")
  }
}*/

object ServiceBootstrap extends App {

  class ResponseHandler extends Actor {
    override def receive = {
      case r: DbResponse => println(r.result.toString)
    }
  }

  println("Hello, world!")

  import akka.pattern.{ ask, pipe }

  //val dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS")


  /*val f: Future[Hashtag] =
    for {
      id <- ask(myActor, InstagramLink).mapTo[Int]
      hashtag <- ask(myActor, InstagramLink).mapTo[String]
      adminUsername <- ask(myActor, InstagramLink).mapTo[String]
      createdAt <- ask(myActor, InstagramLink).mapTo[String]
      updatedAt <- ask(myActor, InstagramLink).mapTo[String]
    } yield Hashtag(id, hashtag, adminUsername, new java.sql.Timestamp(dateFormat.parse(createdAt).getTime()), new Timestamp(dateFormat.parse(updatedAt).getTime()))*/




  val databases = new DefaultDatabases with PooledDatabaseProvider with DbConfig
  //Database.forURL("jdbc:mysql://localhost:8889/ig_links", user = "blewis", password = "testdb")

  val db = databases.igLinksDb
//
//  var iDao = new DefaultIgLinksDAO
//  var hDao = new DefaultHashtagsDAO
//  var bDao = new DefaultBannedUsersDAO
//
  var currentTimestamp = new java.sql.Timestamp(System.currentTimeMillis())
//
//  var count = iDao.countAllIgLinks
//  println(count)

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

  val system = ActorSystem("foo")

  sys.addShutdownHook(system.shutdown())


  val dbActor = system.actorOf(Props[DbActor], "db-actor")
  val rh = system.actorOf(Props[ResponseHandler])
  val dbOperations = new DefaultHashtagsDbOperations(databases = databases, dbActor = dbActor)

  dbOperations.countAllHashtags(rh)
  dbOperations.createHashtag(new NewHashtag("Studmuffin", "admin3", currentTimestamp), rh)

  Thread.sleep(10000L)



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

/*object ScalaJdbcConnectSelect extends App{

  override def main(args: Array[String]): Unit = {
    import java.sql.{DriverManager, ResultSet}

    // Change to Your Database Config
    val conn_str = "jdbc:mysql://localhost:8889/ig_links?user=blewis&password=testdb"

    // Load the driver
    classOf[com.mysql.jdbc.Driver]

    // Setup the connection
    val conn = DriverManager.getConnection(conn_str)
    try {
      // Configure to be Read Only
      val statement = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE)

      val prep = conn.prepareStatement("INSERT INTO ig_links" +
        "(url, hashtag_id, ig_username, ig_postdate, status, admin_username, created_at, updated_at) VALUES " +
        "('https://instagram.com/p/4P239PExhr/', " +
        "1, " +
        "'_itzjustash', " +
        "'2015-06-22 22:15:19', " +
        "'approved', " +
        "'admin1', " +
        "'2015-06-22 22:21:57', " +
        "'2015-06-22 22:21:57');").execute()



      // Execute Query
      //val rs = statement.executeQuery("SELECT quote FROM quotes LIMIT 5")

      // Iterate Over ResultSet
      /*while (rs.next) {
        println(rs.getString("quote"))
      }*/
    } catch {
      case e : Throwable => e.printStackTrace()
    }
    finally {
      conn.close
    }


  }
    // connect to the database named "mysql" on the localhost
    /*val driver = "com.mysql.jdbc.Driver"
    val url = "jdbc:mysql://localhost:8889/ig_links"
    val username = "blewis"
    val password = "testdb"

    var connection:Connection = null

    try {
      // make the connection
      Class.forName(driver)
      connection = DriverManager.getConnection(url, username, password)

      // create the statement, and run the select query
      val statement = connection.createStatement()
      val resultSet = statement.executeQuery("SELECT host, user FROM user")
      while ( resultSet.next() ) {
        val host = resultSet.getString("host")
        val user = resultSet.getString("user")
        println("host, user = " + host + ", " + user)
      }

    connection.close()
  }*/

}*/

/*object ServiceBootstrap extends App {

  import scala.slick.driver.H2Driver.simple._


  // Definition of the InstagramLinks table
  class InstagramLinks(tag: Tag) extends Table[(Int, String, Int, String, DateTime, String, String, DateTime, DateTime, Boolean, DateTime)](tag, "INSTAGRAMLINKS") {
    def id = column[Int]("id", O.PrimaryKey) // This is the primary key column
    def url = column[String]("url")
    def hashtagId = column[Int]("hashtag_id")
    def igUsername = column[String]("ig_username")
    def igPostdate = column[DateTime]("ig_postdate")
    def status = column[String]("status")
    def adminUsername = column[String]("admin_username")
    def createdAt = column[DateTime]("created_at")
    def updatedAt = column[DateTime]("updated_at")
    def starred = column[Boolean]("starred")
    def starredExpiresAt = column[DateTime]("starred_expires_at")
    // Every table needs a * projection with the same type as the table's type parameter
    def * = (id, url, hashtagId, igUsername, igPostdate, status, adminUsername, createdAt, updatedAt, starred, starredExpiresAt)
    // A reified foreign key relation that can be navigated to create a join
    def hashtag = foreignKey("hashtag_fk", hashtagId, hashtags)(_.id)
  }
  val igLinks = TableQuery[InstagramLinks]

  // Definition of the Hashtags table
  class Hashtags (tag: Tag) extends Table[(Int, String, String, DateTime, DateTime)](tag, "HASHTAGS") {
    def id = column[Int]("id", O.PrimaryKey) //This is the primary key column
    def hashtag = column[String]("url")
    def adminUsername = column[String]("hashtag_id")
    def createdAt = column[DateTime]("created_at")
    def updatedAt = column[DateTime]("updated_at")
    def * = (id, hashtag, adminUsername, createdAt, updatedAt)
  }
  val hashtags = TableQuery[Hashtags]


  // Definition of the BannedUsers table
  class BannedUsers(tag: Tag) extends Table[(Int, String, String, String, DateTime, DateTime)](tag, "BANNEDUSERS") {
    def id = column[Int]("id", O.PrimaryKey) //This is the primary key column
    def igUsername = column[String]("ig_username")
    def banReason = column[String]("ban_reason")
    def adminUsername = column[String]("admin_username")
    def createdAt = column[DateTime]("created_at")
    def updatedAt = column[DateTime]("updated_at")
    def * = (id, igUsername, banReason, adminUsername, createdAt, updatedAt)
  }
  val bannedUsers = TableQuery[BannedUsers]


  // Create the tables, including the primary and foreign keys
  (igLinks.ddl ++ hashtags.ddl ++ bannedUsers.ddl).create


  //QUERIES


  //I turned each of the db rows into InstagramLink Objects, but what do I do with them?
  //Add them to a list and return?
  //Simply spit out each one separately?
  //How does implicit/explicit joining work in Slick - is it permanent or temporary?  Which do I use?
  igLinks foreach {case (id, url, hashtagId, igUsername, igPostdate, status, adminUsername, createdAt, updatedAt, starred, starredExpiresAt) =>
    InstagramLink(id, url, hashtagId, igUsername, igPostdate, status, adminUsername, createdAt, updatedAt, starred, starredExpiresAt)
  }









  //Create new link

  // IMPLICIT INNER JOIN?
  val implicitInnerJoin = for {
    i <- igLinks
    h <- hashtags if i.hashtagId === h.id   //or i.hashtag?
  } yield (i.id, h.hashtag)


  // EXPLICIT INNER JOIN?
  val explicitInnerJoin = for {
    (i, h) <- igLinks innerJoin hashtags on (_.hashtagId === _.id)
  } yield (i.id, h.hashtag)


  //jdbc:mysql://localhost:8889/ig_links
  implicit val db = Database.forURL("jdbc:mysql://localhost:8889/ig_links", user = "blewis", password = "testdb", driver = "org.h2.Driver")
}*/


