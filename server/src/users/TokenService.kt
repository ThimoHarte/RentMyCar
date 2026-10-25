package rentmycar.server.users

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTVerificationException
import java.util.Date
import kotlinx.serialization.Serializable

// The settings for the tokens, from application.conf (data class: only holds data)
@Serializable
data class JwtConfig(
    val secret: String,
    val issuer: String,
    val audience: String,
    val realm: String
) {
    init {
        require(secret.length >= 32) { "The JWT secret must have at least 32 characters." }
    }
}

// Makes and checks the JWT tokens that a user gets after logging in
class TokenService(private val jwtConfig: JwtConfig) {

    private val algorithm = Algorithm.HMAC256(jwtConfig.secret)

    // The name of the protected part of the API, used when logging in with a token
    val realm: String = jwtConfig.realm

    // Checks the signature, the issuer, the audience and the expiry time
    val verifier: JWTVerifier = createVerifier()

    // Makes the verifier that checks every token
    private fun createVerifier(): JWTVerifier {
        // The builder collects what must be checked in every token
        val builder = JWT.require(algorithm)
        builder.withIssuer(jwtConfig.issuer)
        builder.withAudience(jwtConfig.audience)
        builder.withClaimPresence("sub")    // the id of the user must be in the token
        builder.withClaimPresence("exp")    // the expiry time must be in the token

        val verifier = builder.build()
        return verifier
    }

    // Makes a token for a user that is valid for 15 minutes
    fun createToken(userId: Int, now: Date = Date()): String {
        // Date works with milliseconds: 1 minute = 60 seconds x 1000 milliseconds
        val validMilliseconds = VALID_MINUTES * 60 * 1000
        val expiresAt = Date(now.time + validMilliseconds)

        // The builder collects all data of the token
        val builder = JWT.create()
        builder.withIssuer(jwtConfig.issuer)
        builder.withAudience(jwtConfig.audience)
        builder.withSubject(userId.toString())    // the id of the user
        builder.withIssuedAt(now)
        builder.withExpiresAt(expiresAt)

        // Sign with the secret, so nobody can change the token without it being noticed
        val token = builder.sign(algorithm)
        return token
    }

    // Checks if a token is valid (signature, issuer, audience and expiry time)
    fun isValid(token: String): Boolean {
        // verify() throws an exception when the token is not valid
        try {
            verifier.verify(token)
            return true
        } catch (e: JWTVerificationException) {
            return false
        }
    }

    // Belongs to the class, the same for every token (companion object: used as TokenService.VALID_MINUTES)
    companion object {
        const val VALID_MINUTES = 15
    }
}
