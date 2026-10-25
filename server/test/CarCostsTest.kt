package rentmycar.server

import kotlin.test.Test
import kotlin.test.assertEquals

// Unit tests for the cost calculations of a car (UT-01, UT-03); each test: Arrange, Act, Assert
class CarCostsTest {

    // UT-01: the cost per km of the petrol car is 0.1452 euro
    @Test
    fun `cost per km of a petrol car`() {
        // Arrange
        val car = petrolCar()

        // Act
        val costPerKm = car.costPerKm()

        // Assert: 6 liters x 2.42 euro / 100 km
        assertEquals(0.1452, costPerKm, 0.0001)
    }

    // UT-03: the TCO of the petrol car is 9128 euro per year
    @Test
    fun `tco of a petrol car of 30000 euro`() {
        // Arrange
        val car = petrolCar()

        // Act
        val tco = car.tco()

        // Assert: 4500 + 800 + 1000 + 650 + 2178 (see docs/aannames.md)
        assertEquals(9128.0, tco, 0.01)
    }
}
