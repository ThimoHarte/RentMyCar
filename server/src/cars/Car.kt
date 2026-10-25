package cars

import rentmycar.server.common.CostAssumptions
import rentmycar.server.users.User

// A car that an owner offers for rent (sealed: you cannot make a Car itself, only a subclass like IceCar)
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
    initiallyActive: Boolean = true
) {
    // Starts with the value from the constructor (private set: only deactivate() can change it)
    var isActive: Boolean = initiallyActive
        private set

    init {
        require(brand.isNotBlank()) { "Brand is required." }
        require(model.isNotBlank()) { "Model is required." }
        require(licensePlate.isNotBlank()) { "License plate is required." }
        require(seats > 0) { "A car must have at least 1 seat." }
        require(hourlyRate >= 0) { "Hourly rate cannot be negative." }
        require(kmRate >= 0) { "Km rate cannot be negative." }
        require(purchasePrice >= 0) { "Purchase price cannot be negative." }
    }

    // The energy costs per km (abstract: no code here, because every kind of car calculates this in its own way)
    abstract fun costPerKm(): Double

    // Insurance, road tax and maintenance per year (abstract: no code here, because every kind of car has its own values)
    abstract fun fixedCostsPerYear(): Double

    // Total cost of ownership per year (see docs/aannames.md)
    fun tco(): Double {
        // The car loses some of its value every year
        val depreciation = purchasePrice * CostAssumptions.DEPRECIATION_RATE
        // Insurance, road tax and maintenance
        val fixedCosts = fixedCostsPerYear()
        // Fuel, electricity or hydrogen for all km in a year
        val energyCosts = CostAssumptions.KM_PER_YEAR * costPerKm()

        val tco = depreciation + fixedCosts + energyCosts
        return tco
    }

    // Price for renting this car for some hours and kilometers
    fun rentalPrice(hours: Double, km: Int): Double {
        val priceForHours = hours * hourlyRate
        val priceForKm = km * kmRate

        val price = priceForHours + priceForKm
        return price
    }

    // A car is never really deleted; it becomes inactive
    fun deactivate() {
        isActive = false
    }

    // Belongs to the class, not to one car (companion object: used as Car.NEW_ID, also before a car exists)
    companion object {
        // Id of a car that is not saved yet; the database gives the real id
        const val NEW_ID = 0
    }
}
