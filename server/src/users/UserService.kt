package rentmycar.server.users

import io.ktor.server.plugins.NotFoundException
import rentmycar.core.LoginRequest
import rentmycar.core.RegisterRequest
import rentmycar.server.common.ConflictException

// The use cases for users: register, log in and find a user
class UserService(
    private val userRepository: UserRepository,
    private val passwordHasher: PasswordHasher,
    private val tokenService: TokenService
) {

    // Registers a new user after checking the name, email address and password
    fun register(request: RegisterRequest): User {
        require(request.name.isNotBlank()) { "Name is required." }
        require(isValidEmail(request.email)) { "Email address is not valid." }
        require(request.password.length >= 8) { "Password must have at least 8 characters." }

        if (userRepository.findByEmail(request.email) != null) {
            throw ConflictException("This email address is already in use.")
        }

        val passwordHash = passwordHasher.hash(request.password)
        return userRepository.add(request.name, request.email, passwordHash)
    }

    // Logs in: returns a token, or null when the email address or the password is wrong
    fun login(request: LoginRequest): String? {
        // A wrong email address and a wrong password both give null, so nobody can find out which emails exist
        val user = userRepository.findByEmail(request.email)
        if (user == null) {
            return null
        }
        val passwordIsCorrect = passwordHasher.verify(request.password, user.passwordHash)
        if (!passwordIsCorrect) {
            return null
        }
        return tokenService.createToken(user.id)
    }

    // Returns the user, or gives a 404 when it does not exist
    fun findById(id: Int): User {
        val user = userRepository.findById(id)
        if (user == null) {
            throw NotFoundException("User not found.")
        }
        return user
    }

    // Checks if an email address looks valid, for example name@domain.nl
    private fun isValidEmail(email: String): Boolean {
        // Something like name@domain.nl: text, an @, a domain, a dot and at least 2 letters
        val emailPattern = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

        val isValid = email.matches(emailPattern)
        return isValid
    }
}
