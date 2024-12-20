import java.io.File
import kotlin.system.exitProcess

val shellBuiltIns = arrayOf("exit", "echo", "type")

fun getSecondArgument(input: String): String {
    return input.split(" ").drop(1)[0]
}

fun getEverythingExceptFirstArg(input: String): String {
    return input.split(" ").drop(1).joinToString(" ")
}

fun fileExistsInDir(filesDir: String, fileName: String): Boolean {
    var file = File(filesDir, fileName)
    return file.exists()
}

fun main() {
    while (true) {
        print("$ ")
        val input: String = readln() // Wait for user input
        val command = input.split(" ")[0]
        val path = System.getenv("PATH")
        val commandFullLocations = path.split(":")

        if (command in shellBuiltIns) {
            when (command) {
                "exit" -> exitProcess(0)
                "echo" -> {
                    val remainder = getEverythingExceptFirstArg(input)
                    print(remainder + "\n")
                }
                "type" -> {
                    val secondArg = getSecondArgument(input)
                    var isBuiltIn = secondArg in shellBuiltIns
                    var found = false
                    if (isBuiltIn) {
                        print("$secondArg is a shell builtin\n")
                    }
                    if (!isBuiltIn) {
                        for (dir in commandFullLocations) {
                            if (fileExistsInDir(dir, secondArg)) {
                                val location = "${dir}/${secondArg}"
                                print("$secondArg is $location\n")
                                found = true
                                break
                            }
                        }
                    }
                    if (!isBuiltIn && !found) {
                        print("$secondArg: not found\n")
                    }
                }
            }
        } else {
            print("$command: command not found\n")
        }
    }
}
