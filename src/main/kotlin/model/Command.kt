package model

enum class Command(val regex: Regex) {
    PLACE(Regex("PLACE (\\d+),(\\d+),(NORTH|SOUTH|EAST|WEST)")),
    MOVE(Regex("MOVE")),
    LEFT(Regex("LEFT")),
    RIGHT(Regex("RIGHT")),
    REPORT(Regex("REPORT")),
    ROBOT(Regex("ROBOT (\\d+)"))
}