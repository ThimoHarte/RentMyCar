package rentmycar.server.cars

import rentmycar.server.common.CostAssumptions
import rentmycar.server.users.User

// An electric car that runs on hydrogen
class hydrogenCar(
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
    val kgPer100Km: Double,
) : Car(
    id, owner, brand, model, year, licensePlate, seats,
    hourlyRate, kmRate, purchasePrice, address, photos
) {
    init {
        require(kgPer100Km > 0) { "Fuel use must be more than 0." }
    }

    override fun costPerKm(): Double =
        kgPer100Km * CostAssumptions.HYDROGEN_PRICE / 100

    override fun fixedCostsPerYear(): Double =
        CostAssumptions.FCEV_INSURANCE + CostAssumptions.FCEV_ROAD_TAX + CostAssumptions.FCEV_MAINTENANCE
}