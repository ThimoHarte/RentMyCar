package rentmycar.server.users

import java.security.MessageDigest
import java.security.SecureRandom
import kotlin.io.encoding.Base64
import org.bouncycastle.crypto.generators.Argon2BytesGenerator
import org.bouncycastle.crypto.params.Argon2Parameters

// Settings for Argon2id. They are the same as in the demo of the guest lecturer.
private const val SALT_LENGTH_IN_BYTES = 16
private const val HASH_LENGTH_IN_BYTES = 32
private const val MEMORY_IN_KB = 65_536      // 64 MB
private const val ITERATIONS = 3             // how many times the calculation is repeated
private const val PARALLEL_THREADS = 4

// Makes a safe hash of a password with Argon2id, and checks a password against a hash.
class PasswordHasher {

    // Makes a hash of a new password; returns "salt:hash", both as Base64 text
    fun hash(password: String): String {
        // A random salt, so two equal passwords still get a different hash
        val salt = ByteArray(SALT_LENGTH_IN_BYTES)
        SecureRandom().nextBytes(salt)
        val hash = calculateHash(password, salt)

        // Bytes cannot be stored as text, so turn them into Base64 text first
        val saltText = Base64.encode(salt)
        val hashText = Base64.encode(hash)

        val storedHash = saltText + ":" + hashText
        return storedHash
    }

    // Checks if a password belongs to a stored hash
    fun verify(password: String, storedHash: String): Boolean {
        val parts = storedHash.split(":")
        if (parts.size != 2) {
            return false
        }

        val salt = Base64.decode(parts[0])
        val expectedHash = Base64.decode(parts[1])
        val actualHash = calculateHash(password, salt)

        // Compare in constant time, so the time does not give anything away
        val isSamePassword = MessageDigest.isEqual(expectedHash, actualHash)
        return isSamePassword
    }

    // Calculates the Argon2id hash of a password with a salt
    private fun calculateHash(password: String, salt: ByteArray): ByteArray {
        // The builder collects all settings for Argon2id
        val builder = Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
        builder.withVersion(Argon2Parameters.ARGON2_VERSION_13)
        builder.withSalt(salt)
        builder.withMemoryAsKB(MEMORY_IN_KB)
        builder.withIterations(ITERATIONS)
        builder.withParallelism(PARALLEL_THREADS)
        val parameters = builder.build()

        val generator = Argon2BytesGenerator()
        generator.init(parameters)

        val hash = ByteArray(HASH_LENGTH_IN_BYTES)
        generator.generateBytes(password.toCharArray(), hash)
        return hash
    }
}
