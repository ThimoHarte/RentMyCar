package rentmycar.core

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

// Used to request a reservation (data class: only holds data)
@Serializable
data class CreateReservationRequest(
    val carId: Int,
    val start: LocalDateTime,
    val end: LocalDateTime,
    val expectedKm: Int
)

@Serializable
data class ChangeStatusRequest(
    val status: ReservationStatus
)

// The contact details of the owner, only shown after approval
@Serializable
data class OwnerContact(
    val name: String,
    val email: String
)

@Serializable
data class ReservationResponse(
    val id: Int,
    val carId: Int,
    val renterId: Int,
    val start: LocalDateTime,
    val end: LocalDateTime,
    val expectedKm: Int,
    val estimatedPrice: Double,
    val status: ReservationStatus,
    val ownerContact: OwnerContact? = null
)
