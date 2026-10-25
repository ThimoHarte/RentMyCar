package rentmycar.server.common

import rentmycar.core.FuelType

// Fixed values for the costs, see docs/aannames.md (object: Kotlin makes exactly one, used via its name)
object CostAssumptions {
    const val KM_PER_YEAR = 15_000
    const val DEPRECIATION_RATE = 0.15

    // Energy prices in euro
    const val PETROL_PRICE = 2.42       // per liter
    const val DIESEL_PRICE = 2.50       // per liter
    const val LPG_PRICE = 0.99          // per liter
    const val ELECTRICITY_PRICE = 0.34  // per kWh
    const val HYDROGEN_PRICE = 21.0     // per kg

    // Fixed costs per year for a car with a combustion engine (ICE)
    const val ICE_INSURANCE = 800.0
    const val ICE_ROAD_TAX = 1000.0
    const val ICE_MAINTENANCE = 650.0

    // Fixed costs per year for an electric car (BEV)
    const val BEV_INSURANCE = 1100.0
    const val BEV_ROAD_TAX = 710.0
    const val BEV_MAINTENANCE = 385.0

    // Fixed costs per year for a hydrogen car (FCEV)
    const val FCEV_INSURANCE = 1100.0
    const val FCEV_ROAD_TAX = 710.0
    const val FCEV_MAINTENANCE = 600.0

    // Returns the price per liter for a kind of fuel
    fun fuelPrice(fuelType: FuelType): Double {
        val price = when (fuelType) {
            FuelType.PETROL -> PETROL_PRICE
            FuelType.DIESEL -> DIESEL_PRICE
            FuelType.LPG -> LPG_PRICE
        }
        return price
    }
}
