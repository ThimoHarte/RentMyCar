package rentmycar.server.users

// A person who can offer cars and rent cars
class User(
    val id: Int,
    var name: String,
    var email: String,
    var passwordHash : String,
    var bonusPoints : Int
)
