package mfp.platform.services.instagramlinks

import java.sql.Timestamp

case class Hashtag (id: Int,
                    hashtag: String,
                    adminUsername: String,
                    createdAt: Timestamp,
                    updatedAt: Timestamp)