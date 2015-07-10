package mfp.platform.services.instagramlinks

import java.sql.Timestamp

case class NewInstagramLink (url: String,
                                hashtag: String, //Hashtag,
                                igUsername: String,
                                igPostdate: Timestamp,
                                status: String,
                                adminUsername: String,
                                updatedAt: Timestamp,
                                starred: Boolean,
                                starredExpiresAt: Timestamp)

case class InstagramLink (id: Int,
                         url: String,
                         hashtag: Hashtag,
                         igUsername: String,
                         igPostdate: Timestamp,
                         status: String,
                         adminUsername: String,
                         createdAt: Timestamp,
                         updatedAt: Timestamp,
                         starred: Boolean,
                         starredExpiresAt: Timestamp)
