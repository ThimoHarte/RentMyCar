package rentmycar.server.users

import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.plugins.ratelimit.RateLimitName
import io.ktor.server.plugins.ratelimit.rateLimit
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import rentmycar.core.ApiError
import rentmycar.core.LoginRequest
import rentmycar.core.RegisterRequest
import rentmycar.core.TokenResponse
import rentmycar.server.common.userId

// The routes under /api/users (extension: extends Route with a function, "this" is the /api route)
fun Route.userRoutes(userService: UserService) {

    // At most 10 attempts per minute, against guessing passwords
    rateLimit(RateLimitName("credentials")) {
        // Create an account, for example POST /api/users/register
        post("/users/register") {
            val request = call.receive<RegisterRequest>()
            val user = userService.register(request)
            call.respond(HttpStatusCode.Created, user.toResponse())
        }

        // Log in and get a token, for example POST /api/users/login
        post("/users/login") {
            val request = call.receive<LoginRequest>()
            val token = userService.login(request)
            if (token == null) {
                call.respond(HttpStatusCode.Unauthorized, ApiError("Invalid email or password."))
            } else {
                call.respond(TokenResponse(token))
            }
        }
    }

    authenticate("auth-jwt") {
        // Get my own account, for example /api/users/me
        get("/users/me") {
            val userId = call.userId()
            val user = userService.findById(userId)
            call.respond(user.toResponse())
        }
    }
}
