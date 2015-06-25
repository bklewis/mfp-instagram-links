package mfp.platform.services.instagramlinks

import java.sql.Timestamp

case class InstagramLink (id: Option[Int],
                         url: String,
                         hashtag: Hashtag,
                         igUsername: String,
                         igPostdate: Timestamp,
                         status: String,
                         adminUsername: String,
                         createdAt: Timestamp,
                         updatedAt: Timestamp,
                         starred: Boolean = false,
                         starredExpiresAt: Option[Timestamp] = None)
