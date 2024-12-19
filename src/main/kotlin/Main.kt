import kotlin.system.exitProcess

val validCommands = arrayOf("exit", "echo", "type")

fun getSecondArgument(input: String): String {
    return input.split(" ").drop(1)[0]
}

fun getEverythingExceptFirstArg(input: String): String {
    return input.split(" ").drop(1).joinToString(" ")
}

fun main() {
    while (true) {
        print("$ ")
        val input: String = readln() // Wait for user input
        val command = input.split(" ")[0]

        if (command in validCommands) {
            when (command) {
                "exit" -> exitProcess(0)
                "echo" -> {
                    val remainder = getEverythingExceptFirstArg(input)
                    print(remainder + "\n")
                }
                "type" -> {
                    val secondArg = getSecondArgument(input)
                    if (secondArg in validCommands) {
                        print("$secondArg is a shell builtin\n")
                    } else {
                        print("$secondArg: not found\n")
                    }
                }
            }
        } else {
            print("$command: command not found\n")
        }
    }
}
