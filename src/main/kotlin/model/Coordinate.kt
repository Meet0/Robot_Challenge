package model

data class Coordinate(var x: Int, var y: Int) {

    override fun toString(): String {
        return "${this.x},${this.y}"
    }
}
