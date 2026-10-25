package rentmycar.server.cars

import org.jetbrains.exposed.v1.core.ResultRow
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.statements.UpdateBuilder
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.insertAndGetId
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.jetbrains.exposed.v1.jdbc.update
import rentmycar.core.CarType
import rentmycar.server.users.UserRepository

// Saves and finds cars (interface: only the functions, ExposedCarRepository has the code)
interface CarRepository {
    // Finds the car with this id, or null when it does not exist
    fun findById(id: Int): Car?

    // Finds all cars that are not deleted
    fun findAllActive(): List<Car>

    // Finds all cars of one owner, also the deleted ones
    fun findByOwner(ownerId: Int): List<Car>

    // Checks if a car with this license plate already exists
    fun existsByLicensePlate(licensePlate: String): Boolean

    // Saves a new car and returns it with the id from the database
    fun add(car: Car): Car

    // Saves the changes of an existing car
    fun update(car: Car)
}

// The real version: uses the database, and the user repository to load the owner of a car
class ExposedCarRepository(
    private val database: Database,
    private val userRepository: UserRepository
) : CarRepository {

    // Finds the car with this id in the database (override: fills in the function from CarRepository)
    override fun findById(id: Int): Car? {
        val row = transaction(database) {
            // Find the car with this id (SQL: SELECT * FROM cars WHERE id = <id>)
            val query = CarTable.selectAll()
            query.where { CarTable.id eq id }
            query.singleOrNull()
        }

        // There is no car with this id
        if (row == null) {
            return null
        }
        return toCar(row)
    }

    // Finds all active cars in the database (override: fills in the function from CarRepository)
    override fun findAllActive(): List<Car> {
        val rows = transaction(database) {
            // Find all cars that are not deleted, sorted by id (SQL: SELECT * FROM cars WHERE is_active = TRUE ORDER BY id)
            val query = CarTable.selectAll()
            query.where { CarTable.isActive eq true }
            query.orderBy(CarTable.id)
            query.toList()
        }

        val cars = mutableListOf<Car>()
        for (row in rows) {
            cars.add(toCar(row))
        }
        return cars
    }

    // Finds all cars of one owner in the database (override: fills in the function from CarRepository)
    override fun findByOwner(ownerId: Int): List<Car> {
        val rows = transaction(database) {
            // Find all cars of one owner, sorted by id (SQL: SELECT * FROM cars WHERE owner_id = <ownerId> ORDER BY id)
            val query = CarTable.selectAll()
            query.where { CarTable.owner eq ownerId }
            query.orderBy(CarTable.id)
            query.toList()
        }

        val cars = mutableListOf<Car>()
        for (row in rows) {
            cars.add(toCar(row))
        }
        return cars
    }

    // Counts the cars with this license plate in the database (override: fills in the function from CarRepository)
    override fun existsByLicensePlate(licensePlate: String): Boolean {
        val numberOfCars = transaction(database) {
            // Count the cars with this license plate (SQL: SELECT COUNT(*) FROM cars WHERE license_plate = <licensePlate>)
            val query = CarTable.selectAll()
            query.where { CarTable.licensePlate eq licensePlate }
            query.count()
        }
        val carExists = numberOfCars > 0
        return carExists
    }

    // Saves a new car in the database (override: fills in the function from CarRepository)
    override fun add(car: Car): Car {
        val newId = transaction(database) {
            // Add a new row and get the id that the database gives it (SQL: INSERT INTO cars (owner_id, brand, ...) VALUES (...))
            CarTable.insertAndGetId { row -> writeCar(row, car) }
        }
        val savedCar = findById(newId.value)
        if (savedCar == null) {
            throw RuntimeException("The car was not saved.")
        }
        return savedCar
    }

    // Saves the changes of a car in the database (override: fills in the function from CarRepository)
    override fun update(car: Car) {
        transaction(database) {
            // Change the row of this car (SQL: UPDATE cars SET brand = ..., ... WHERE id = <car.id>)
            CarTable.update({ CarTable.id eq car.id }) { row -> writeCar(row, car) }
        }
    }

    // Puts the values of a car in the columns of the table
    private fun writeCar(row: UpdateBuilder<*>, car: Car) {
        row[CarTable.owner] = car.owner.id
        row[CarTable.brand] = car.brand
        row[CarTable.model] = car.model
        row[CarTable.year] = car.year
        row[CarTable.licensePlate] = car.licensePlate
        row[CarTable.seats] = car.seats
        row[CarTable.hourlyRate] = car.hourlyRate
        row[CarTable.kmRate] = car.kmRate
        row[CarTable.purchasePrice] = car.purchasePrice
        row[CarTable.street] = car.address.street
        row[CarTable.houseNumber] = car.address.houseNumber
        row[CarTable.postalCode] = car.address.postalCode
        row[CarTable.city] = car.address.city
        row[CarTable.isActive] = car.isActive

        // First empty the columns of all kinds, then fill the ones of this kind
        row[CarTable.fuelType] = null
        row[CarTable.litersPer100Km] = null
        row[CarTable.kWhPer100Km] = null
        row[CarTable.kgPer100Km] = null

        when (car) {
            is IceCar -> {
                row[CarTable.type] = CarType.ICE
                row[CarTable.fuelType] = car.fuelType
                row[CarTable.litersPer100Km] = car.litersPer100Km
            }
            is ElectricCar -> {
                row[CarTable.type] = CarType.BEV
                row[CarTable.kWhPer100Km] = car.kWhPer100Km
            }
            is HydrogenCar -> {
                row[CarTable.type] = CarType.FCEV
                row[CarTable.kgPer100Km] = car.kgPer100Km
            }
        }
    }

    // Turns a row of the table into the right kind of Car object, with its owner
    private fun toCar(row: ResultRow): Car {
        val id = row[CarTable.id].value
        val ownerId = row[CarTable.owner].value

        val owner = userRepository.findById(ownerId)
        if (owner == null) {
            throw RuntimeException("User $ownerId of car $id does not exist.")
        }
        val address = Address(
            street = row[CarTable.street],
            houseNumber = row[CarTable.houseNumber],
            postalCode = row[CarTable.postalCode],
            city = row[CarTable.city]
        )

        val type = row[CarTable.type]

        if (type == CarType.ICE) {
            val fuelType = row[CarTable.fuelType]
            val litersPer100Km = row[CarTable.litersPer100Km]
            if (fuelType == null || litersPer100Km == null) {
                throw RuntimeException("ICE car $id has no fuel data in the database.")
            }
            return IceCar(
                id = id,
                owner = owner,
                brand = row[CarTable.brand],
                model = row[CarTable.model],
                year = row[CarTable.year],
                licensePlate = row[CarTable.licensePlate],
                seats = row[CarTable.seats],
                hourlyRate = row[CarTable.hourlyRate],
                kmRate = row[CarTable.kmRate],
                purchasePrice = row[CarTable.purchasePrice],
                address = address,
                initiallyActive = row[CarTable.isActive],
                fuelType = fuelType,
                litersPer100Km = litersPer100Km
            )
        }

        if (type == CarType.BEV) {
            val kWhPer100Km = row[CarTable.kWhPer100Km]
            if (kWhPer100Km == null) {
                throw RuntimeException("BEV $id has no kWh per 100 km in the database.")
            }
            return ElectricCar(
                id = id,
                owner = owner,
                brand = row[CarTable.brand],
                model = row[CarTable.model],
                year = row[CarTable.year],
                licensePlate = row[CarTable.licensePlate],
                seats = row[CarTable.seats],
                hourlyRate = row[CarTable.hourlyRate],
                kmRate = row[CarTable.kmRate],
                purchasePrice = row[CarTable.purchasePrice],
                address = address,
                initiallyActive = row[CarTable.isActive],
                kWhPer100Km = kWhPer100Km
            )
        }

        // The only kind that is left is FCEV
        val kgPer100Km = row[CarTable.kgPer100Km]
        if (kgPer100Km == null) {
            throw RuntimeException("FCEV $id has no kg per 100 km in the database.")
        }
        return HydrogenCar(
            id = id,
            owner = owner,
            brand = row[CarTable.brand],
            model = row[CarTable.model],
            year = row[CarTable.year],
            licensePlate = row[CarTable.licensePlate],
            seats = row[CarTable.seats],
            hourlyRate = row[CarTable.hourlyRate],
            kmRate = row[CarTable.kmRate],
            purchasePrice = row[CarTable.purchasePrice],
            address = address,
            initiallyActive = row[CarTable.isActive],
            kgPer100Km = kgPer100Km
        )
    }
}
