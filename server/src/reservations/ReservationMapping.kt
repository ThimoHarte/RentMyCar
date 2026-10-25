package rentmycar.server.reservations

import rentmycar.core.OwnerContact
import rentmycar.core.ReservationResponse
import rentmycar.core.ReservationStatus
import rentmycar.server.common.EURO_DECIMALS
import rentmycar.server.common.rounded

// Turns a reservation into the data for the client (extension: extends Reservation with a function, "this" is the reservation)
fun Reservation.toResponse(): ReservationResponse {
    // The contact details of the owner are only added after approval
    var contact: OwnerContact? = null
    if (status == ReservationStatus.APPROVED) {
        contact = OwnerContact(car.owner.name, car.owner.email)
    }

    return ReservationResponse(
        id = id,
        carId = car.id,
        renterId = renter.id,
        start = start,
        end = end,
        expectedKm = expectedKm,
        estimatedPrice = estimatedPrice.rounded(EURO_DECIMALS),
        status = status,
        ownerContact = contact
    )
}
