package rentmycar.server

import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import rentmycar.server.cars.CarTable
import rentmycar.server.reservations.ReservationTable
import rentmycar.server.users.UserTable

// Connects to H2 and makes the tables (object: Kotlin makes exactly one, used via its name)
object DatabaseFactory {
    // Stored in a file, so the data is still there after a restart
    const val FILE_URL = "jdbc:h2:file:./data/rentmycar"

    // The address of a database that only exists in memory, used in the tests
    fun inMemoryUrl(name: String): String {
        return "jdbc:h2:mem:$name;DB_CLOSE_DELAY=-1"
    }

    // Connects to the database and makes the tables when they do not exist yet
    fun connect(url: String): Database {
        val database = Database.connect(url, driver = "org.h2.Driver")
        transaction(database) {
            SchemaUtils.create(UserTable, CarTable, ReservationTable)
        }
        return database
    }
}
