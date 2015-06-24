package mfp.platform.services.instagramlinks

import java.sql.Timestamp

case class InstagramLink (id: Int = -1,
                         url: String,
                         hashtagId: Int,
                         igUsername: String,
                         igPostdate: Timestamp,
                         status: String,
                         adminUsername: String,
                         createdAt: Timestamp,
                         updatedAt: Timestamp,
                         starred: Boolean = false,
                         starredExpiresAt: Option[Timestamp] = None)
