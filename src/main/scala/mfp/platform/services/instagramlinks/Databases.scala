package mfp.platform.services.instagramlinks

import scala.slick.driver.JdbcDriver.backend.Database

/**
 * Provides [[Database]] instances mapped to a particular service
 * database.
 */
trait Databases {

  def instagramDb: Database

}
