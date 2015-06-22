package mfp.platform.services.instagramlinks

import mfp.platform.db.DatabaseProvider
import scala.slick.driver.H2Driver.simple._
import scala.slick.jdbc.StaticQuery.interpolation
import scala.slick.jdbc.GetResult
import scala.slick.driver.JdbcDriver.backend.Database


/*
This implementation uses the DatabaseProvider mixed-in to lookup the required
databases by their configuration key.
*/
trait DefaultDatabases extends Databases {
  this: DatabaseProvider ⇒

  // NOTE: This could also be a lazy val, but the internal dbFor lookup is cheap which
  // makes simply a matter of preference.
  //override def foodDb = dbFor("food-db").get

  val query = sql"select LINKID, HASHTAG, IG_USERNAME from IG-LINKS".as[(String,String,Int)]

  override def instagramDb = dbFor("instagram-db").get
  //override def instagramDb =
  Database.forURL("jdbc:mysql://localhost:8889/bryce-test-db", driver = "org.h2.Driver") withSession {
    implicit session =>
      //queries
        query.list
      /*instagramDb foreach { case (id, url, hashtag, ig_username, ig_postdate, created_at) =>
        println(" " + id + "\t" + url + "\t" + hashtag + "\t" + ig_username + "\t" + ig_postdate + "\t" + created_at)
      }*/

  }
}

