package aoc2024

import AoCPuzzle
import util.Direction
import util.Input
import util.List2D
import util.Position
import util.arrayDequeOf
import util.forEach2DIndexed
import util.getOrNull
import util.mapStrings2D
import util.set
import kotlin.experimental.xor

object Day12 : AoCPuzzle() {
    override val part1Tests = listOf(
		Test(140, """
            AAAA
            BBCD
            BBCC
            EEEC
        """.trimIndent()),
        Test(772, """
            OOOOO
            OXOXO
            OOOOO
            OXOXO
            OOOOO
        """.trimIndent()),
        Test(1930, """
            RRRRIICCFF
            RRRRIICCCF
            VVRRRCCFFF
            VVRCCCJFFF
            VVVVCJJCFE
            VVIVCCJJEE
            VVIIICJJEE
            MIIIIIJJEE
            MIIISIJEEE
            MMMISSJEEE
        """.trimIndent()),
	)

    override fun part1(input: Input): Int {
        val plots = input.map { it.toMutableList() as MutableList<Char?> }
        var plotPrice = 0
        plots.forEach2DIndexed { plant ->
            if (plant == null) return@forEach2DIndexed
            val plotsToSearch = arrayDequeOf<Position>(this)
            val regionPlots = mutableSetOf<Position>(this)
            var perimeter = 0
            while (plotsToSearch.isNotEmpty()) {
                val position = plotsToSearch.removeFirst()
                for (direction in Direction.entries) {
                    val adjacent = position.offset(direction)
                    if (adjacent !in regionPlots) {
                        perimeter++
                        if (plots.getOrNull(adjacent) == plant) {
                            perimeter--
                            regionPlots += adjacent
                            plotsToSearch += adjacent
                        }
                    }
                }
            }
            regionPlots.forEach {
                plots[it] = null
            }
            plotPrice += perimeter * regionPlots.size
        }
        return plotPrice
    }

    override val part2Tests = listOf(
		Test(80, """
            AAAA
            BBCD
            BBCC
            EEEC
        """.trimIndent()),
        Test(436, """
            OOOOO
            OXOXO
            OOOOO
            OXOXO
            OOOOO
        """.trimIndent()),
        Test(236, """
            EEEEE
            EXXXX
            EEEEE
            EXXXX
            EEEEE
        """.trimIndent()),
        Test(368, """
            AAAAAA
            AAABBA
            AAABBA
            ABBAAA
            ABBAAA
            AAAAAA
        """.trimIndent()),
        Test(1206, """
            RRRRIICCFF
            RRRRIICCCF
            VVRRRCCFFF
            VVRCCCJFFF
            VVVVCJJCFE
            VVIVCCJJEE
            VVIIICJJEE
            MIIIIIJJEE
            MIIISIJEEE
            MMMISSJEEE
        """.trimIndent()),
	)

    override fun part2(input: Input): Int {
        val plots = input.map { it.toMutableList() as MutableList<Char?> }
        var plotPrice = 0
        plots.forEach2DIndexed { plant ->
            if (plant == null) return@forEach2DIndexed
            val plotsToSearch = arrayDequeOf<Position>(this)
            val regionPlots = mutableSetOf<Position>(this)
            while (plotsToSearch.isNotEmpty()) {
                val position = plotsToSearch.removeFirst()
                for (direction in Direction.entries) {
                    val adjacent = position.offset(direction)
                    if (adjacent !in regionPlots && plots.getOrNull(adjacent) == plant) {
                        regionPlots += adjacent
                        plotsToSearch += adjacent
                    }
                }
            }
            regionPlots.forEach {
                plots[it] = null
            }
            val vertices = mutableMapOf<Position, Int>()
            regionPlots.forEach { plot ->
                listOf(
                    plot,
                    plot.copy(row = plot.row + 1),
                    plot.copy(column = plot.column + 1),
                    plot.copy(row = plot.row + 1, column = plot.column + 1),
                ).forEach {
                    vertices.computeIfAbsent(it) { newVertex ->
                        listOf(
                            newVertex.copy(row = newVertex.row - 1, column = newVertex.column - 1),
                            newVertex.copy(row = newVertex.row - 1),
                            newVertex.copy(column = newVertex.column - 1),
                            newVertex,
                        ).foldIndexed(0) { index, acc, adjacentPlot ->
                            if (adjacentPlot in regionPlots) {
                                acc or (1 shl index)
                            } else acc
                        }
                    }
                }
            }
            val uniqueVertices = vertices.values.fold(0) { acc, it ->
                when (it.countOneBits()) {
                    1, 3 -> acc + 1
                    2 if it in listOf(0b0110, 0b1001) -> acc + 2
                    else -> acc
                }
            }
            plotPrice += uniqueVertices * regionPlots.size
        }
        return plotPrice
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
