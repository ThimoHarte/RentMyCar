package rentmycar.server.users

import rentmycar.core.UserResponse

// Turns a user into the data for the client, without the password (extension: extends User with a function, "this" is the user)
fun User.toResponse(): UserResponse {
    return UserResponse(
        id = id,
        name = name,
        email = email,
        bonusPoints = bonusPoints
    )
}
