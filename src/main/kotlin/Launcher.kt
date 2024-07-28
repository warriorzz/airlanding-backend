package ee.bjarn.airlanding

import org.slf4j.LoggerFactory
import java.io.File

private val logger = LoggerFactory.getLogger("AirLanding")
val watchDirectory: String = System.getenv("AIRLANDING_FILE_WATCH")
val serveDirectory: String = System.getenv("AIRLANDING_FILE_SERVE").let { 
    if (!it.endsWith("/")) "$it/" else it
}

suspend fun main() {
    logger.info("Starting...")
    File(serveDirectory).deleteFiles()
    File(watchDirectory).deleteFiles()
    logger.info("Deleted old files.")
    startFileWatcher(logger)
    logger.info("Started FileWatcher.")
    server(logger).start(true)
}

fun File.deleteFiles() {
    if (this.isDirectory) listFiles()?.forEach { it.deleteFiles() }
    else this.delete()
}