package rentmycar.server.cars

import rentmycar.server.common.CostAssumptions
import rentmycar.server.users.User

// An electric car with a battery
class electricCar(
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
    val kWhPer100Km: Double,
) : Car(
    id, owner, brand, model, year, licensePlate, seats,
    hourlyRate, kmRate, purchasePrice, address, photos
) {
    init {
        require(kWhPer100Km > 0) { "Energy use must be more than 0." }
    }

    override fun costPerKm(): Double =
        kWhPer100Km * CostAssumptions.ELECTRICITY_PRICE / 100

    override fun fixedCostsPerYear(): Double =
        CostAssumptions.BEV_INSURANCE + CostAssumptions.BEV_ROAD_TAX + CostAssumptions.BEV_MAINTENANCE
}