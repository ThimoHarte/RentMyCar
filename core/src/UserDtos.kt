package rentmycar.core

import kotlinx.serialization.Serializable

// Used to make a new account (data class: only holds data)
@Serializable
data class RegisterRequest(
    val name: String,
    val email: String,
    val password: String
)

@Serializable
data class LoginRequest(
    val email: String,
    val password: String
)

@Serializable
data class TokenResponse(
    val token: String
)

// A user as it is sent to the client: never with the password
@Serializable
data class UserResponse(
    val id: Int,
    val name: String,
    val email: String,
    val bonusPoints: Int
)
