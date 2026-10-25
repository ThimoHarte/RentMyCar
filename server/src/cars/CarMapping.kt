package rentmycar.server.cars

import rentmycar.core.AddressDto
import rentmycar.core.CarResponse
import rentmycar.core.CarType
import rentmycar.core.FuelType

// The kind of a car as a CarType (extension: extends Car with a function, "this" is the car)
fun Car.type(): CarType {
    val type = when (this) {
        is IceCar -> CarType.ICE
        is ElectricCar -> CarType.BEV
        is HydrogenCar -> CarType.FCEV
    }
    return type
}

// Turns a car into the data for the client (extension: extends Car with a function, "this" is the car)
fun Car.toResponse(): CarResponse {
    val addressDto = AddressDto(
        street = address.street,
        houseNumber = address.houseNumber,
        postalCode = address.postalCode,
        city = address.city
    )

    // Only the consumption of this kind of car is filled in, the others stay null
    var fuelType: FuelType? = null
    var litersPer100Km: Double? = null
    var kWhPer100Km: Double? = null
    var kgPer100Km: Double? = null

    // After "this is IceCar" Kotlin knows it is an IceCar, so fuelType can be used (smart cast)
    if (this is IceCar) {
        fuelType = this.fuelType
        litersPer100Km = this.litersPer100Km
    }
    if (this is ElectricCar) {
        kWhPer100Km = this.kWhPer100Km
    }
    if (this is HydrogenCar) {
        kgPer100Km = this.kgPer100Km
    }

    return CarResponse(
        id = id,
        type = type(),
        ownerId = owner.id,
        brand = brand,
        model = model,
        year = year,
        licensePlate = licensePlate,
        seats = seats,
        hourlyRate = hourlyRate,
        kmRate = kmRate,
        purchasePrice = purchasePrice,
        address = addressDto,
        isActive = isActive,
        fuelType = fuelType,
        litersPer100Km = litersPer100Km,
        kWhPer100Km = kWhPer100Km,
        kgPer100Km = kgPer100Km
    )
}
