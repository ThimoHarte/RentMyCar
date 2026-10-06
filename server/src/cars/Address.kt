package rentmycar.server.cars

// The place where a car is parked
data class Address(
    val street: String,
    val houseNumber: String,
    val postalCode: String,
    val city: String
)