package rentmycar.server.reservations

import io.ktor.server.plugins.NotFoundException
import kotlinx.datetime.LocalDateTime
import rentmycar.core.CreateReservationRequest
import rentmycar.core.ReservationStatus
import rentmycar.server.cars.CarRepository
import rentmycar.server.common.ConflictException
import rentmycar.server.common.ForbiddenException
import rentmycar.server.users.UserRepository

// The use cases for reservations: request, view and change the status
class ReservationService(
    private val reservationRepository: ReservationRepository,
    private val carRepository: CarRepository,
    private val userRepository: UserRepository
) {
    // Used to make sure two approvals for the same period cannot happen at the same moment
    private val approveLock = Any()

    // A renter asks to reserve a car in a period
    fun requestReservation(request: CreateReservationRequest, userId: Int, now: LocalDateTime): Reservation {
        val car = carRepository.findById(request.carId)
        if (car == null) {
            throw NotFoundException("Car not found.")
        }
        // A deleted car cannot be reserved
        if (!car.isActive) {
            throw NotFoundException("Car not found.")
        }
        val renter = userRepository.findById(userId)
        if (renter == null) {
            throw NotFoundException("User not found.")
        }

        val reservation = Reservation.request(
            car = car,
            renter = renter,
            start = request.start,
            end = request.end,
            expectedKm = request.expectedKm,
            now = now
        )
        return reservationRepository.add(reservation)
    }

    // All reservations of the logged-in user as renter
    fun getMyReservations(userId: Int): List<Reservation> {
        return reservationRepository.findByRenter(userId)
    }

    // All reservations of a car; only the owner of the car can see them
    fun getReservationsForCar(carId: Int, userId: Int): List<Reservation> {
        val car = carRepository.findById(carId)
        if (car == null) {
            throw NotFoundException("Car not found.")
        }
        if (car.owner.id != userId) {
            throw ForbiddenException("This is not your car.")
        }
        return reservationRepository.findByCar(carId)
    }

    // One reservation; only the renter and the owner of the car can see it
    fun getReservation(id: Int, userId: Int): Reservation {
        val reservation = findReservation(id)
        val isRenter = reservation.renter.id == userId
        val isOwner = reservation.car.owner.id == userId
        if (!isRenter && !isOwner) {
            throw ForbiddenException("This is not your reservation.")
        }
        return reservation
    }

    // Approves, rejects or cancels a reservation
    fun changeStatus(id: Int, newStatus: ReservationStatus, userId: Int, now: LocalDateTime): Reservation {
        val reservation = findReservation(id)

        when (newStatus) {
            ReservationStatus.APPROVED -> approve(reservation, userId, now)
            ReservationStatus.REJECTED -> reject(reservation, userId)
            ReservationStatus.CANCELLED -> cancel(reservation, userId, now)
            ReservationStatus.REQUESTED -> throw IllegalArgumentException("A reservation cannot be set back to requested.")
        }
        return reservation
    }

    // Rejects a request; only the owner of the car can do this
    private fun reject(reservation: Reservation, userId: Int) {
        checkIsOwner(reservation, userId)
        reservation.reject()
        reservationRepository.update(reservation)
    }

    // Cancels a reservation; only the renter can do this
    private fun cancel(reservation: Reservation, userId: Int, now: LocalDateTime) {
        val isRenter = reservation.renter.id == userId
        if (!isRenter) {
            throw ForbiddenException("Only the renter can cancel a reservation.")
        }
        reservation.cancel(now)
        reservationRepository.update(reservation)
    }

    // Approves a request; only the owner, and only when there is no overlap
    private fun approve(reservation: Reservation, userId: Int, now: LocalDateTime) {
        checkIsOwner(reservation, userId)

        // Check for overlap and save in one step, so two approvals at the same moment cannot both succeed
        synchronized(approveLock) {
            val approvedReservations = reservationRepository.findApprovedForCar(reservation.car.id)
            for (other in approvedReservations) {
                if (other.overlapsWith(reservation)) {
                    throw ConflictException("This car is already rented in this period.")
                }
            }
            reservation.approve(now)
            reservationRepository.update(reservation)
        }
    }

    // Gives a 403 when the user is not the owner of the car
    private fun checkIsOwner(reservation: Reservation, userId: Int) {
        val isOwner = reservation.car.owner.id == userId
        if (!isOwner) {
            throw ForbiddenException("Only the owner of the car can do this.")
        }
    }

    // Returns the reservation, or gives a 404 when it does not exist
    private fun findReservation(id: Int): Reservation {
        val reservation = reservationRepository.findById(id)
        if (reservation == null) {
            throw NotFoundException("Reservation not found.")
        }
        return reservation
    }
}
