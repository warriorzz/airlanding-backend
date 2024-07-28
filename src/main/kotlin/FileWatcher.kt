package ee.bjarn.airlanding

import dev.vishna.watchservice.KWatchChannel
import dev.vishna.watchservice.KWatchEvent
import dev.vishna.watchservice.asWatchChannel
import io.ktor.util.logging.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.consumeEach
import java.io.File
import java.util.*

suspend fun startFileWatcher(logger: Logger) {
    val directory = File(watchDirectory)
    val coroutineScope = CoroutineScope(Dispatchers.Default) + Job()

    val watchChannel = directory.asWatchChannel(mode = KWatchChannel.Mode.SingleDirectory, scope = coroutineScope)

    coroutineScope.launch {
        watchChannel.consumeEach { event ->
            if (event.kind != KWatchEvent.Kind.Created) {
                return@consumeEach
            }
            logger.debug("Received created event.")
            val uuid = UUID.randomUUID().toString()

            val name = uuid + "/" + event.file.name
            File(serveDirectory + name).mkdir()
            event.file.copyTo(File(serveDirectory + name), true)
            files.add(FileObject(name, event.file.name))
            event.file.delete()
        }
    }

    Runtime.getRuntime().addShutdownHook(object : Thread() {
        override fun run(): Unit = runBlocking {
            watchChannel.close()
        }
    })
}
