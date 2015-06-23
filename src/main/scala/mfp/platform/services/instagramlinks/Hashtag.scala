package mfp.platform.services.instagramlinks

import org.joda.time.DateTime

case class Hashtag (id: Int,
                    hashtag: String,
                    adminUsername: String,
                    createdAt: DateTime,
                    updatedAt: DateTime)