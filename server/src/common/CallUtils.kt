package rentmycar.server.common

import io.ktor.server.application.ApplicationCall
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.plugins.BadRequestException

// The id of the logged-in user, from the token (extension: extends ApplicationCall with a function, "this" is the call)
fun ApplicationCall.userId(): Int {
    val principal = principal<JWTPrincipal>()
    if (principal == null) {
        throw IllegalStateException("No logged-in user.")
    }

    // The id of the user is stored in the "subject" of the token, as text
    val userIdText = principal.payload.subject
    val userId = userIdText.toInt()
    return userId
}

// A number from the URL, like the 5 in /api/cars/5 (extension: extends ApplicationCall with a function, "this" is the call)
fun ApplicationCall.intParameter(name: String): Int {
    val text = parameters[name]
    if (text == null) {
        throw BadRequestException("'$name' is missing.")
    }
    // toIntOrNull() gives null when the text is not a number
    val number = text.toIntOrNull()
    if (number == null) {
        throw BadRequestException("'$name' must be a number.")
    }
    return number
}
