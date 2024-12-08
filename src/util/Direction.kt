package util

import util.Direction.*
import kotlin.math.absoluteValue

enum class Direction {
    North, East, South, West;

    fun isVertical(): Boolean = when (this) {
        North, South -> true
        else -> false
    }

    fun isHorizontal(): Boolean = when (this) {
        East, West -> true
        else -> false
    }

    @Deprecated("", replaceWith = ReplaceWith("opposite()"))
    val opposite: Direction get() = when (this) {
        North -> South
        South -> North
        East -> West
        West -> East
    }

    fun opposite(): Direction = when (this) {
        North -> South
        South -> North
        East -> West
        West -> East
    }

    fun rotateClockwise(): Direction = when (this) {
        North -> East
        East -> South
        South -> West
        West -> North
    }

    fun rotateCounterClockwise(): Direction = when (this) {
        North -> West
        West -> South
        South -> East
        East -> North
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
