package rentmycar.core

import kotlinx.serialization.Serializable

// The body of every error, for example { "message": "Car not found." } (data class: only holds data)
@Serializable
data class ApiError(
    val message: String
)
