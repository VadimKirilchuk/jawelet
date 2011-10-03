import ch.qos.logback.classic.encoder.*
import ch.qos.logback.core.ConsoleAppender

import static ch.qos.logback.classic.Level.*

appender("STDOUT", ConsoleAppender) {
  encoder(PatternLayoutEncoder) {
	pattern = "%-2level %logger{36} - %msg%n"
  }
}

root(INFO, ["STDOUT"])
logger("ru.ifmo.diplom.kirilchuk.jawelet", DEBUG)

