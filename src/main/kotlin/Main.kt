import kotlin.system.exitProcess

val validCommands = arrayOf("exit")

fun main() {
    while (true) {
        print("$ ")
        val input: String = readln() // Wait for user input
        val command = input.split(" ")[0]
        if (command in validCommands) {
            exitProcess(0)
        } else {
            print("$command: command not found\n")
        }
    }
}
