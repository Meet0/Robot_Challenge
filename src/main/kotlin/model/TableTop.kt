package model

data class TableTop(
    var length: Int = 5,
    var width: Int = 5
) {
    private var tableTop = List(length) { MutableList(width) { -1 } }

    /**
     * Checks if a given coordinate is valid for the TableTop
     * @param coordinate The coordinate to be checked
     * @return true if the coordinate is valid, false otherwise
     */
    fun isValidCoordinate(coordinate: Coordinate): Boolean {
        return coordinate.x in 0 until width && coordinate.y in 0 until length
    }

    /**
     * Checks if a given coordinate is empty or contains a given value
     * @param coordinate The coordinate to be checked
     * @param value The value to be checked
     * @return true if the coordinate is empty or contains the value, false otherwise
     */
    fun isSpaceEmpty(coordinate: Coordinate, value: Int): Boolean {
        return (this.tableTop[coordinate.x][coordinate.y] == -1) || (this.tableTop[coordinate.x][coordinate.y] == value)
    }

    /**
     * Gets the current TableTop
     * @return A list of lists containing the values of the TableTop
     */
    fun getTableTop(): List<MutableList<Int>> {
        return this.tableTop
    }

    /**
     * Clears a given coordinate from the TableTop
     * @param coordinate The coordinate to be cleared
     */
    fun clearCoordinate(coordinate: Coordinate) {
        this.tableTop[coordinate.x][coordinate.y] = -1
    }
}