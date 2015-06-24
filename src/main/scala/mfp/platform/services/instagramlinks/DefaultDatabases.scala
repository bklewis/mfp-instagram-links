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
  override def igLinksDb = dbFor("ig-links-db").get

}

// Set up functions that call SQL Queries given parameters (table, id, etc)
// Set up functions that call Slick Queries given parameters (table, id, etc)
// Set up a group of functions for each table (EG selectAllIGLinks, selectLinksByHashtag(hashtag: String))
// Write out all of the queries you'll need in slick with some parameters (EG hashtag, linkId, etc)