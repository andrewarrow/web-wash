package team.higher.web.utility

import org.ocpsoft.prettytime.PrettyTime
import java.time.OffsetDateTime
import java.util.*

data class DateTimeHuman(
  val dateString: String = "",
  val timeString: String = "",
  val humanString: String = ""
)

val prettyTime: PrettyTime by lazy { PrettyTime() }

fun OffsetDateTime?.toDateTimeHuman(): DateTimeHuman {
  return DateTimeHuman(this.dateString(), this.timeString(), this.humanString())
}

/**
 * Convert to date string
 *
 * Eg:
 * 2017-07-19T15:55:34.570856Z
 * 2017-07-18T04:40:39.752025Z[Etc/UTC]
 */
fun OffsetDateTime?.dateString(): String {
  if (this == null) {
    return ""
  }
  val s = toString()
  if (s.length < 10) {
    return ""
  }
  return s.substring(0, 10)
}

/**
 * Convert to time string
 *
 * Eg:
 * 15:55:34.570856Z
 * 04:40:39.752025Z[Etc/UTC]
 */
fun OffsetDateTime?.timeString(): String {
  if (this == null) {
    return ""
  }
  val s = toString()
  if (s.length < 10) {
    return ""
  }
  return s.substring(11, 11 + 8)
}

fun OffsetDateTime?.humanString(): String {
  if (this == null) {
    return ""
  }
  return prettyTime.format(Date(toInstant().toEpochMilli()))
}
