import kotlin.system.exitProcess

val validCommands = arrayOf("exit", "echo")

fun main() {
    while (true) {
        print("$ ")
        val input: String = readln() // Wait for user input
        val command = input.split(" ")[0]
        val remainder = input.split(" ").drop(1).joinToString(" ")
        if (command in validCommands) {
            when (command) {
                "exit" -> exitProcess(0)
                "echo" -> print(remainder + "\n")
            }
        } else {
            print("$command: command not found\n")
        }
    }
}
