package rentmycar.server

import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.ratelimit.RateLimit
import io.ktor.server.plugins.ratelimit.RateLimitName
import io.ktor.server.response.respond
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import kotlin.time.Duration.Companion.minutes
import kotlinx.serialization.json.Json
import rentmycar.core.ApiError
import rentmycar.server.cars.carRoutes
import rentmycar.server.common.configureStatusPages
import rentmycar.server.reservations.reservationRoutes
import rentmycar.server.users.userRoutes

// Against guessing passwords: at most this many login or register attempts per minute
private const val MAX_ATTEMPTS_PER_MINUTE = 10

// Installs the plugins and the routes, the tests use it too (extension: extends Application with a function)
fun Application.module(dependencies: Dependencies) {
    // JSON in and out
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            ignoreUnknownKeys = true
        })
    }

    // Exceptions -> status codes
    configureStatusPages()

    // At most 10 login or register attempts per minute
    install(RateLimit) {
        register(RateLimitName("credentials")) {
            rateLimiter(limit = MAX_ATTEMPTS_PER_MINUTE, refillPeriod = 1.minutes)
        }
    }

    // Logging in with a JWT token
    install(Authentication) {
        jwt("auth-jwt") {
            realm = dependencies.tokenService.realm
            verifier(dependencies.tokenService.verifier)
            // The token must contain the id of the user ("subject"); null means: token not accepted
            validate { credential ->
                // The last value in this lambda is the result (a return is not allowed here)
                if (credential.payload.subject == null) {
                    null
                } else {
                    JWTPrincipal(credential.payload)
                }
            }
            // The answer when the token is missing or wrong (_ means: this value is not used)
            challenge { _, _ ->
                call.respond(
                    HttpStatusCode.Unauthorized,
                    ApiError("Log in first: the token is missing, expired or invalid.")
                )
            }
        }
    }

    routing {
        route("/api") {
            userRoutes(dependencies.userService)
            carRoutes(dependencies.carService)
            reservationRoutes(dependencies.reservationService)
        }
    }
}
