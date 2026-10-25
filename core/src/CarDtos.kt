package rentmycar.core

import kotlinx.serialization.Serializable

// The address as it is sent (data class: only holds data)
@Serializable
data class AddressDto(
    val street: String,
    val houseNumber: String,
    val postalCode: String,
    val city: String
)

// Used to add or change a car; only the consumption fields of its type have to be filled in
@Serializable
data class CarRequest(
    val type: CarType,
    val brand: String,
    val model: String,
    val year: Int,
    val licensePlate: String,
    val seats: Int,
    val hourlyRate: Double,
    val kmRate: Double,
    val purchasePrice: Double,
    val address: AddressDto,
    val fuelType: FuelType? = null,
    val litersPer100Km: Double? = null,
    val kWhPer100Km: Double? = null,
    val kgPer100Km: Double? = null
)

@Serializable
data class CarResponse(
    val id: Int,
    val type: CarType,
    val ownerId: Int,
    val brand: String,
    val model: String,
    val year: Int,
    val licensePlate: String,
    val seats: Int,
    val hourlyRate: Double,
    val kmRate: Double,
    val purchasePrice: Double,
    val address: AddressDto,
    val isActive: Boolean,
    val fuelType: FuelType? = null,
    val litersPer100Km: Double? = null,
    val kWhPer100Km: Double? = null,
    val kgPer100Km: Double? = null
)

@Serializable
data class CostsResponse(
    val costPerKm: Double,
    val tcoPerYear: Double,
    val kmPerYear: Int
)
