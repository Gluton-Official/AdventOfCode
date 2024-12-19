package util

import java.util.PriorityQueue
import kotlin.apply

fun <T> List<List<T>>.astar(
    start: Position,
    end: Position,
    obstacles: List<T> = emptyList(),
    weight: (from: Position, to: Position) -> Float = { from, to -> 1f },
    heuristic: (from: Position) -> Float = { from -> from.distance(end) },
): List<Position>? {
    val pathScore = mutableMapOf(start to heuristic(start))
    val discoveredPositions = PriorityQueue<Position>(compareBy { pathScore.getOrDefault(it, Float.MAX_VALUE) }).apply { add(start) }
    val previousPosition = mutableMapOf<Position, Position>()
    val minCostToPosition = mutableMapOf(start to Float.MAX_VALUE)

    while (discoveredPositions.isNotEmpty()) {
        var current = discoveredPositions.remove()
        if (current == end) {
            val path = mutableListOf(current)
            while (current in previousPosition.keys) {
                current = previousPosition[current]!!
                path.add(0, current)
            }
            return path
        }

        for (adjacent in Direction.entries.map { current.offset(it) }.filterNot { getOrNull(it)?.let { it in obstacles } != false }) {
            val currentPathScore = pathScore.getOrDefault(current, Float.MAX_VALUE) + weight(current, adjacent)
            if (currentPathScore < pathScore.getOrDefault(adjacent, Float.MAX_VALUE)) {
                previousPosition[adjacent] = current
                pathScore[adjacent] = currentPathScore
                minCostToPosition[adjacent] = currentPathScore + heuristic(adjacent)
                if (adjacent !in discoveredPositions) {
                    discoveredPositions += adjacent
                }
            }
        }
    }
    return null
}
