package rentmycar.server

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.testing.testApplication
import kotlin.test.Test
import kotlin.test.assertEquals
import rentmycar.core.AddressDto
import rentmycar.core.CarRequest
import rentmycar.core.CarResponse
import rentmycar.core.CarType
import rentmycar.core.FuelType
import rentmycar.core.LoginRequest
import rentmycar.core.RegisterRequest
import rentmycar.core.TokenResponse

// Integration tests (IT-03, IT-04): real routes, plugins, services and H2 in memory; each test: Arrange, Act, Assert
class ApiTest {

    // Starts the application with a new, empty database and gives a JSON client to the test
    private fun apiTest(test: suspend (HttpClient) -> Unit) {
        testApplication {
            val databaseUrl = DatabaseFactory.inMemoryUrl("api-test-${System.nanoTime()}")
            val dependencies = createDependencies(databaseUrl, TEST_JWT_CONFIG)
            application {
                module(dependencies)
            }

            val client = createClient {
                install(ContentNegotiation) {
                    json()
                }
            }
            test(client)
        }
    }

    // The data of a Volkswagen Golf, as the client sends it
    private fun golfRequest(): CarRequest {
        val address = AddressDto(
            street = "Hogeschoollaan",
            houseNumber = "1",
            postalCode = "4818 CR",
            city = "Breda"
        )
        return CarRequest(
            type = CarType.ICE,
            brand = "Volkswagen",
            model = "Golf",
            year = 2019,
            licensePlate = "AB-123-C",
            seats = 5,
            hourlyRate = 6.0,
            kmRate = 0.25,
            purchasePrice = 30000.0,
            address = address,
            fuelType = FuelType.PETROL,
            litersPer100Km = 6.0
        )
    }

    // IT-03: the four kinds of CRUD requests on a car work
    @Test
    fun `create, read, update and delete a car`() {
        apiTest { client ->
            // Arrange: register and log in
            client.post("/api/users/register") {
                contentType(ContentType.Application.Json)
                setBody(RegisterRequest(name = "Eva", email = "eva@example.com", password = "Welkom123!"))
            }
            val loginResponse = client.post("/api/users/login") {
                contentType(ContentType.Application.Json)
                setBody(LoginRequest(email = "eva@example.com", password = "Welkom123!"))
            }
            val token = loginResponse.body<TokenResponse>().token

            // Act and Assert 1 - Create
            val createResponse = client.post("/api/cars") {
                bearerAuth(token)
                contentType(ContentType.Application.Json)
                setBody(golfRequest())
            }
            assertEquals(HttpStatusCode.Created, createResponse.status)
            val car = createResponse.body<CarResponse>()

            // Act and Assert 2 - Read
            val getResponse = client.get("/api/cars/${car.id}")
            assertEquals(HttpStatusCode.OK, getResponse.status)
            val foundCar = getResponse.body<CarResponse>()
            assertEquals("Golf", foundCar.model)

            // Act and Assert 3 - Update: a higher hourly rate
            val updateResponse = client.put("/api/cars/${car.id}") {
                bearerAuth(token)
                contentType(ContentType.Application.Json)
                setBody(golfRequest().copy(hourlyRate = 7.5))
            }
            assertEquals(HttpStatusCode.OK, updateResponse.status)
            val updatedCar = updateResponse.body<CarResponse>()
            assertEquals(7.5, updatedCar.hourlyRate)

            // Act and Assert 4 - Delete: the car becomes inactive and is not found anymore
            val deleteResponse = client.delete("/api/cars/${car.id}") {
                bearerAuth(token)
            }
            assertEquals(HttpStatusCode.NoContent, deleteResponse.status)
            val getAfterDeleteResponse = client.get("/api/cars/${car.id}")
            assertEquals(HttpStatusCode.NotFound, getAfterDeleteResponse.status)
        }
    }

    // IT-04: adding a car without logging in is refused
    @Test
    fun `adding a car without a token gives 401`() {
        apiTest { client ->
            // Arrange: nothing to prepare, there is no token

            // Act: add a car without bearerAuth(...)
            val response = client.post("/api/cars") {
                contentType(ContentType.Application.Json)
                setBody(golfRequest())
            }

            // Assert
            assertEquals(HttpStatusCode.Unauthorized, response.status)
        }
    }
}
