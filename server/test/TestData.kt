package rentmycar.server

import kotlinx.datetime.LocalDateTime
import rentmycar.core.FuelType
import rentmycar.server.cars.Address
import rentmycar.server.cars.Car
import rentmycar.server.cars.ElectricCar
import rentmycar.server.cars.HydrogenCar
import rentmycar.server.cars.IceCar
import rentmycar.server.users.JwtConfig
import rentmycar.server.users.User

// Fixed test data, so every test gives the same result

// The token settings for the tests, instead of application.conf
val TEST_JWT_CONFIG = JwtConfig(
    secret = "a-test-secret-that-is-long-enough-for-hmac",
    issuer = "http://localhost:8080",
    audience = "rentmycar-api",
    realm = "rentmycar"
)

// "Now" in the tests: always the same moment
val NOW = LocalDateTime(2030, 1, 1, 12, 0)

val OWNER = User(id = 1, name = "Eva", email = "eva@example.com", passwordHash = "hash")
val RENTER = User(id = 2, name = "Daan", email = "daan@example.com", passwordHash = "hash")
val OTHER_USER = User(id = 3, name = "Sanne", email = "sanne@example.com", passwordHash = "hash")

val TEST_ADDRESS = Address(street = "Hogeschoollaan", houseNumber = "1", postalCode = "4818 CR", city = "Breda")

// A moment on 10 January 2030 at a whole hour
fun jan10(hour: Int): LocalDateTime {
    return LocalDateTime(2030, 1, 10, hour, 0)
}

// The petrol car from the example in docs/aannames.md: 30.000 euro and 6 liters per 100 km
fun petrolCar(id: Int = 1, owner: User = OWNER): Car {
    return IceCar(
        id = id,
        owner = owner,
        brand = "Volkswagen",
        model = "Golf",
        year = 2019,
        licensePlate = "AB-123-C",
        seats = 5,
        hourlyRate = 6.00,
        kmRate = 0.25,
        purchasePrice = 30000.0,
        address = TEST_ADDRESS,
        fuelType = FuelType.PETROL,
        litersPer100Km = 6.0
    )
}

// An electric car for the tests
fun electricCar(id: Int = 2, owner: User = OWNER): Car {
    return ElectricCar(
        id = id,
        owner = owner,
        brand = "Tesla",
        model = "Model 3",
        year = 2023,
        licensePlate = "R-123-XY",
        seats = 5,
        hourlyRate = 8.50,
        kmRate = 0.20,
        purchasePrice = 35000.0,
        address = TEST_ADDRESS,
        kWhPer100Km = 17.0
    )
}

// A hydrogen car for the tests
fun hydrogenCar(id: Int = 3, owner: User = OWNER): Car {
    return HydrogenCar(
        id = id,
        owner = owner,
        brand = "Toyota",
        model = "Mirai",
        year = 2021,
        licensePlate = "U-321-CD",
        seats = 5,
        hourlyRate = 10.00,
        kmRate = 0.30,
        purchasePrice = 65000.0,
        address = TEST_ADDRESS,
        kgPer100Km = 1.0
    )
}
