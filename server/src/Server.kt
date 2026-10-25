package rentmycar.server

import io.ktor.server.config.ApplicationConfig
import io.ktor.server.config.getAs
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import kotlinx.serialization.Serializable
import rentmycar.server.users.JwtConfig

// The port and address of the server, from application.conf (data class: only holds data)
@Serializable
data class ServerConfig(
    val port: Int = 8080,
    val host: String = "0.0.0.0"
)

// All settings from application.conf
@Serializable
data class AppConfig(
    val server: ServerConfig = ServerConfig(),
    val jwt: JwtConfig
)

// Starts the server with the settings from application.conf
fun main() {
    val appConfig = ApplicationConfig("application.conf").getAs<AppConfig>()

    // Without JWT_SECRET the secret from application.conf is used, which is only safe on your own computer
    if (System.getenv("JWT_SECRET") == null) {
        println("Warning: JWT_SECRET is not set, the development secret from application.conf is used.")
    }

    val dependencies = createDependencies(DatabaseFactory.FILE_URL, appConfig.jwt)
    addDemoDataWhenEmpty(dependencies)

    embeddedServer(Netty, port = appConfig.server.port, host = appConfig.server.host) {
        module(dependencies)
    }.start(wait = true)
}
