package rentmycar.server

import rentmycar.core.ReservationStatus
import rentmycar.server.cars.Car
import rentmycar.server.cars.CarRepository
import rentmycar.server.reservations.Reservation
import rentmycar.server.reservations.ReservationRepository
import rentmycar.server.users.User
import rentmycar.server.users.UserRepository

// Simple versions of the repositories for the unit tests: they use a list, so no database is needed

class FakeUserRepository(private val users: MutableList<User> = mutableListOf()) : UserRepository {

    // Finds the user with this id in the list (override: fake version for the tests, with a list instead of a database)
    override fun findById(id: Int): User? {
        for (user in users) {
            if (user.id == id) {
                return user
            }
        }
        return null
    }

    // Finds the user with this email address in the list (override: fake version for the tests, with a list instead of a database)
    override fun findByEmail(email: String): User? {
        for (user in users) {
            if (user.email == email) {
                return user
            }
        }
        return null
    }

    // Adds a user to the list and gives it the next id (override: fake version for the tests, with a list instead of a database)
    override fun add(name: String, email: String, passwordHash: String): User {
        // The next free id: 1 for the first user, 2 for the second, and so on
        val newId = users.size + 1
        val user = User(id = newId, name = name, email = email, passwordHash = passwordHash)
        users.add(user)
        return user
    }
}

class FakeCarRepository(private val cars: MutableList<Car> = mutableListOf()) : CarRepository {

    // Finds the car with this id in the list (override: fake version for the tests, with a list instead of a database)
    override fun findById(id: Int): Car? {
        for (car in cars) {
            if (car.id == id) {
                return car
            }
        }
        return null
    }

    // Finds all active cars in the list (override: fake version for the tests, with a list instead of a database)
    override fun findAllActive(): List<Car> {
        val result = mutableListOf<Car>()
        for (car in cars) {
            if (car.isActive) {
                result.add(car)
            }
        }
        return result
    }

    // Finds all cars of one owner in the list (override: fake version for the tests, with a list instead of a database)
    override fun findByOwner(ownerId: Int): List<Car> {
        val result = mutableListOf<Car>()
        for (car in cars) {
            if (car.owner.id == ownerId) {
                result.add(car)
            }
        }
        return result
    }

    // Checks if a car in the list has this license plate (override: fake version for the tests, with a list instead of a database)
    override fun existsByLicensePlate(licensePlate: String): Boolean {
        for (car in cars) {
            if (car.licensePlate == licensePlate) {
                return true
            }
        }
        return false
    }

    // Adds a car to the list (override: fake version for the tests, with a list instead of a database)
    override fun add(car: Car): Car {
        cars.add(car)
        return car
    }

    // Replaces the old version of the car with the new one (override: fake version for the tests, with a list instead of a database)
    override fun update(car: Car) {
        val oldCar = findById(car.id)
        if (oldCar != null) {
            cars.remove(oldCar)
        }
        cars.add(car)
    }
}

class FakeReservationRepository(
    private val reservations: MutableList<Reservation> = mutableListOf()
) : ReservationRepository {

    // Finds the reservation with this id in the list (override: fake version for the tests, with a list instead of a database)
    override fun findById(id: Int): Reservation? {
        for (reservation in reservations) {
            if (reservation.id == id) {
                return reservation
            }
        }
        return null
    }

    // Finds all reservations of one renter in the list (override: fake version for the tests, with a list instead of a database)
    override fun findByRenter(renterId: Int): List<Reservation> {
        val result = mutableListOf<Reservation>()
        for (reservation in reservations) {
            if (reservation.renter.id == renterId) {
                result.add(reservation)
            }
        }
        return result
    }

    // Finds all reservations of one car in the list (override: fake version for the tests, with a list instead of a database)
    override fun findByCar(carId: Int): List<Reservation> {
        val result = mutableListOf<Reservation>()
        for (reservation in reservations) {
            if (reservation.car.id == carId) {
                result.add(reservation)
            }
        }
        return result
    }

    // Finds the approved reservations of one car in the list (override: fake version for the tests, with a list instead of a database)
    override fun findApprovedForCar(carId: Int): List<Reservation> {
        val result = mutableListOf<Reservation>()
        for (reservation in reservations) {
            val isThisCar = reservation.car.id == carId
            val isApproved = reservation.status == ReservationStatus.APPROVED
            if (isThisCar && isApproved) {
                result.add(reservation)
            }
        }
        return result
    }

    // Adds a reservation to the list (override: fake version for the tests, with a list instead of a database)
    override fun add(reservation: Reservation): Reservation {
        reservations.add(reservation)
        return reservation
    }

    // Nothing to do: the list already holds the same object (override: fake version for the tests, with a list instead of a database)
    override fun update(reservation: Reservation) {
        // The objects in the list are the same objects, so nothing has to be done
    }
}
