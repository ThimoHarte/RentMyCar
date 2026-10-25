package rentmycar.server.users

import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.insertAndGetId
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

// Saves and finds users (interface: only the functions, ExposedUserRepository has the code)
interface UserRepository {
    // Finds the user with this id, or null when it does not exist
    fun findById(id: Int): User?

    // Finds the user with this email address, or null when it does not exist
    fun findByEmail(email: String): User?

    // Saves a new user and returns it with the id from the database
    fun add(name: String, email: String, passwordHash: String): User
}

// The real version: uses the database
class ExposedUserRepository(private val database: Database) : UserRepository {

    // Finds the user with this id in the database (override: fills in the function from UserRepository)
    override fun findById(id: Int): User? {
        val row = transaction(database) {
            // Find the user with this id (SQL: SELECT * FROM users WHERE id = <id>)
            val query = UserTable.selectAll()
            query.where { UserTable.id eq id }
            query.singleOrNull()
        }

        // There is no user with this id
        if (row == null) {
            return null
        }
        return toUser(row)
    }

    // Finds the user with this email address in the database (override: fills in the function from UserRepository)
    override fun findByEmail(email: String): User? {
        val row = transaction(database) {
            // Find the user with this email address (SQL: SELECT * FROM users WHERE email = <email>)
            val query = UserTable.selectAll()
            query.where { UserTable.email eq email }
            query.singleOrNull()
        }

        // There is no user with this email address
        if (row == null) {
            return null
        }
        return toUser(row)
    }

    // Saves a new user in the database (override: fills in the function from UserRepository)
    override fun add(name: String, email: String, passwordHash: String): User {
        val newId = transaction(database) {
            // Add a new row and get the id that the database gives it (SQL: INSERT INTO users (name, email, password_hash) VALUES (...))
            UserTable.insertAndGetId { row ->
                row[UserTable.name] = name
                row[UserTable.email] = email
                row[UserTable.passwordHash] = passwordHash
            }
        }
        return User(
            id = newId.value,
            name = name,
            email = email,
            passwordHash = passwordHash
        )
    }

    // Turns a row of the table into a User object
    private fun toUser(row: ResultRow): User {
        return User(
            id = row[UserTable.id].value,
            name = row[UserTable.name],
            email = row[UserTable.email],
            passwordHash = row[UserTable.passwordHash],
            bonusPoints = row[UserTable.bonusPoints]
        )
    }
}
