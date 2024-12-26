import java.io.File
import kotlin.system.exitProcess
import java.nio.file.Files
import java.nio.file.Path

val shellBuiltIns = arrayOf("exit", "echo", "type", "pwd", "cd")

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

val startingDir: String = System.getProperty("user.dir")

var currentDir = startingDir

fun main() {
    while (true) {
        print("$ ")
        val input: String = readln() // Wait for user input
        val command = input.split(" ")[0]
        val path = System.getenv("PATH")
        val commandFullLocations = path.split(":")
        var found = false
        val isBuiltIn = command in shellBuiltIns

        if (isBuiltIn) {
            when (command) {
                "exit" -> exitProcess(0)
                "echo" -> {
                    val remainder = getEverythingExceptFirstArg(input)
                    print(remainder + "\n")
                }
                "type" -> {
                    val secondArg = getSecondArgument(input)
                    var isBuiltIn = secondArg in shellBuiltIns
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
                "pwd" -> {
                    println(currentDir)
                }
                "cd" -> {
                    var newDir = getSecondArgument(input)
                    val relativePathRegex = Regex("^\\./")
                    val backupPathRegex = Regex("^\\.\\./")
                    if (newDir.contains(relativePathRegex)) {
                        // add new directory to current directory
                       newDir = currentDir + "/" + newDir.replace("./", "")
                    }
                    if (newDir.contains(backupPathRegex)) {
                        while (newDir.contains(backupPathRegex)) {
                            // remove one backup step from the command
                            newDir = newDir.replace(backupPathRegex, "")
                            // back up current directory by one
                            currentDir = currentDir.split("/").dropLast(1).joinToString("/")
                        }
                        newDir = currentDir
                    }
                    val directory = File(newDir)
                    if (directory.exists() && directory.isDirectory) {
                        currentDir = newDir
                    } else {
                        println("cd: $newDir: No such file or directory")
                    }
                }
            }
        } else if (!isBuiltIn) {
            for (dir in commandFullLocations) {
                if (fileExistsInDir(dir, command)) {
                    val secondArg = getSecondArgument(input)
                    ProcessBuilder(command, secondArg)
                            .directory(File(currentDir))
                            .redirectOutput(ProcessBuilder.Redirect.INHERIT)
                            .redirectError(ProcessBuilder.Redirect.INHERIT)
                            .start()
                            .waitFor()
                    found = true
                    break
                }
            }
        }
        if (!found && !isBuiltIn) {
            print("$command: command not found\n")
        }
    }
}
