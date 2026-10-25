package rentmycar.server

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlinx.datetime.LocalDateTime
import rentmycar.server.reservations.Reservation

// Unit test for the overlap rule of a reservation (UT-05); each test: Arrange, Act, Assert
class ReservationTest {

    // Makes a reservation of the petrol car from start to end
    private fun reservation(start: LocalDateTime, end: LocalDateTime): Reservation {
        return Reservation.request(
            car = petrolCar(),
            renter = RENTER,
            start = start,
            end = end,
            expectedKm = 10,
            now = NOW
        )
    }

    // UT-05: overlapsWith gives the right answer in four cases
    @Test
    fun `overlapsWith for four cases`() {
        // Arrange
        val nineToTwelve = reservation(jan10(9), jan10(12))
        val elevenToTwo = reservation(jan10(11), jan10(14))
        val twelveToThree = reservation(jan10(12), jan10(15))
        val oneToThree = reservation(jan10(13), jan10(15))
        val nineToFive = reservation(jan10(9), jan10(17))
        val tenToEleven = reservation(jan10(10), jan10(11))

        // Act
        val partlyTheSameTime = nineToTwelve.overlapsWith(elevenToTwo)
        val startsWhenOtherEnds = nineToTwelve.overlapsWith(twelveToThree)
        val later = nineToTwelve.overlapsWith(oneToThree)
        val inside = nineToFive.overlapsWith(tenToEleven)

        // Assert
        assertTrue(partlyTheSameTime)
        assertFalse(startsWhenOtherEnds)
        assertFalse(later)
        assertTrue(inside)
    }
}
