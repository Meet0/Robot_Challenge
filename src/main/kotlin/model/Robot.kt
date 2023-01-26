package model

class Robot(
    var id: Int = -1,
    var coordinate: Coordinate = Coordinate(-1,-1),
    var direction: Direction = Direction.SOUTH
) {

    /**
     * report - returns a string of the current coordinates and direction
     *
     * @return String - a string of the current coordinates and direction
     */
    fun report(): String {
        return "ROBOT ${this.id} ${this.coordinate},${this.direction}"
    }
}

