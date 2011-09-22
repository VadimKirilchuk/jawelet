import ch.qos.logback.classic.encoder.*
import ch.qos.logback.core.ConsoleAppender

import static ch.qos.logback.classic.Level.*

debug = "true"

appender("STDOUT", ConsoleAppender) {
  encoder(PatternLayoutEncoder) {
	pattern = "%d{HH:mm:ss} %-5level %logger{36} - %msg%n"
  }
}

root(DEBUG, ["STDOUT"])
logger("ru.ifmo", INFO)

