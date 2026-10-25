package rentmycar.server.reservations

import org.jetbrains.exposed.v1.core.dao.id.IntIdTable
import org.jetbrains.exposed.v1.datetime.datetime
import rentmycar.core.ReservationStatus
import rentmycar.server.cars.CarTable
import rentmycar.server.users.UserTable

// The table "reservations"; start_time, because END is a SQL word (object: Kotlin makes exactly one, used via its name)
object ReservationTable : IntIdTable("reservations") {
    val car = reference("car_id", CarTable)
    val renter = reference("renter_id", UserTable)
    val startTime = datetime("start_time")
    val endTime = datetime("end_time")
    val expectedKm = integer("expected_km")
    val estimatedPrice = double("estimated_price")
    val status = enumerationByName<ReservationStatus>("status", 10)
}
