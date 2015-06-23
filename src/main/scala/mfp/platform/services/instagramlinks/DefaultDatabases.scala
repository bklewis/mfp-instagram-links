package mfp.platform.services.instagramlinks

import mfp.platform.db.DatabaseProvider


/*
This implementation uses the DatabaseProvider mixed-in to lookup the required
databases by their configuration key.
*/
trait DefaultDatabases extends Databases {
  this: DatabaseProvider ⇒

  // NOTE: This could also be a lazy val, but the internal dbFor lookup is cheap which
  // makes simply a matter of preference.
  override def instagramDb = dbFor("instagram-db").get

}

