package rentmycar.server

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import rentmycar.core.ReservationStatus
import rentmycar.server.common.ConflictException
import rentmycar.server.reservations.Reservation
import rentmycar.server.reservations.ReservationService

// Unit test for the reservation service with fake repositories (UT-09); each test: Arrange, Act, Assert
class ServiceTest {

    // UT-09: approving is refused when it overlaps with an approved reservation
    @Test
    fun `approving is refused when the car is already rented in that period`() {
        // Arrange: the car is already rented from 9 to 12, and someone asks for 11 to 14
        val car = petrolCar()
        val approved = Reservation(
            id = 1,
            car = car,
            renter = RENTER,
            start = jan10(9),
            end = jan10(12),
            expectedKm = 10,
            estimatedPrice = 20.0,
            initialStatus = ReservationStatus.APPROVED
        )
        val request = Reservation(
            id = 2,
            car = car,
            renter = OTHER_USER,
            start = jan10(11),
            end = jan10(14),
            expectedKm = 10,
            estimatedPrice = 20.0
        )

        val reservationRepository = FakeReservationRepository(mutableListOf(approved, request))
        val carRepository = FakeCarRepository(mutableListOf(car))
        val userRepository = FakeUserRepository(mutableListOf(OWNER, RENTER, OTHER_USER))
        val reservationService = ReservationService(reservationRepository, carRepository, userRepository)

        // Act and Assert: approving must give a ConflictException
        assertFailsWith<ConflictException> {
            reservationService.changeStatus(
                id = request.id,
                newStatus = ReservationStatus.APPROVED,
                userId = OWNER.id,
                now = NOW
            )
        }

        // Assert: the request has not changed
        assertEquals(ReservationStatus.REQUESTED, request.status)
    }
}
