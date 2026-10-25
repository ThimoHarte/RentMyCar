package rentmycar.server.cars

import rentmycar.server.common.CostAssumptions
import rentmycar.server.users.User

// An electric car that runs on hydrogen (FCEV)
class HydrogenCar(
    id: Int,
    owner: User,
    brand: String,
    model: String,
    year: Int,
    licensePlate: String,
    seats: Int,
    hourlyRate: Double,
    kmRate: Double,
    purchasePrice: Double,
    address: Address,
    initiallyActive: Boolean = true,
    val kgPer100Km: Double
) : Car(
    id, owner, brand, model, year, licensePlate, seats,
    hourlyRate, kmRate, purchasePrice, address, initiallyActive
) {
    init {
        require(kgPer100Km > 0) { "Hydrogen use must be more than 0." }
    }

    // Hydrogen costs per km (override: every kind of car calculates this in its own way)
    override fun costPerKm(): Double {
        val pricePerKg = CostAssumptions.HYDROGEN_PRICE
        val costPer100Km = kgPer100Km * pricePerKg

        val costPerKm = costPer100Km / 100
        return costPerKm
    }

    // Fixed costs per year of a hydrogen car (override: every kind of car has its own values)
    override fun fixedCostsPerYear(): Double {
        val insurance = CostAssumptions.FCEV_INSURANCE
        val roadTax = CostAssumptions.FCEV_ROAD_TAX
        val maintenance = CostAssumptions.FCEV_MAINTENANCE

        val fixedCosts = insurance + roadTax + maintenance
        return fixedCosts
    }
}