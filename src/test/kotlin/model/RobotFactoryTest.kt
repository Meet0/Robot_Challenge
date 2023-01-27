package model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class RobotFactoryTest {
    val factory = RobotFactory()

    @Test
    fun `test Robot factory for auto-incremental id's`() {
        val robot1 = factory.Robot(1,1, Direction.NORTH)
        val robot2 = factory.Robot(2,2, Direction.NORTH)
        val robot3 = factory.Robot(3,3, Direction.NORTH)
        val robot4 = factory.Robot(4,4, Direction.NORTH)
        assertEquals(1, robot1.id)
        assertEquals(2, robot2.id)
        assertEquals(3, robot3.id)
        assertEquals(4, robot4.id)
    }

}