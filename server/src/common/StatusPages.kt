package rentmycar.server.common

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.application.log
import io.ktor.server.plugins.BadRequestException
import io.ktor.server.plugins.NotFoundException
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond
import rentmycar.core.ApiError

// Turns exceptions into a status code with a clear message (extension: extends Application with a function)
fun Application.configureStatusPages() {
    install(StatusPages) {
        // 400: wrong input, for example a negative rate or invalid JSON
        exception<BadRequestException> { call, cause ->
            val message = messageOf(cause, "Invalid request.")
            call.respond(HttpStatusCode.BadRequest, ApiError(message))
        }
        exception<IllegalArgumentException> { call, cause ->
            val message = messageOf(cause, "Invalid request.")
            call.respond(HttpStatusCode.BadRequest, ApiError(message))
        }

        // 403: logged in, but not allowed
        exception<ForbiddenException> { call, cause ->
            val message = messageOf(cause, "Not allowed.")
            call.respond(HttpStatusCode.Forbidden, ApiError(message))
        }

        // 404: does not exist
        exception<NotFoundException> { call, cause ->
            val message = messageOf(cause, "Not found.")
            call.respond(HttpStatusCode.NotFound, ApiError(message))
        }

        // 409: not possible right now, for example overlap or a wrong status
        exception<ConflictException> { call, cause ->
            val message = messageOf(cause, "Conflict.")
            call.respond(HttpStatusCode.Conflict, ApiError(message))
        }
        exception<IllegalStateException> { call, cause ->
            val message = messageOf(cause, "Conflict.")
            call.respond(HttpStatusCode.Conflict, ApiError(message))
        }

        // 500: something we did not expect. The details go to the log, not to the client.
        exception<Throwable> { call, cause ->
            call.application.log.error("Unexpected error", cause)
            call.respond(HttpStatusCode.InternalServerError, ApiError("Something went wrong on the server."))
        }

        // 429: too many login attempts (from the RateLimit plugin)
        status(HttpStatusCode.TooManyRequests) { call, status ->
            call.respond(status, ApiError("Too many attempts. Try again in a minute."))
        }
    }
}

// The message of the exception, or the default message when the exception has none
private fun messageOf(cause: Throwable, defaultMessage: String): String {
    val message = cause.message
    if (message == null) {
        return defaultMessage
    }
    return message
}
