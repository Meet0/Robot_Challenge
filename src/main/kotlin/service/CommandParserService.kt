package service

import controller.RobotController
import model.Command
import model.Coordinate
import model.Direction
import model.Robot

class CommandParserService(private val robotController: RobotController) {
    private var placeCommandReceived: Boolean  = false
    private var idCounter = 1

    /**
     * Parses a command string and executes commands.
     *
     * @param commandString The command string to parse.
     */
    fun parse(commandString: String) {
        val command = Command.values().find { it.regex.matches(commandString) }
        if (command != null) {
            when (command) {
                Command.PLACE -> robotController.place(createRobot(command, commandString))
                Command.ROBOT -> if (placeCommandReceived) activate(command, commandString)
                Command.MOVE -> if (placeCommandReceived) robotController.move()
                Command.LEFT -> if (placeCommandReceived) robotController.turnLeft()
                Command.RIGHT -> if (placeCommandReceived) robotController.turnRight()
                Command.REPORT -> if (placeCommandReceived) robotController.report()
            }
        }
    }

    /**
     * Generates a Robot object from the given command and command string.
     *
     * @param command The command to generate the robot with.
     * @param commandString The command string to use for generating the robot.
     * @return The generated Robot object.
     */
    private fun createRobot(command: Command, commandString: String): Robot {
        val (x, y, direction) = command.regex.find(commandString)!!.destructured
        val robot = Robot(id = idCounter, coordinate = Coordinate(x.toInt(), y.toInt()), direction = Direction.valueOf(direction))
        idCounter++
        placeCommandReceived = true
        return robot
    }

    /**
     * Activates a robot given the command and command string using the id.
     *
     * @param command The command to use for activating the robot.
     * @param commandString The command string to use for activating the robot.
     */
    private fun activate(command: Command, commandString: String) {
        val (id) = command.regex.find(commandString)!!.destructured
        robotController.setActive(id.toInt())
    }

    fun placeCommandReceived(): Boolean = placeCommandReceived
}