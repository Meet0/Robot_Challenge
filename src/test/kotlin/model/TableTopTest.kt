package model

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class TableTopTest {
    private val tableTop = TableTop()

    @Test
    fun `should validate valid coordinate`() {
        val coordinate = Coordinate(1,1)
        Assertions.assertTrue(tableTop.isValidCoordinate(coordinate))
    }

    @Test
    fun `should validate invalid coordinate`() {
        val coordinate = Coordinate(10,10)
        Assertions.assertFalse(tableTop.isValidCoordinate(coordinate))
    }

    @Test
    fun `should validate space is empty`() {
        val coordinate = Coordinate(1,1)
        Assertions.assertTrue(tableTop.isSpaceEmpty(coordinate, 1))
    }

    @Test
    fun `should validate space is empty given the same value resides at space`() {
        val coordinate = Coordinate(1,1)
        tableTop.getTableTop()[coordinate.x][coordinate.y] = 10
        Assertions.assertTrue(tableTop.isSpaceEmpty(coordinate, 10))
    }

    @Test
    fun `should validate space is not empty`() {
        val coordinate = Coordinate(1,1)
        tableTop.getTableTop()[coordinate.x][coordinate.y] = 10
        Assertions.assertFalse(tableTop.isSpaceEmpty(coordinate, 11))
    }

    @Test
    fun `should clear given coordinate`() {
        val coordinate = Coordinate(1, 1)
        tableTop.getTableTop()[coordinate.x][coordinate.y] = 10
        Assertions.assertEquals(tableTop.getTableTop()[coordinate.x][coordinate.y], 10)
        tableTop.clearCoordinate(coordinate)
        Assertions.assertEquals(tableTop.getTableTop()[coordinate.x][coordinate.y], -1)
    }
}