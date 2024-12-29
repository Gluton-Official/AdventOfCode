package aoc2024

import AoCPuzzle
import com.sun.org.apache.xalan.internal.lib.ExsltDatetime.time
import com.sun.org.apache.xalan.internal.lib.ExsltDynamic.map
import util.Direction
import util.Input
import util.Position
import util.astar
import util.forEach2D
import util.forEach2DIndexed
import util.get
import util.getOrNull
import util.set
import java.time.temporal.TemporalAdjusters.next

object Day20 : AoCPuzzle() {
    override val part1Tests = listOf(
		Test(44, """
            ###############
            #...#...#.....#
            #.#.#.#.#.###.#
            #S#...#.#.#...#
            #######.#.#.###
            #######.#.#...#
            #######.#.###.#
            ###..E#...#...#
            ###.#######.###
            #...###...#...#
            #.#####.#.###.#
            #.#...#.#.#...#
            #.#.#.#.#.#.###
            #...#...#...###
            ###############
        """.trimIndent()),
	)

    override fun part1(input: Input): Int {
        val track = input.map { it.mapTo(mutableListOf<Pair<Char, Int?>>()) { it to null } }
        lateinit var start: Position
        track.forEach2DIndexed {
            when (it.first) {
                'S' -> start = this
            }
        }

        var current = start
        var time = 0
        do {
            val nextDirection = Direction.entries.find {
                track[current.offset(it)].let { (tile, time) ->
                    tile in ".E" && time == null
                }
            }!!
            track[current] = track[current].copy(second = time++)
            current = current.offset(nextDirection)
        } while (track[current].first != 'E')
        track[current] = track[current].copy(second = time)

        val skips = mutableMapOf<Int, Int>()
        current = start
        do {
            val currentTime = track[current].second!!
            val skipDirections = Direction.entries.filter {
                track.getOrNull(current.offset(it, 2))?.let { (_, time) -> time != null && (time - 2) > currentTime } == true
            }
            skipDirections.forEach {
                val skipTime = track[current.offset(it, 2)].second!! - currentTime - 2
                skips.compute(skipTime) { _, count -> if (count != null) count + 1 else 1 }
            }
            val nextDirection = Direction.entries.find {
                track[current.offset(it)].second.let { it != null && it > currentTime }
            }!!
            current = current.offset(nextDirection)
        } while (track[current].first != 'E')

//        skips.toSortedMap().forEach { (skipTime, count) ->
//            println("$count cheat${if (count != 1) "s" else ""} that save${if (count == 1) "s" else ""} $skipTime picoseconds")
//        }

        return skips.filterKeys { it >= 100 }.values.sum()
    }

    override val part2Tests = listOf(
		Test(285, """
            ###############
            #...#...#.....#
            #.#.#.#.#.###.#
            #S#...#.#.#...#
            #######.#.#.###
            #######.#.#...#
            #######.#.###.#
            ###..E#...#...#
            ###.#######.###
            #...###...#...#
            #.#####.#.###.#
            #.#...#.#.#...#
            #.#.#.#.#.#.###
            #...#...#...###
            ###############
        """.trimIndent()),
	)

    override fun part2(input: Input): Int {
        val track = input.map { it.mapTo(mutableListOf<Pair<Char, Int?>>()) { it to null } }
        lateinit var start: Position
        track.forEach2DIndexed {
            when (it.first) {
                'S' -> start = this
            }
        }

        val path = mutableMapOf<Position, Int>()
        var current = start
        var time = 0
        do {
            path[current] = time
            val nextDirection = Direction.entries.find {
                track[current.offset(it)].let { (tile, time) ->
                    tile in ".E" && time == null
                }
            }!!
            track[current] = track[current].copy(second = time++)
            current = current.offset(nextDirection)
        } while (track[current].first != 'E')
        track[current] = track[current].copy(second = time)
        path[current] = time

        val skips = mutableMapOf<Int, Int>()
        current = start
        path.entries.forEachIndexed { index, (current, currentTime) ->
            path.entries.drop(index)
                .mapNotNull { (tile, time) -> current.manhattanDistance(tile).takeIf { it <= 20 }?.let { it to time } }
                .mapNotNull { (skipDistance, time) -> (time - currentTime - skipDistance).takeIf { it >= 100 } }
                .forEach { skipTime -> skips.compute(skipTime) { _, count -> if (count != null) count + 1 else 1 } }
        }

//        skips.toSortedMap().forEach { (skipTime, count) ->
//            println("$count cheat${if (count != 1) "s" else ""} that save${if (count == 1) "s" else ""} $skipTime picoseconds")
//        }

        return skips.filterKeys { it >= 100 }.values.sum()
    }

    @JvmStatic
    fun main(args: Array<String>) {
//        if (testPart1()) {
            runPart1()
//        }

//        if (testPart2()) {
            runPart2()
//        }
    }
}
