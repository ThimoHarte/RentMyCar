package rentmycar.core

// The status of a reservation (enum: a fixed list of values)
enum class ReservationStatus {
    REQUESTED,
    APPROVED,
    REJECTED,
    CANCELLED
}
