package rentmycar.server.common

// The user is logged in, but is not allowed to do this (403)
class ForbiddenException(message: String) : Exception(message)

// The action is not possible right now, for example a double reservation (409)
class ConflictException(message: String) : Exception(message)
