package rentmycar.server

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import rentmycar.core.FuelType
import rentmycar.server.cars.Car
import rentmycar.server.cars.ElectricCar
import rentmycar.server.cars.ExposedCarRepository
import rentmycar.server.cars.HydrogenCar
import rentmycar.server.cars.IceCar
import rentmycar.server.users.ExposedUserRepository

// Integration test (IT-01): the real repository with H2 in memory; each test: Arrange, Act, Assert
class CarRepositoryTest {

    // IT-01: every kind of car comes back from the database as the same kind
    @Test
    fun `every kind of car comes back as the same kind`() {
        // Arrange: a new, empty database with one owner (nanoTime() gives every test its own database)
        val databaseName = "repo-test-${System.nanoTime()}"
        val database = DatabaseFactory.connect(DatabaseFactory.inMemoryUrl(databaseName))
        val userRepository = ExposedUserRepository(database)
        val carRepository = ExposedCarRepository(database, userRepository)
        val owner = userRepository.add(name = "Eva", email = "eva@example.com", passwordHash = "hash")

        // Act: save one car of each kind and read them back
        val savedIce = carRepository.add(petrolCar(Car.NEW_ID, owner))
        val savedBev = carRepository.add(electricCar(Car.NEW_ID, owner))
        val savedFcev = carRepository.add(hydrogenCar(Car.NEW_ID, owner))

        val ice = carRepository.findById(savedIce.id)
        val bev = carRepository.findById(savedBev.id)
        val fcev = carRepository.findById(savedFcev.id)

        // Assert: every car is still the right kind, with its own data
        assertIs<IceCar>(ice)
        assertEquals(FuelType.PETROL, ice.fuelType)
        assertEquals(6.0, ice.litersPer100Km)
        assertEquals("Breda", ice.address.city)
        assertEquals(owner.id, ice.owner.id)

        assertIs<ElectricCar>(bev)
        assertEquals(17.0, bev.kWhPer100Km)

        assertIs<HydrogenCar>(fcev)
        assertEquals(1.0, fcev.kgPer100Km)
    }
}
