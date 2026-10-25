package rentmycar.server.cars

import io.ktor.server.plugins.NotFoundException
import kotlinx.datetime.LocalDateTime
import rentmycar.core.CarRequest
import rentmycar.core.CarType
import rentmycar.core.CostsResponse
import rentmycar.core.ReservationStatus
import rentmycar.server.common.ConflictException
import rentmycar.server.common.CostAssumptions
import rentmycar.server.common.EURO_DECIMALS
import rentmycar.server.common.ForbiddenException
import rentmycar.server.common.rounded
import rentmycar.server.reservations.ReservationRepository
import rentmycar.server.users.User
import rentmycar.server.users.UserRepository

// Number of decimals for the cost per km when it is sent to the client
private const val COST_PER_KM_DECIMALS = 4

// The use cases for cars: search, add, change, delete and costs
class CarService(
    private val carRepository: CarRepository,
    private val userRepository: UserRepository,
    private val reservationRepository: ReservationRepository
) {

    // Searches active cars; every filter is optional
    fun search(type: CarType?, city: String?, maxHourlyRate: Double?): List<Car> {
        val allCars = carRepository.findAllActive()
        val foundCars = mutableListOf<Car>()

        for (car in allCars) {
            // Every filled-in filter must match; "continue" skips this car and goes to the next one
            if (type != null && car.type() != type) {
                continue
            }
            if (city != null && !car.address.city.equals(city, ignoreCase = true)) {
                continue
            }
            if (maxHourlyRate != null && car.hourlyRate > maxHourlyRate) {
                continue
            }
            foundCars.add(car)
        }
        return foundCars
    }

    // Returns an active car, or gives a 404 when it does not exist or is deleted
    fun getActiveCar(id: Int): Car {
        val car = carRepository.findById(id)
        if (car == null) {
            throw NotFoundException("Car not found.")
        }
        // A deleted car is inactive; for the user it does not exist anymore
        if (!car.isActive) {
            throw NotFoundException("Car not found.")
        }
        return car
    }

    // Returns all cars of one owner
    fun getCarsOfOwner(ownerId: Int): List<Car> {
        return carRepository.findByOwner(ownerId)
    }

    // Returns the car, but only when the user is the owner (otherwise 403)
    fun getOwnCar(id: Int, userId: Int): Car {
        val car = getActiveCar(id)
        if (car.owner.id != userId) {
            throw ForbiddenException("This is not your car.")
        }
        return car
    }

    // Adds a new car for the logged-in user
    fun createCar(request: CarRequest, userId: Int): Car {
        val owner = userRepository.findById(userId)
        if (owner == null) {
            throw NotFoundException("User not found.")
        }
        if (carRepository.existsByLicensePlate(request.licensePlate)) {
            throw ConflictException("This license plate is already in use.")
        }
        val car = makeCar(Car.NEW_ID, owner, request)
        return carRepository.add(car)
    }

    // Changes a car of the logged-in user
    fun updateCar(id: Int, request: CarRequest, userId: Int): Car {
        val oldCar = getOwnCar(id, userId)
        // Only check the license plate when it is changed
        val plateIsChanged = request.licensePlate != oldCar.licensePlate
        if (plateIsChanged && carRepository.existsByLicensePlate(request.licensePlate)) {
            throw ConflictException("This license plate is already in use.")
        }
        // A new Car object with the same id, so all checks in the init blocks are done again
        val newCar = makeCar(oldCar.id, oldCar.owner, request)
        carRepository.update(newCar)
        return newCar
    }

    // Deletes a car by making it inactive (soft delete)
    fun deleteCar(id: Int, userId: Int, now: LocalDateTime) {
        val car = getOwnCar(id, userId)

        // The car cannot be deleted when it still has open reservations in the future
        val reservations = reservationRepository.findByCar(id)
        for (reservation in reservations) {
            val isOpen = reservation.status == ReservationStatus.REQUESTED ||
                reservation.status == ReservationStatus.APPROVED
            val isInFuture = reservation.end > now
            if (isOpen && isInFuture) {
                throw ConflictException("This car still has reservations in the future.")
            }
        }

        car.deactivate()
        carRepository.update(car)
    }

    // The costs per km and the TCO per year of a car
    fun getCosts(id: Int, userId: Int): CostsResponse {
        val car = getOwnCar(id, userId)

        // Cost per km is small (for example 0.1452), so it gets more decimals than the TCO
        val costPerKm = car.costPerKm().rounded(COST_PER_KM_DECIMALS)
        val tcoPerYear = car.tco().rounded(EURO_DECIMALS)

        return CostsResponse(
            costPerKm = costPerKm,
            tcoPerYear = tcoPerYear,
            kmPerYear = CostAssumptions.KM_PER_YEAR
        )
    }

    // Makes the right kind of car from the data in the request
    private fun makeCar(id: Int, owner: User, request: CarRequest): Car {
        val address = Address(
            street = request.address.street,
            houseNumber = request.address.houseNumber,
            postalCode = request.address.postalCode,
            city = request.address.city
        )

        if (request.type == CarType.ICE) {
            val fuelType = request.fuelType
            val litersPer100Km = request.litersPer100Km
            if (fuelType == null) {
                throw IllegalArgumentException("Fuel type is required for an ICE car.")
            }
            if (litersPer100Km == null) {
                throw IllegalArgumentException("Liters per 100 km is required for an ICE car.")
            }
            return IceCar(
                id = id,
                owner = owner,
                brand = request.brand,
                model = request.model,
                year = request.year,
                licensePlate = request.licensePlate,
                seats = request.seats,
                hourlyRate = request.hourlyRate,
                kmRate = request.kmRate,
                purchasePrice = request.purchasePrice,
                address = address,
                fuelType = fuelType,
                litersPer100Km = litersPer100Km
            )
        }

        if (request.type == CarType.BEV) {
            val kWhPer100Km = request.kWhPer100Km
            if (kWhPer100Km == null) {
                throw IllegalArgumentException("kWh per 100 km is required for a BEV.")
            }
            return ElectricCar(
                id = id,
                owner = owner,
                brand = request.brand,
                model = request.model,
                year = request.year,
                licensePlate = request.licensePlate,
                seats = request.seats,
                hourlyRate = request.hourlyRate,
                kmRate = request.kmRate,
                purchasePrice = request.purchasePrice,
                address = address,
                kWhPer100Km = kWhPer100Km
            )
        }

        // The only kind that is left is FCEV
        val kgPer100Km = request.kgPer100Km
        if (kgPer100Km == null) {
            throw IllegalArgumentException("Kg per 100 km is required for an FCEV.")
        }
        return HydrogenCar(
            id = id,
            owner = owner,
            brand = request.brand,
            model = request.model,
            year = request.year,
            licensePlate = request.licensePlate,
            seats = request.seats,
            hourlyRate = request.hourlyRate,
            kmRate = request.kmRate,
            purchasePrice = request.purchasePrice,
            address = address,
            kgPer100Km = kgPer100Km
        )
    }
}
