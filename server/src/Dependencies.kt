package rentmycar.server

import rentmycar.server.cars.CarRepository
import rentmycar.server.cars.CarService
import rentmycar.server.cars.ExposedCarRepository
import rentmycar.server.reservations.ExposedReservationRepository
import rentmycar.server.reservations.ReservationRepository
import rentmycar.server.reservations.ReservationService
import rentmycar.server.users.ExposedUserRepository
import rentmycar.server.users.JwtConfig
import rentmycar.server.users.PasswordHasher
import rentmycar.server.users.TokenService
import rentmycar.server.users.UserRepository
import rentmycar.server.users.UserService

// All parts of the application, made once and passed to each other (no framework needed)
class Dependencies(
    val userRepository: UserRepository,
    val carRepository: CarRepository,
    val reservationRepository: ReservationRepository,
    val passwordHasher: PasswordHasher,
    val tokenService: TokenService,
    val userService: UserService,
    val carService: CarService,
    val reservationService: ReservationService
)

// Makes all repositories and services once and passes them to each other
fun createDependencies(databaseUrl: String, jwtConfig: JwtConfig): Dependencies {
    val database = DatabaseFactory.connect(databaseUrl)

    val userRepository = ExposedUserRepository(database)
    val carRepository = ExposedCarRepository(database, userRepository)
    val reservationRepository = ExposedReservationRepository(database, carRepository, userRepository)

    val passwordHasher = PasswordHasher()
    val tokenService = TokenService(jwtConfig)

    val userService = UserService(userRepository, passwordHasher, tokenService)
    val carService = CarService(carRepository, userRepository, reservationRepository)
    val reservationService = ReservationService(reservationRepository, carRepository, userRepository)

    return Dependencies(
        userRepository = userRepository,
        carRepository = carRepository,
        reservationRepository = reservationRepository,
        passwordHasher = passwordHasher,
        tokenService = tokenService,
        userService = userService,
        carService = carService,
        reservationService = reservationService
    )
}
