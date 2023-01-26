package controller

import model.Coordinate
import model.Direction
import model.Robot
import model.TableTop
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import java.lang.StringBuilder

class RobotControllerTest {
    private lateinit var controller: RobotController
    private lateinit var robot : Robot
    private var outContent = ByteArrayOutputStream()

    @BeforeEach
    fun setup() {
        System.setOut(PrintStream(outContent))
        controller = RobotController(TableTop())
        robot = Robot(id = 1, coordinate = Coordinate(0, 0), direction = Direction.NORTH)
    }

    @Test
    fun `test turnLeft For All Directions`() {
        controller.place(robot)

        controller.turnLeft()
        Assertions.assertEquals(Direction.EAST, controller.getActiveRobot().direction)

        controller.turnLeft()
        Assertions.assertEquals(Direction.SOUTH, controller.getActiveRobot().direction)

        controller.turnLeft()
        Assertions.assertEquals(Direction.WEST, controller.getActiveRobot().direction)

        controller.turnLeft()
        Assertions.assertEquals(Direction.NORTH, controller.getActiveRobot().direction)
    }

    @Test
    fun `test turnRight For All Directions`() {
        controller.place(robot)

        controller.turnRight()
        Assertions.assertEquals(Direction.WEST, controller.getActiveRobot().direction)

        controller.turnRight()
        Assertions.assertEquals(Direction.SOUTH, controller.getActiveRobot().direction)

        controller.turnRight()
        Assertions.assertEquals(Direction.EAST, controller.getActiveRobot().direction)

        controller.turnRight()
        Assertions.assertEquals(Direction.NORTH, controller.getActiveRobot().direction)
    }

    @Test
    fun `test place given a robot with valid coordinate`() {
        Assertions.assertTrue(controller.place(robot))
        Assertions.assertEquals(robot, controller.getActiveRobot())
        Assertions.assertEquals(1, controller.getRobotsOnTable().size)
        Assertions.assertEquals(robot.id, controller.tableTop.getTableTop()[robot.coordinate.x][robot.coordinate.y])
    }

    @Test
    fun `test place given a robot with invalid coordinate`() {
        val invalidRobot = Robot(coordinate = Coordinate(10,10), direction = Direction.NORTH)
        Assertions.assertFalse(controller.place(invalidRobot))
        Assertions.assertNotEquals(robot, controller.getActiveRobot())
        Assertions.assertEquals(0, controller.getRobotsOnTable().size)
    }

    @Test
    fun `test place given the same robot already exists at that location`() {
        Assertions.assertTrue(controller.place(robot))
        Assertions.assertTrue(controller.place(robot))
        Assertions.assertEquals(robot, controller.getActiveRobot())
        Assertions.assertEquals(1, controller.getRobotsOnTable().size)
    }

    @Test
    fun `test place given other robot already exists at that location`() {
        val otherRobot = Robot(id = 10, coordinate = Coordinate(0,0), direction = Direction.NORTH)
        Assertions.assertTrue(controller.place(otherRobot))
        Assertions.assertFalse(controller.place(robot))
        Assertions.assertEquals(otherRobot, controller.getActiveRobot())
        Assertions.assertEquals(1, controller.getRobotsOnTable().size)
    }

    @Test
    fun `test place with multiple valid robots`() {
        Assertions.assertTrue(controller.place(robot))
        Assertions.assertTrue(controller.place(Robot(id = 2, coordinate = Coordinate(1,1), direction = Direction.SOUTH)))
        Assertions.assertTrue(controller.place(Robot(id = 3, coordinate = Coordinate(2,2), direction = Direction.EAST)))
        Assertions.assertTrue(controller.place(Robot(id = 4, coordinate = Coordinate(3,3), direction = Direction.WEST)))
        Assertions.assertEquals(robot, controller.getActiveRobot())
        Assertions.assertEquals(4, controller.getRobotsOnTable().size)
    }

    @Test
    fun `test move given no robots exist on table top`() {
        controller.move()
        Assertions.assertEquals(-1, controller.getActiveRobot().coordinate.x)
        Assertions.assertEquals(-1, controller.getActiveRobot().coordinate.y)
    }


    @Test
    fun `test move to a valid space given robot facing NORTH`() {
        val oldCoordinate = robot.coordinate.copy()
        controller.place(robot)
        controller.move()
        Assertions.assertEquals(oldCoordinate.x, robot.coordinate.x)
        Assertions.assertEquals(oldCoordinate.y+1, robot.coordinate.y)
        Assertions.assertEquals(-1, controller.tableTop.getTableTop()[oldCoordinate.x][oldCoordinate.y])
    }

    @Test
    fun `test move to a valid space given robot facing SOUTH`() {
        robot.direction = Direction.SOUTH
        robot.coordinate = Coordinate(1,1)
        val oldCoordinate = robot.coordinate.copy()
        controller.place(robot)
        controller.move()
        Assertions.assertEquals(oldCoordinate.x, robot.coordinate.x)
        Assertions.assertEquals(oldCoordinate.y-1, robot.coordinate.y)
        Assertions.assertEquals(-1, controller.tableTop.getTableTop()[oldCoordinate.x][oldCoordinate.y])
    }

    @Test
    fun `test move to a valid space given robot facing EAST`() {
        robot.direction = Direction.EAST
        val oldCoordinate = robot.coordinate.copy()
        controller.place(robot)
        controller.move()
        Assertions.assertEquals(oldCoordinate.x+1, robot.coordinate.x)
        Assertions.assertEquals(oldCoordinate.y, robot.coordinate.y)
        Assertions.assertEquals(-1, controller.tableTop.getTableTop()[oldCoordinate.x][oldCoordinate.y])
    }

    @Test
    fun `test move to a valid space given robot facing WEST`() {
        robot.direction = Direction.WEST
        robot.coordinate = Coordinate(1,1)
        val oldCoordinate = robot.coordinate.copy()
        controller.place(robot)
        controller.move()
        Assertions.assertEquals(oldCoordinate.x-1, robot.coordinate.x)
        Assertions.assertEquals(oldCoordinate.y, robot.coordinate.y)
        Assertions.assertEquals(-1, controller.tableTop.getTableTop()[oldCoordinate.x][oldCoordinate.y])
    }

    @Test
    fun `test move to a invalid space`() {
        val oldCoordinate = robot.coordinate.copy()
        controller.place(robot)
        controller.turnRight()
        controller.move()
        Assertions.assertEquals(oldCoordinate.x, robot.coordinate.x)
        Assertions.assertEquals(oldCoordinate.y, robot.coordinate.y)
    }

    @Test
    fun `test move given multiple robots on table top`() {
        val oldCoordinate = robot.coordinate.copy()
        controller.place(robot)
        controller.place(Robot(coordinate = Coordinate(1,1), direction = Direction.SOUTH))
        controller.place(Robot(coordinate = Coordinate(2,2), direction = Direction.EAST))
        controller.move()
        Assertions.assertEquals(oldCoordinate.x, robot.coordinate.x)
        Assertions.assertEquals(oldCoordinate.y+1, robot.coordinate.y)
        Assertions.assertEquals(-1, controller.tableTop.getTableTop()[oldCoordinate.x][oldCoordinate.y])
    }

    @Test
    fun `test report with single robot on table top`() {
        controller.place(robot)
        controller.report()
        val expected = StringBuilder("Total Robots on Table: 1\n"
                + "Active Robot: ROBOT ${robot.id} ${robot.coordinate},${robot.direction.name}\n"
                + "Other Robots: ").toString()
        Assertions.assertTrue(outContent.toString().contains(expected))
    }

    @Test
    fun `test report with multiple robot on table top`() {
        val otherRobot1 = Robot(id = 2, coordinate = Coordinate(1,1), direction = Direction.SOUTH)
        val otherRobot2 = Robot(id = 3, coordinate = Coordinate(2,2), direction = Direction.EAST)
        controller.place(robot)
        controller.place(otherRobot1)
        controller.place(otherRobot2)
        controller.report()
        val expected = StringBuilder("Total Robots on Table: 3\n"
                + "Active Robot: ROBOT ${robot.id} ${robot.coordinate},${robot.direction.name}\n"
                + "Other Robots: ROBOT ${otherRobot1.id} ${otherRobot1.coordinate},${otherRobot1.direction.name}\n"
                + "ROBOT ${otherRobot2.id} ${otherRobot2.coordinate},${otherRobot2.direction.name}\n").toString()
        Assertions.assertEquals(expected, outContent.toString())
        Assertions.assertTrue(outContent.toString().contains(expected))
    }

    @Test
    fun `test setActive given multiple robots on table top`() {
        controller.place(robot)
        controller.place(Robot(coordinate = Coordinate(1,1), direction = Direction.SOUTH))
        controller.place(Robot(coordinate = Coordinate(2,2), direction = Direction.EAST))
        val otherInactiveRobotId =
            controller.getRobotsOnTable().keys.first { it != controller.getActiveRobot().id }

        Assertions.assertEquals(robot.id, controller.getActiveRobot().id)
        controller.setActive(otherInactiveRobotId)
        Assertions.assertEquals(otherInactiveRobotId, controller.getActiveRobot().id)
    }

    @Test
    fun `test setActive given the robot id does not exist on table top`() {
        controller.place(robot)
        val invalidId = 10

        Assertions.assertEquals(robot.id, controller.getActiveRobot().id)
        controller.setActive(invalidId)
        Assertions.assertTrue(outContent.toString().contains(("Robot $invalidId does not exist on table")))
    }
}