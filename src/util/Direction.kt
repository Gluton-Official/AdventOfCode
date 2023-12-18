package util

import util.Direction.*
import kotlin.math.absoluteValue

enum class Direction {
    North, East, South, West;

    val opposite: Direction get() = when (this) {
        North -> South
        South -> North
        East -> West
        West -> East
    }

    fun onAxisOf(other: Direction): Boolean = this == other || this == other.opposite
}

data class Position(val row: Int, val column: Int) {
    fun distance(other: Position): Float = (other.row - row) / (other.column - column).toFloat()
    fun manhattanDistance(other: Position): Int =
        (row - other.row).absoluteValue + (column - other.column).absoluteValue

    fun offset(direction: Direction, scale: Int = 1): Position = when (direction) {
        North -> copy(row = row - scale)
        East -> copy(column = column + scale)
        South -> copy(row = row + scale)
        West -> copy(column = column - scale)
    }
}
