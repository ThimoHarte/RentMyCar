package rentmycar.server.cars

import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.put
import io.ktor.server.routing.route
import rentmycar.core.CarRequest
import rentmycar.core.CarResponse
import rentmycar.core.CarType
import rentmycar.server.common.currentTime
import rentmycar.server.common.intParameter
import rentmycar.server.common.userId

// The routes under /api/cars (extension: extends Route with a function, "this" is the /api route)
fun Route.carRoutes(carService: CarService) {
    route("/cars") {

        // Search cars, for example /api/cars?type=BEV&city=Breda&maxHourlyRate=10
        get {
            val parameters = call.request.queryParameters

            // Every filter is optional, so each one can be null
            val typeText = parameters["type"]
            var type: CarType? = null
            if (typeText != null) {
                type = CarType.valueOf(typeText.uppercase())
            }

            val city = parameters["city"]

            val maxHourlyRateText = parameters["maxHourlyRate"]
            var maxHourlyRate: Double? = null
            if (maxHourlyRateText != null) {
                maxHourlyRate = maxHourlyRateText.toDouble()
            }

            val cars = carService.search(type, city, maxHourlyRate)
            // Turn every car into the data that is sent to the client
            val responses = mutableListOf<CarResponse>()
            for (car in cars) {
                responses.add(car.toResponse())
            }
            call.respond(responses)
        }

        // Get one car, for example /api/cars/1
        get("/{id}") {
            val id = call.intParameter("id")
            val car = carService.getActiveCar(id)
            call.respond(car.toResponse())
        }

        authenticate("auth-jwt") {
            // Get my own cars, for example /api/cars/mine
            get("/mine") {
                val userId = call.userId()
                val cars = carService.getCarsOfOwner(userId)
                // Turn every car into the data that is sent to the client
                val responses = mutableListOf<CarResponse>()
                for (car in cars) {
                    responses.add(car.toResponse())
                }
                call.respond(responses)
            }

            // Add a car, for example POST /api/cars
            post {
                val request = call.receive<CarRequest>()
                val userId = call.userId()
                val car = carService.createCar(request, userId)
                call.respond(HttpStatusCode.Created, car.toResponse())
            }

            // Change my car, for example PUT /api/cars/1
            put("/{id}") {
                val id = call.intParameter("id")
                val request = call.receive<CarRequest>()
                val userId = call.userId()
                val car = carService.updateCar(id, request, userId)
                call.respond(car.toResponse())
            }

            // Remove my car, for example DELETE /api/cars/1
            delete("/{id}") {
                val id = call.intParameter("id")
                val userId = call.userId()
                carService.deleteCar(id, userId, currentTime())
                call.respond(HttpStatusCode.NoContent)
            }

            // Get the costs of my car, for example /api/cars/1/costs
            get("/{id}/costs") {
                val id = call.intParameter("id")
                val userId = call.userId()
                val costs = carService.getCosts(id, userId)
                call.respond(costs)
            }
        }
    }
}
