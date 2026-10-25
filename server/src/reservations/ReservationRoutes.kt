package rentmycar.server.reservations

import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.patch
import io.ktor.server.routing.post
import rentmycar.core.ChangeStatusRequest
import rentmycar.core.CreateReservationRequest
import rentmycar.core.ReservationResponse
import rentmycar.server.common.currentTime
import rentmycar.server.common.intParameter
import rentmycar.server.common.userId

// The routes for reservations, all need a login (extension: extends Route with a function, "this" is the /api route)
fun Route.reservationRoutes(reservationService: ReservationService) {
    authenticate("auth-jwt") {

        // Request a reservation, for example POST /api/reservations
        post("/reservations") {
            val request = call.receive<CreateReservationRequest>()
            val userId = call.userId()
            val reservation = reservationService.requestReservation(request, userId, currentTime())
            call.respond(HttpStatusCode.Created, reservation.toResponse())
        }

        // Get my own reservations, for example /api/reservations/mine
        get("/reservations/mine") {
            val userId = call.userId()
            val reservations = reservationService.getMyReservations(userId)
            // Turn every reservation into the data that is sent to the client
            val responses = mutableListOf<ReservationResponse>()
            for (reservation in reservations) {
                responses.add(reservation.toResponse())
            }
            call.respond(responses)
        }

        // Get one reservation, for example /api/reservations/1
        get("/reservations/{id}") {
            val id = call.intParameter("id")
            val userId = call.userId()
            val reservation = reservationService.getReservation(id, userId)
            call.respond(reservation.toResponse())
        }

        // Approve, reject or cancel, for example PATCH /api/reservations/1/status with { "status": "APPROVED" }
        patch("/reservations/{id}/status") {
            val id = call.intParameter("id")
            val request = call.receive<ChangeStatusRequest>()
            val userId = call.userId()
            val reservation = reservationService.changeStatus(id, request.status, userId, currentTime())
            call.respond(reservation.toResponse())
        }

        // The requests and reservations for one of my own cars, for example /api/cars/1/reservations
        get("/cars/{id}/reservations") {
            val carId = call.intParameter("id")
            val userId = call.userId()
            val reservations = reservationService.getReservationsForCar(carId, userId)
            // Turn every reservation into the data that is sent to the client
            val responses = mutableListOf<ReservationResponse>()
            for (reservation in reservations) {
                responses.add(reservation.toResponse())
            }
            call.respond(responses)
        }
    }
}
