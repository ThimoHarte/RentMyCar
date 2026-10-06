package rentmycar.server.cars

import rentmycar.server.common.CostAssumptions
import rentmycar.server.users.User

// A car that an owner offers for rent. There are exactly three kinds of cars.
sealed class Car(
    val id: Int,
    val owner: User,
    val brand: String,
    val model: String,
    val year: Int,
    val licensePlate: String,
    val seats: Int,
    val hourlyRate: Double,
    val kmRate: Double,
    val purchasePrice: Double,
    val address: Address,
    val photos: List<Photo> = emptyList()
) {
    // Only the car itself can change this, with deactivate()
    var isActive: Boolean = true
        private set

    init {
        require(seats > 0) { "A car must have at least 1 seat." }
        require(hourlyRate >= 0) { "Hourly rate cannot be negative." }
        require(kmRate >= 0) { "Km rate cannot be negative." }
        require(purchasePrice >= 0) { "Purchase price cannot be negative." }
    }

    // Every kind of car calculates these in its own way
    abstract fun costPerKm(): Double
    abstract fun fixedCostsPerYear(): Double

    // Total cost of ownership per year (see docs/aannames.md)
    fun tco(): Double =
        purchasePrice * CostAssumptions.DEPRECIATION_RATE +
            fixedCostsPerYear() +
            CostAssumptions.KM_PER_YEAR * costPerKm()

    // Price for renting this car for some hours and kilometers
    fun rentalPrice(hours: Double, km: Int): Double =
        hours * hourlyRate + km * kmRate

    // A car is never really deleted; it becomes inactive
    fun deactivate() {
        isActive = false
    }
}
