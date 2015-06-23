package mfp.platform.services.instagramlinks

import org.joda.time.DateTime

case class BannedUser (id: Int,
                    igUsername: String,
                    banReason: String,
                    adminUsername: String,
                    createdAt: DateTime,
                    updatedAt: DateTime)