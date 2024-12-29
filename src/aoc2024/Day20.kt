package aoc2024

import AoCPuzzle
import util.Direction
import util.Input
import util.firstNotNullOf2DIndexed
import util.get
import util.set
import util.zipWith

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
        val cheatDuration = 2

        val track = input.map { it.zipWith<Int?>(null).toMutableList() }
        val start = track.firstNotNullOf2DIndexed { (char, _) -> takeIf { char == 'S' } }

        val path = buildMap {
            var current = start
            var time = 0
            while (true) {
                val (char) = track[current]
                track[current] = char to time
                put(current, time++)
                if (char == 'E') return@buildMap
                current = Direction.entries.map(current::offset)
                    .first { position -> track[position] in listOf('.' to null, 'E' to null) }
            }
        }

        val skips = buildMap<Int, Int> {
            path.forEach { (current, currentTime) ->
                Direction.entries
                    .map { current.offset(it, 2) }
                    .filter { position -> path[position]?.let { time -> time - cheatDuration > currentTime } == true }
                    .forEach { skipEnd ->
                        val skipTime = path[skipEnd]!! - currentTime - 2
                        compute(skipTime) { _, count -> if (count != null) count + 1 else 1 }
                    }
            }
        }

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
        val minSkipTime = 100

        val track = input.map { it.zipWith<Int?>(null).toMutableList() }
        val start = track.firstNotNullOf2DIndexed { (char, _) -> takeIf { char == 'S' } }

        val path = buildList {
            var current = start
            var time = 0
            while (true) {
                val (char) = track[current]
                track[current] = char to time
                add(current to time++)
                if (char == 'E') return@buildList
                current = Direction.entries.map(current::offset)
                    .first { position -> track[position] in listOf('.' to null, 'E' to null) }
            }
        }

        val skips = mutableMapOf<Int, Int>()
        path.forEachIndexed { index, (current, currentTime) ->
            path.drop(index)
                .mapNotNull { (tile, time) -> current.manhattanDistance(tile).takeIf { it <= 20 }?.let { it to time } }
                .mapNotNull { (skipDistance, time) -> (time - currentTime - skipDistance).takeIf { it >= minSkipTime } }
                .forEach { skipTime -> skips.compute(skipTime) { _, count -> if (count != null) count + 1 else 1 } }
        }

        return skips.filterKeys { it >= minSkipTime }.values.sum()
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
