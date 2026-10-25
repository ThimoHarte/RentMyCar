package rentmycar.server.cars

import rentmycar.core.FuelType
import rentmycar.server.common.CostAssumptions
import rentmycar.server.users.User

// A car with a combustion engine (petrol, diesel or LPG)
class IceCar(
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
    val fuelType: FuelType,
    val litersPer100Km: Double
) : Car(
    id, owner, brand, model, year, licensePlate, seats,
    hourlyRate, kmRate, purchasePrice, address, initiallyActive
) {
    init {
        require(litersPer100Km > 0) { "Fuel use must be more than 0." }
    }

    // Fuel costs per km (override: every kind of car calculates this in its own way)
    override fun costPerKm(): Double {
        val pricePerLiter = CostAssumptions.fuelPrice(fuelType)
        val costPer100Km = litersPer100Km * pricePerLiter

        val costPerKm = costPer100Km / 100
        return costPerKm
    }

    // Fixed costs per year of a car with a combustion engine (override: every kind of car has its own values)
    override fun fixedCostsPerYear(): Double {
        val insurance = CostAssumptions.ICE_INSURANCE
        val roadTax = CostAssumptions.ICE_ROAD_TAX
        val maintenance = CostAssumptions.ICE_MAINTENANCE

        val fixedCosts = insurance + roadTax + maintenance
        return fixedCosts
    }
}
