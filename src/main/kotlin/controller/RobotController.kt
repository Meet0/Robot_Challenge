package controller

import model.Coordinate
import model.Direction
import model.Robot
import model.TableTop
import java.lang.StringBuilder

open class RobotController(val tableTop: TableTop) {
    private var robotsOnTable: MutableMap<Int, Robot> = mutableMapOf()
    private val defaultRobot = Robot(-1, Coordinate(-1,-1), Direction.SOUTH)
    private var activeRobot: Robot = defaultRobot

    /**
     * left - rotates the direction 90 degrees to the left
     */
    fun turnLeft() {
        when (activeRobot.direction) {
            Direction.NORTH -> activeRobot.direction = Direction.EAST
            Direction.SOUTH -> activeRobot.direction = Direction.WEST
            Direction.EAST -> activeRobot.direction = Direction.SOUTH
            Direction.WEST -> activeRobot.direction = Direction.NORTH
        }
    }

    /**
     * right - rotates the direction 90 degrees to the right
     */
    fun turnRight() {
        when (activeRobot.direction) {
            Direction.NORTH -> activeRobot.direction = Direction.WEST
            Direction.SOUTH -> activeRobot.direction = Direction.EAST
            Direction.EAST -> activeRobot.direction = Direction.NORTH
            Direction.WEST -> activeRobot.direction = Direction.SOUTH
        }
    }

    /**
     * place - places the robot onto the tabletop
     * @param robot - the robot to be placed
     * @return Boolean - true if robot is placed successfully, false otherwise
     */
    fun place(robot: Robot): Boolean {
        if (tableTop.isValidCoordinate(robot.coordinate) && tableTop.isSpaceEmpty(robot.coordinate, robot.id)) {
            tableTop.getTableTop()[robot.coordinate.x][robot.coordinate.y] = robot.id
            robotsOnTable[robot.id] = robot
            if (activeRobot == defaultRobot) activeRobot = robot
            return true
        }

        println("Unable to place the robot")
        return false
    }

    /**
     * move - moves the robot one step in the direction it is currently facing
     */
    fun move() {
        if (activeRobot.id == -1) return

        val oldCoordinate: Coordinate = activeRobot.coordinate.copy()
        when(activeRobot.direction) {
            Direction.NORTH -> activeRobot.coordinate.y++
            Direction.SOUTH -> activeRobot.coordinate.y--
            Direction.EAST -> activeRobot.coordinate.x++
            Direction.WEST -> activeRobot.coordinate.x--
        }
        if (place(activeRobot)) tableTop.clearCoordinate(oldCoordinate)
        else activeRobot.coordinate = oldCoordinate
    }

    /**
     * report - generates a string report of the robots on the tabletop and prints it
     */
    fun report() {
        if (robotsOnTable.isEmpty()) println("0 robots present on table top.")
        val output = StringBuilder()
            .append("Total Robots on Table: ", robotsOnTable.size).append("\n")
            .append("Active Robot: " + activeRobot.report()).append("\n")
            .append("Other Robots: " + robotsOnTable.filter { it.key != activeRobot.id }
                .map { robot -> robot.value.report() }
                .joinToString(separator = "\n")
            ).toString()

        println(output)
    }

    /**
     * Sets the active robot to the robot of the specified ID on the table.
     * @param id the ID of the robot to be set active.
     */
    fun setActive(id: Int) {
        if (robotsOnTable.containsKey(id)) activeRobot = robotsOnTable[id]!!
        else println("Robot $id does not exist on table")
    }

    /**
     * Gets the active robot.
     * @return The active robot.
     */
    fun getActiveRobot(): Robot {
        return activeRobot
    }

    /**
     * Gets the robots on the table.
     * @return A map of robots on the table, with their ID as the key.
     */
    fun getRobotsOnTable(): MutableMap<Int, Robot> {
        return robotsOnTable
    }
}