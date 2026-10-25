package rentmycar.server.reservations

import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.insertAndGetId
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update
import rentmycar.core.ReservationStatus
import rentmycar.server.cars.CarRepository
import rentmycar.server.users.UserRepository

// Saves and finds reservations (interface: only the functions, ExposedReservationRepository has the code)
interface ReservationRepository {
    // Finds the reservation with this id, or null when it does not exist
    fun findById(id: Int): Reservation?

    // Finds all reservations of one renter
    fun findByRenter(renterId: Int): List<Reservation>

    // Finds all reservations of one car
    fun findByCar(carId: Int): List<Reservation>

    // Finds the approved reservations of one car
    fun findApprovedForCar(carId: Int): List<Reservation>

    // Saves a new reservation and returns it with the id from the database
    fun add(reservation: Reservation): Reservation

    // Saves the new status of a reservation
    fun update(reservation: Reservation)
}

// The real version: uses the database, and the other repositories to load the car and the renter
class ExposedReservationRepository(
    private val database: Database,
    private val carRepository: CarRepository,
    private val userRepository: UserRepository
) : ReservationRepository {

    // Finds the reservation with this id in the database (override: fills in the function from ReservationRepository)
    override fun findById(id: Int): Reservation? {
        val row = transaction(database) {
            // Find the reservation with this id (SQL: SELECT * FROM reservations WHERE id = <id>)
            val query = ReservationTable.selectAll()
            query.where { ReservationTable.id eq id }
            query.singleOrNull()
        }

        // There is no reservation with this id
        if (row == null) {
            return null
        }
        return toReservation(row)
    }

    // Finds all reservations of one renter in the database (override: fills in the function from ReservationRepository)
    override fun findByRenter(renterId: Int): List<Reservation> {
        val rows = transaction(database) {
            // Find all reservations of one renter, sorted by start time (SQL: SELECT * FROM reservations WHERE renter_id = <renterId> ORDER BY start_time)
            val query = ReservationTable.selectAll()
            query.where { ReservationTable.renter eq renterId }
            query.orderBy(ReservationTable.startTime)
            query.toList()
        }

        val reservations = mutableListOf<Reservation>()
        for (row in rows) {
            reservations.add(toReservation(row))
        }
        return reservations
    }

    // Finds all reservations of one car in the database (override: fills in the function from ReservationRepository)
    override fun findByCar(carId: Int): List<Reservation> {
        val rows = transaction(database) {
            // Find all reservations of one car, sorted by start time (SQL: SELECT * FROM reservations WHERE car_id = <carId> ORDER BY start_time)
            val query = ReservationTable.selectAll()
            query.where { ReservationTable.car eq carId }
            query.orderBy(ReservationTable.startTime)
            query.toList()
        }

        val reservations = mutableListOf<Reservation>()
        for (row in rows) {
            reservations.add(toReservation(row))
        }
        return reservations
    }

    // Finds the approved reservations of one car in the database (override: fills in the function from ReservationRepository)
    override fun findApprovedForCar(carId: Int): List<Reservation> {
        val rows = transaction(database) {
            // Find the approved reservations of one car. Both conditions must be true (SQL: SELECT * FROM reservations WHERE car_id = <carId> AND status = 'APPROVED')
            val query = ReservationTable.selectAll()
            query.where {
                (ReservationTable.car eq carId) and (ReservationTable.status eq ReservationStatus.APPROVED)
            }
            query.toList()
        }

        val reservations = mutableListOf<Reservation>()
        for (row in rows) {
            reservations.add(toReservation(row))
        }
        return reservations
    }

    // Saves a new reservation in the database (override: fills in the function from ReservationRepository)
    override fun add(reservation: Reservation): Reservation {
        val newId = transaction(database) {
            // Add a new row and get the id that the database gives it (SQL: INSERT INTO reservations (car_id, renter_id, ...) VALUES (...))
            ReservationTable.insertAndGetId { row ->
                row[car] = reservation.car.id
                row[renter] = reservation.renter.id
                row[startTime] = reservation.start
                row[endTime] = reservation.end
                row[expectedKm] = reservation.expectedKm
                row[estimatedPrice] = reservation.estimatedPrice
                row[status] = reservation.status
            }
        }
        val savedReservation = findById(newId.value)
        if (savedReservation == null) {
            throw RuntimeException("The reservation was not saved.")
        }
        return savedReservation
    }

    // Saves the new status in the database; only the status can change (override: fills in the function from ReservationRepository)
    override fun update(reservation: Reservation) {
        transaction(database) {
            // Change the status in the row of this reservation (SQL: UPDATE reservations SET status = <status> WHERE id = <reservation.id>)
            ReservationTable.update({ ReservationTable.id eq reservation.id }) { row ->
                row[status] = reservation.status
            }
        }
    }

    // Turns a row of the table into a Reservation object, with its car and renter
    private fun toReservation(row: ResultRow): Reservation {
        val carId = row[ReservationTable.car].value
        val renterId = row[ReservationTable.renter].value

        val car = carRepository.findById(carId)
        if (car == null) {
            throw RuntimeException("Car $carId of a reservation does not exist.")
        }
        val renter = userRepository.findById(renterId)
        if (renter == null) {
            throw RuntimeException("User $renterId of a reservation does not exist.")
        }

        return Reservation(
            id = row[ReservationTable.id].value,
            car = car,
            renter = renter,
            start = row[ReservationTable.startTime],
            end = row[ReservationTable.endTime],
            expectedKm = row[ReservationTable.expectedKm],
            estimatedPrice = row[ReservationTable.estimatedPrice],
            initialStatus = row[ReservationTable.status]
        )
    }
}
