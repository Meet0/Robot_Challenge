import controller.RobotController
import model.TableTop
import service.CommandParserService
import java.io.File
import kotlin.system.exitProcess

fun main() {
    val robotController = RobotController(TableTop())
    val commandParserService = CommandParserService(robotController)

    while (true) {
        println(
            "Select Command input method (enter choice - 1 or 2): \n"
                    + "1. Interactive Command Input - one command at a time via console.\n"
                    + "2. Read Commands(each command on new line) from file"
        )
        when (readLine()?.toIntOrNull()) {
            1 -> {
                while (true) {
                    when (val command = readLine()) {
                        "RESTART" -> break
                        "EXIT" -> exitProcess(0)
                        else -> command?.let { commandParserService.parse(it) }
                    }
                }
            }
            2 -> {
                while (true) {
                    println("Enter path to file:")
                    when (val filePath = readLine()) {
                        "RESTART" -> break
                        "EXIT" -> exitProcess(0)
                        else -> {
                            val file = filePath?.let { File(it) }
                            if (file?.exists() == true) file.readLines().forEach { commandParserService.parse(it) }
                        }
                    }
                }
            }
            else -> println("Enter a valid choice(1-2).")
        }
    }
}