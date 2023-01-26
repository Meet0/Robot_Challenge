package model

class RobotFactory {
    private var idCounter = 1;

    fun Robot(x:Int, y:Int, direction: Direction): Robot {
        return Robot(idCounter++, Coordinate(x,y), direction)
    }
}