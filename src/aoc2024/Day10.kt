package aoc2024

import AoCPuzzle
import util.Direction
import util.Input
import util.Position
import util.arrayDequeOf
import util.forEach2D
import util.forEach2DIndexed
import util.get
import util.getOrNull
import javax.swing.Spring.height

object Day10 : AoCPuzzle() {
    override val part1Tests = listOf(
		Test(1, """
            0123
            1234
            8765
            9876
        """.trimIndent()),
        Test(36, """
            89010123
            78121874
            87430965
            96549874
            45678903
            32019012
            01329801
            10456732
        """.trimIndent()),
	)

    override fun part1(input: Input): Int {
        val topographicMap = input.map { it.map(Char::digitToInt) }

        val trailHeads = mutableListOf<Position>()
        topographicMap.forEach2DIndexed {
            if (it == 0) trailHeads += this
        }
        return trailHeads.sumOf {
            val trails = arrayDequeOf<Position>(it)
            val hillTops = mutableSetOf<Position>()
            while (trails.isNotEmpty()) {
                val position = trails.removeFirst()
                val height = topographicMap[position]
                if (height == 9) {
                    hillTops += position
                } else {
                    trails.addAll(Direction.entries.mapNotNull { direction ->
                        position.offset(direction).takeIf {
                            topographicMap.getOrNull(it) == height + 1
                        }
                    })

                }
            }
            hillTops.count()
        }
    }

    override val part2Tests = listOf(
		Test(81, """
            89010123
            78121874
            87430965
            96549874
            45678903
            32019012
            01329801
            10456732
        """.trimIndent()),
	)

    override fun part2(input: Input): Int {
        val topographicMap = input.map { it.map(Char::digitToInt) }

        val trailHeads = mutableListOf<Position>()
        topographicMap.forEach2DIndexed {
            if (it == 0) trailHeads += this
        }
        return trailHeads.sumOf {
            val trails = arrayDequeOf<Position>(it)
            var rating = 0
            while (trails.isNotEmpty()) {
                val position = trails.removeFirst()
                val height = topographicMap[position]
                if (height == 9) {
                    rating++
                } else {
                    trails.addAll(Direction.entries.mapNotNull { direction ->
                        position.offset(direction).takeIf {
                            topographicMap.getOrNull(it) == height + 1
                        }
                    })

                }
            }
            rating
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        if (testPart1()) {
            runPart1()
        }

        if (testPart2()) {
            runPart2()
        }
    }
}
