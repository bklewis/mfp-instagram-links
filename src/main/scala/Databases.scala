package mfp.platform.services.instagramlinks

import scala.slick.driver.JdbcDriver.backend.Database

/**
 * Provides [[Database]] instances mapped to a particular service
 * database.
 */
//at some point I might need to connect to these
//has to be explicit
trait Databases {

  def instagramDb: Database

}
