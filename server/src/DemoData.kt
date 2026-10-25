package rentmycar.server

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.LocalTime
import kotlinx.datetime.plus
import rentmycar.core.FuelType
import rentmycar.server.cars.Address
import rentmycar.server.cars.Car
import rentmycar.server.cars.ElectricCar
import rentmycar.server.cars.HydrogenCar
import rentmycar.server.cars.IceCar
import rentmycar.server.common.currentTime
import rentmycar.server.reservations.Reservation

// The password of all demo users
const val DEMO_PASSWORD = "Welkom123!"

// Fills an empty database with realistic test data, so the API can be tried right away
fun addDemoDataWhenEmpty(dependencies: Dependencies) {
    if (dependencies.userRepository.findByEmail("eva@example.com") != null) {
        return
    }
    println("The database is empty: adding demo data.")

    val users = dependencies.userRepository
    val cars = dependencies.carRepository
    val reservations = dependencies.reservationRepository
    val hash = dependencies.passwordHasher.hash(DEMO_PASSWORD)

    // Three users: two owners and one renter
    val eva = users.add(name = "Eva de Vries", email = "eva@example.com", passwordHash = hash)
    val sanne = users.add(name = "Sanne Jansen", email = "sanne@example.com", passwordHash = hash)
    val daan = users.add(name = "Daan Bakker", email = "daan@example.com", passwordHash = hash)

    val breda = Address(street = "Hogeschoollaan", houseNumber = "1", postalCode = "4818 CR", city = "Breda")
    val ginneken = Address(street = "Ginnekenweg", houseNumber = "120", postalCode = "4835 NK", city = "Breda")
    val tilburg = Address(street = "Professor Goossenslaan", houseNumber = "1", postalCode = "5022 DM", city = "Tilburg")
    val denBosch = Address(street = "Onderwijsboulevard", houseNumber = "215", postalCode = "5223 DE", city = "Den Bosch")

    val golf = IceCar(
        id = Car.NEW_ID,
        owner = eva,
        brand = "Volkswagen",
        model = "Golf",
        year = 2019,
        licensePlate = "AB-123-C",
        seats = 5,
        hourlyRate = 6.00,
        kmRate = 0.25,
        purchasePrice = 30000.0,
        address = breda,
        fuelType = FuelType.PETROL,
        litersPer100Km = 6.0
    )
    val savedGolf = cars.add(golf)

    val peugeot = IceCar(
        id = Car.NEW_ID,
        owner = eva,
        brand = "Peugeot",
        model = "308 SW",
        year = 2018,
        licensePlate = "GH-456-J",
        seats = 5,
        hourlyRate = 5.50,
        kmRate = 0.22,
        purchasePrice = 24000.0,
        address = ginneken,
        fuelType = FuelType.DIESEL,
        litersPer100Km = 5.0
    )
    cars.add(peugeot)

    val dacia = IceCar(
        id = Car.NEW_ID,
        owner = sanne,
        brand = "Dacia",
        model = "Jogger",
        year = 2022,
        licensePlate = "KL-789-M",
        seats = 7,
        hourlyRate = 5.00,
        kmRate = 0.18,
        purchasePrice = 22000.0,
        address = tilburg,
        fuelType = FuelType.LPG,
        litersPer100Km = 8.0
    )
    cars.add(dacia)

    val tesla = ElectricCar(
        id = Car.NEW_ID,
        owner = eva,
        brand = "Tesla",
        model = "Model 3",
        year = 2023,
        licensePlate = "R-123-XY",
        seats = 5,
        hourlyRate = 8.50,
        kmRate = 0.20,
        purchasePrice = 35000.0,
        address = breda,
        kWhPer100Km = 17.0
    )
    val savedTesla = cars.add(tesla)

    val kia = ElectricCar(
        id = Car.NEW_ID,
        owner = sanne,
        brand = "Kia",
        model = "EV6",
        year = 2022,
        licensePlate = "S-456-ZZ",
        seats = 5,
        hourlyRate = 9.00,
        kmRate = 0.20,
        purchasePrice = 40000.0,
        address = tilburg,
        kWhPer100Km = 18.0
    )
    cars.add(kia)

    val renault = ElectricCar(
        id = Car.NEW_ID,
        owner = sanne,
        brand = "Renault",
        model = "Zoe",
        year = 2021,
        licensePlate = "T-789-AB",
        seats = 4,
        hourlyRate = 4.50,
        kmRate = 0.15,
        purchasePrice = 25000.0,
        address = denBosch,
        kWhPer100Km = 15.0
    )
    cars.add(renault)

    val toyota = HydrogenCar(
        id = Car.NEW_ID,
        owner = sanne,
        brand = "Toyota",
        model = "Mirai",
        year = 2021,
        licensePlate = "U-321-CD",
        seats = 5,
        hourlyRate = 10.00,
        kmRate = 0.30,
        purchasePrice = 65000.0,
        address = denBosch,
        kgPer100Km = 1.0
    )
    cars.add(toyota)

    // Reservations of Daan, in the coming days
    val now = currentTime()
    val inTwoDays = atTime(now, 2, 9)
    val inThreeDays = atTime(now, 3, 10)

    // The saved cars are used, because they have the real id from the database
    val first = Reservation.request(
        car = savedGolf,
        renter = daan,
        start = inTwoDays,
        end = plusHours(inTwoDays, 4),
        expectedKm = 80,
        now = now
    )
    first.approve(now)
    reservations.add(first)

    val second = Reservation.request(
        car = savedTesla,
        renter = daan,
        start = inThreeDays,
        end = plusHours(inThreeDays, 3),
        expectedKm = 50,
        now = now
    )
    reservations.add(second)

    println("Demo data added. Log in with eva@example.com, sanne@example.com or daan@example.com and password $DEMO_PASSWORD")
}

// A moment some days from now, at a whole hour
private fun atTime(now: LocalDateTime, days: Int, hour: Int): LocalDateTime {
    val date = now.date.plus(days, DateTimeUnit.DAY)
    val time = LocalTime(hour, 0)

    val moment = LocalDateTime(date, time)
    return moment
}

// The same day, some hours later (only for demo data, so it never goes past midnight)
private fun plusHours(moment: LocalDateTime, hours: Int): LocalDateTime {
    val newTime = LocalTime(moment.hour + hours, moment.minute)

    val newMoment = LocalDateTime(moment.date, newTime)
    return newMoment
}
