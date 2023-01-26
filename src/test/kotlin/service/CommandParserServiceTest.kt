package service

import controller.RobotController
import model.Direction
import model.TableTop
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class CommandParserServiceTest {
    private lateinit var robotController: RobotController
    private lateinit var commandParserService: CommandParserService
    private var outContent = ByteArrayOutputStream()

    @BeforeEach
    fun setup() {
        System.setOut(PrintStream(outContent))
        robotController = RobotController(TableTop())
        commandParserService = CommandParserService(robotController)
    }

    @Test
    fun `should parse empty command and return`() {
        commandParserService.parse("")
        assertFalse(commandParserService.placeCommandReceived())
        assertEquals(0, robotController.getRobotsOnTable().size)
    }

    @Test
    fun `should parse place command`() {
        runPlaceCommand(1,2,Direction.NORTH)

        assertTrue(commandParserService.placeCommandReceived())
        assertEquals(1, robotController.getRobotsOnTable().size)
    }

    @Test
    fun `should parse robot command`() {
        runPlaceCommand(1,2,Direction.NORTH)
        runPlaceCommand(2,2,Direction.NORTH)
        val inActiveRobotId = robotController.getRobotsOnTable().keys.filter { it != robotController.getActiveRobot().id }.first()
        val commandString = "ROBOT $inActiveRobotId"

        assertNotEquals(inActiveRobotId, robotController.getActiveRobot().id)
        commandParserService.parse(commandString)
        assertEquals(inActiveRobotId, robotController.getActiveRobot().id)
    }

    @Test
    fun `should parse move command`() {
        runPlaceCommand(1,2,Direction.NORTH)
        val commandString = "MOVE"

        assertEquals(2, robotController.getActiveRobot().coordinate.y)
        commandParserService.parse(commandString)
        assertEquals(3, robotController.getActiveRobot().coordinate.y)
    }

    @Test
    fun `should parse left command`() {
        runPlaceCommand(1,2,Direction.NORTH)
        val commandString = "LEFT"

        commandParserService.parse(commandString)
        assertEquals(Direction.EAST, robotController.getActiveRobot().direction)
    }

    @Test
    fun `should parse right command`() {
        runPlaceCommand(1,2,Direction.NORTH)
        val commandString = "RIGHT"

        commandParserService.parse(commandString)
        assertEquals(Direction.WEST, robotController.getActiveRobot().direction)
    }

    @Test
    fun `should parse report command`() {
        runPlaceCommand(1,2,Direction.NORTH)
        val commandString = "REPORT"

        commandParserService.parse(commandString)
        assertTrue(outContent.toString().contains("1,2,NORTH\n"))
    }

    @Test
    fun `should not execute ROBOT command if place command has not been received`() {
        val commandString = "ROBOT 2"
        commandParserService.parse(commandString)

        assertEquals(0, robotController.getRobotsOnTable().size)
    }

    @Test
    fun `should not execute MOVE command if place command has not been received`() {
        val commandString = "MOVE"
        commandParserService.parse(commandString)

        assertEquals(0, robotController.getRobotsOnTable().size)
    }

    @Test
    fun `should not execute LEFT command if place command has not been received`() {
        val commandString = "LEFT"
        commandParserService.parse(commandString)

        assertEquals(0, robotController.getRobotsOnTable().size)
    }

    @Test
    fun `should not execute RIGHT command if place command has not been received`() {
        val commandString = "RIGHT"
        commandParserService.parse(commandString)

        assertEquals(0, robotController.getRobotsOnTable().size)
    }

    @Test
    fun `should not execute REPORT command if place command has not been received`() {
        val commandString = "REPORT"
        commandParserService.parse(commandString)

        assertEquals(0, robotController.getRobotsOnTable().size)
    }

    private fun runPlaceCommand(x: Int, y: Int, direction: Direction) {
        val commandString = "PLACE $x,$y,${direction.name}"
        commandParserService.parse(commandString)
    }

}