import java.net.URI

rootProject.name = "airlanding"

sourceControl {
    gitRepository(URI("https://github.com/warriorzz/watchservice-ktx.git")) {
        producesModule("dev.vishna:watchservice-ktx")
    }
}