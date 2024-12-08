package aoc2024

import AoCPuzzle
import util.Input
import util.Position
import util.rangeInclusive
import kotlin.collections.flatMap

typealias Frequency = Char

object Day08 : AoCPuzzle() {
    override val part1Tests = listOf(
		Test(14, """
            ............
            ........0...
            .....0......
            .......0....
            ....0.......
            ......A.....
            ............
            ............
            ........A...
            .........A..
            ............
            ............
        """.trimIndent()),
	)

    private class AntennaMap private constructor(
        antennae: Map<Frequency, List<Position>>,
        val width: Int,
        val height: Int,
    ) : Map<Frequency, List<Position>> by antennae {
        companion object {
            operator fun invoke(input: Input): AntennaMap = AntennaMap(
                input.flatMapIndexed { rowIndex, row ->
                    row.mapIndexedNotNull { columnIndex, char ->
                        char.takeUnless { it == '.' }?.let { char to Position(rowIndex, columnIndex) }
                    }
                }.groupBy({ it.first }, { it.second }),
                width = input.first().length,
                height = input.size,
            )
        }
    }

    private fun antiNodesOf(position: Position, otherPosition: Position): List<Position> {
        val rowDiff = position.row - otherPosition.row
        val columnDiff = position.column - otherPosition.column
        return listOf(
            Position(position.row + rowDiff, position.column + columnDiff),
            Position(otherPosition.row - rowDiff, otherPosition.column - columnDiff),
        )
    }

    override fun part1(input: Input): Int {
        val antennaMap = AntennaMap(input)
        val antiNodes = antennaMap.flatMap { (_, positions) ->
            positions.subList(0, positions.size - 1).flatMapIndexed { index, firstPosition ->
                positions.subList(index + 1, positions.size).flatMap { secondPosition ->
                    antiNodesOf(firstPosition, secondPosition).filter { (row, column) ->
                        row in 0..<antennaMap.height && column in 0..<antennaMap.width
                    }
                }
            }
        }.toSet()
        return antiNodes.count()
    }

    override val part2Tests = listOf(
		Test(34, """
            ............
            ........0...
            .....0......
            .......0....
            ....0.......
            ......A.....
            ............
            ............
            ........A...
            .........A..
            ............
            ............
        """.trimIndent()),
	)

    private fun antiNodesHarmonicsOf(firstPosition: Position, secondPosition: Position, maxRow: Int, maxColumn: Int): List<Position> {
        val rowDiff = firstPosition.row - secondPosition.row
        val columnDiff = firstPosition.column - secondPosition.column
        return buildList {
            var row = firstPosition.row
            var column = firstPosition.column
            do {
                add(Position(row, column))
                row += rowDiff
                column += columnDiff
            } while (row in maxRow.rangeInclusive && column in maxColumn.rangeInclusive)
            row = secondPosition.row
            column = secondPosition.column
            do {
                add(Position(row, column))
                row -= rowDiff
                column -= columnDiff
            } while (row in maxRow.rangeInclusive && column in maxColumn.rangeInclusive)
        }
    }

    override fun part2(input: Input): Int {
        val antennaMap = AntennaMap(input)
        val antiNodes = buildSet {
            for (positions in antennaMap.values) {
                for ((index, firstPosition) in positions.subList(0, positions.size - 1).withIndex()) {
                    for (secondPosition in positions.subList(index + 1, positions.size)) {
                        addAll(antiNodesHarmonicsOf(firstPosition, secondPosition, antennaMap.height - 1, antennaMap.width - 1))
                    }
                }
            }
        }
        val map = List(antennaMap.height) {
            MutableList(antennaMap.width) { '.' }
        }
        antiNodes.forEach { position ->
            map[position.row][position.column] = '#'
        }
        antennaMap.forEach { (frequency, positions) ->
            positions.forEach { position ->
                map[position.row][position.column] = frequency
            }
        }
        map.forEach {
            println(it.joinToString(""))
        }
        return antiNodes.count()
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
