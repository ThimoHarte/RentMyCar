package rentmycar.server.users

import org.jetbrains.exposed.v1.core.dao.id.IntIdTable

// The table "users" (object: Kotlin makes exactly one, used via its name)
object UserTable : IntIdTable("users") {
    val name = varchar("name", 100)
    val email = varchar("email", 255).uniqueIndex()
    val passwordHash = varchar("password_hash", 255)
    val bonusPoints = integer("bonus_points").default(0)
}
