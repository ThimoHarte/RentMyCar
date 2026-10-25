package rentmycar.server.common

import kotlin.time.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

// The current date and time in the time zone of the server
fun currentTime(): LocalDateTime {
    val now = Clock.System.now()
    return now.toLocalDateTime(TimeZone.currentSystemDefault())
}

// The number of hours between two moments, for example 2.5
fun hoursBetween(start: LocalDateTime, end: LocalDateTime): Double {
    // Turn both into an exact moment (Instant) to subtract them; UTC, so summer/winter time does not count
    val startMoment = start.toInstant(TimeZone.UTC)
    val endMoment = end.toInstant(TimeZone.UTC)

    val duration = endMoment - startMoment
    val minutes = duration.inWholeMinutes
    val hours = minutes / 60.0
    return hours
}
