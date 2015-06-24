package mfp.platform.services.instagramlinks

import java.sql.Timestamp

case class BannedUser (id: Int,
                    igUsername: String,
                    banReason: String,
                    adminUsername: String,
                    createdAt: Timestamp,
                    updatedAt: Timestamp)