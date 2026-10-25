package cars

import rentmycar.server.common.CostAssumptions
import rentmycar.server.users.User

// An electric car with a battery (BEV)
class ElectricCar(
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
    val kWhPer100Km: Double
) : Car(
    id, owner, brand, model, year, licensePlate, seats,
    hourlyRate, kmRate, purchasePrice, address, initiallyActive
) {
    init {
        require(kWhPer100Km > 0) { "Energy use must be more than 0." }
    }

    // Electricity costs per km (override: every kind of car calculates this in its own way)
    override fun costPerKm(): Double {
        val pricePerKWh = CostAssumptions.ELECTRICITY_PRICE
        val costPer100Km = kWhPer100Km * pricePerKWh

        val costPerKm = costPer100Km / 100
        return costPerKm
    }

    // Fixed costs per year of an electric car (override: every kind of car has its own values)
    override fun fixedCostsPerYear(): Double {
        val insurance = CostAssumptions.BEV_INSURANCE
        val roadTax = CostAssumptions.BEV_ROAD_TAX
        val maintenance = CostAssumptions.BEV_MAINTENANCE

        val fixedCosts = insurance + roadTax + maintenance
        return fixedCosts
    }
}