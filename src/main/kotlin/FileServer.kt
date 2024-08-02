package ee.bjarn.airlanding

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.cio.*
import io.ktor.server.engine.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.collections.*
import io.ktor.util.logging.*
import kotlinx.serialization.Serializable
import java.io.File

fun server(logger: Logger) = embeddedServer(CIO, port = serverPort) {
    configureAuthentication()
    routing {
        authenticate("auth") {
            route("/api/v1") {
                get("/files") {
                    logger.debug("Received GET for /files from ${call.request.local.remoteAddress}")
                    call.respond(FileResponse(files.toList()))
                }

                staticFiles("/file", File(serveDirectory)) {
                    enableAutoHeadResponse()
                }
            }

            staticResources("/", "www", "index.html")
        }
    }
    logger.info("Configured server. Startup complete.")
}

fun Application.configureAuthentication() {
    install(Authentication) {
        basic("auth") {
            validate { credentials ->
                if (credentials.name == "airlanding" && credentials.password == "airlanding") {
                    UserIdPrincipal("client")
                } else {
                    null
                }
            }
        }
    }
}

val files = ConcurrentSet<FileObject>()

@Serializable data class FileObject(val id: String, val name: String)
@Serializable data class FileResponse(val files: List<FileObject>)