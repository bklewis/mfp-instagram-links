package mfp.platform.services.instagramlinks

import org.joda.time.DateTime

case class InstagramLink (id: Int,
                         url: String,
                         hashtagId: Int,
                         igUsername: String,
                         igPostdate: DateTime,
                         status: String,
                         adminUsername: String,
                         createdAt: DateTime,
                         updatedAt: DateTime,
                         starred: Boolean,
                         starredExpiresAt: DateTime)
