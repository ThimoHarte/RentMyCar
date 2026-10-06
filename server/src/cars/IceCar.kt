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
    photos: List<Photo> = emptyList(),
    val fuelType: FuelType,
    val litersPer100Km: Double
) : Car(
    id, owner, brand, model, year, licensePlate, seats,
    hourlyRate, kmRate, purchasePrice, address, photos
) {
    init {
        require(litersPer100Km > 0) { "Fuel use must be more than 0." }
    }

    override fun costPerKm(): Double =
        litersPer100Km * CostAssumptions.fuelPrice(fuelType) / 100

    override fun fixedCostsPerYear(): Double =
        CostAssumptions.ICE_INSURANCE + CostAssumptions.ICE_ROAD_TAX + CostAssumptions.ICE_MAINTENANCE
}
