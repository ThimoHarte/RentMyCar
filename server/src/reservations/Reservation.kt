package rentmycar.server.reservations

import kotlinx.datetime.LocalDateTime
import rentmycar.core.ReservationStatus
import rentmycar.server.cars.Car
import rentmycar.server.common.hoursBetween
import rentmycar.server.users.User

// A request of a renter to use a car in a period
class Reservation(
    val id: Int,
    val car: Car,
    val renter: User,
    val start: LocalDateTime,
    val end: LocalDateTime,
    val expectedKm: Int,
    val estimatedPrice: Double,
    initialStatus: ReservationStatus = ReservationStatus.REQUESTED
) {
    // Starts with the value from the constructor (private set: only approve(), reject() and cancel() can change it)
    var status: ReservationStatus = initialStatus
        private set

    // The number of hours of this reservation
    fun hours(): Double {
        return hoursBetween(start, end)
    }

    // Two periods overlap when each one starts before the other one ends
    fun overlapsWith(other: Reservation): Boolean {
        val startsBeforeOtherEnds = start < other.end
        val otherStartsBeforeThisEnds = other.start < end

        val overlaps = startsBeforeOtherEnds && otherStartsBeforeThisEnds
        return overlaps
    }

    // The owner approves the request; only possible before the start
    fun approve(now: LocalDateTime) {
        check(status == ReservationStatus.REQUESTED) { "Only a requested reservation can be approved." }
        check(start > now) { "This reservation has already started." }
        status = ReservationStatus.APPROVED
    }

    // The owner rejects the request
    fun reject() {
        check(status == ReservationStatus.REQUESTED) { "Only a requested reservation can be rejected." }
        status = ReservationStatus.REJECTED
    }

    // The renter cancels the reservation; only possible before the start
    fun cancel(now: LocalDateTime) {
        val isOpen = status == ReservationStatus.REQUESTED || status == ReservationStatus.APPROVED
        check(isOpen) { "This reservation can no longer be cancelled." }
        check(start > now) { "This reservation has already started." }
        status = ReservationStatus.CANCELLED
    }

    // Belongs to the class, not to one reservation (companion object: used as Reservation.request(), before a reservation exists)
    companion object {
        // Id of a reservation that is not saved yet; the database gives the real id
        const val NEW_ID = 0

        // Makes a new reservation request; checks all rules and fixes the price
        fun request(
            car: Car,
            renter: User,
            start: LocalDateTime,
            end: LocalDateTime,
            expectedKm: Int,
            now: LocalDateTime
        ): Reservation {
            require(renter.id != car.owner.id) { "You cannot reserve your own car." }
            require(start > now) { "The start must be in the future." }
            require(end > start) { "The end must be after the start." }
            require(hoursBetween(start, end) >= 1.0) { "A reservation must last at least 1 hour." }
            require(expectedKm >= 0) { "Expected km cannot be negative." }

            val hours = hoursBetween(start, end)
            val price = car.rentalPrice(hours, expectedKm)
            return Reservation(
                id = NEW_ID,
                car = car,
                renter = renter,
                start = start,
                end = end,
                expectedKm = expectedKm,
                estimatedPrice = price
            )
        }
    }
}
