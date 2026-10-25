package rentmycar.server.cars

import org.jetbrains.exposed.v1.core.dao.id.IntIdTable
import rentmycar.core.CarType
import rentmycar.core.FuelType
import rentmycar.server.users.UserTable

// The table "cars"; the column "type" tells the kind of car (object: Kotlin makes exactly one, used via its name)
object CarTable : IntIdTable("cars") {
    val owner = reference("owner_id", UserTable)
    val type = enumerationByName<CarType>("type", 10)
    val brand = varchar("brand", 50)
    val model = varchar("model", 50)
    val year = integer("year")
    val licensePlate = varchar("license_plate", 10).uniqueIndex()
    val seats = integer("seats")
    val hourlyRate = double("hourly_rate")
    val kmRate = double("km_rate")
    val purchasePrice = double("purchase_price")
    val street = varchar("street", 100)
    val houseNumber = varchar("house_number", 10)
    val postalCode = varchar("postal_code", 10)
    val city = varchar("city", 100)
    val isActive = bool("is_active").default(true)

    // Only for ICE
    val fuelType = enumerationByName<FuelType>("fuel_type", 10).nullable()
    val litersPer100Km = double("liters_per_100km").nullable()

    // Only for BEV
    val kWhPer100Km = double("kwh_per_100km").nullable()

    // Only for FCEV
    val kgPer100Km = double("kg_per_100km").nullable()
}
