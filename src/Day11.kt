import util.Input
import util.uniquePairs
import kotlin.math.absoluteValue

object Day11 : AoCPuzzle() {
    override val part1Tests = listOf(
		Test(374L, """
            ...#......
            .......#..
            #.........
            ..........
            ......#...
            .#........
            .........#
            ..........
            .......#..
            #...#.....
        """.trimIndent()),
	)

    override fun part1(input: Input): Long = galaxyPositions(input, scale = 2)
        .uniquePairs { first.manhattanDistance(second) }.sum()

    override val part2Tests = listOf(
		Test(82000210L, """
            ...#......
            .......#..
            #.........
            ..........
            ......#...
            .#........
            .........#
            ..........
            .......#..
            #...#.....
        """.trimIndent()),
	)

    override fun part2(input: Input): Long = galaxyPositions(input, scale = 1_000_000)
        .uniquePairs { first.manhattanDistance(second) }.sum()

    private fun galaxyPositions(input: Input, scale: Long): List<Position> {
        val voidRows = voidRows(input)
        val voidColumns = voidColumns(input)
        return input.flatMapIndexed { rowIndex, row ->
            val verticalVoids = voidRows.count { it < rowIndex }
            row.mapIndexedNotNull { columnIndex, dataPoint ->
                val horizontalVoids = voidColumns.count { it < columnIndex }
                when (dataPoint) {
                    '#' -> Position(
                        row = rowIndex - verticalVoids + scale * verticalVoids,
                        column = columnIndex - horizontalVoids + scale * horizontalVoids,
                    )
                    else -> null
                }
            }
        }
    }

    private fun voidRows(input: Input): List<Int> = buildList {
        input.forEachIndexed { rowIndex, row ->
            if ('#' !in row) add(rowIndex)
        }
    }

    private fun voidColumns(input: Input): List<Int> = buildList {
        input.indices.forEach { columnIndex ->
            if (input.none { row -> row[columnIndex] == '#' }) {
                add(columnIndex)
            }
        }
    }

    private data class Position(val row: Long, val column: Long) {
        fun manhattanDistance(other: Position): Long =
            (row - other.row).absoluteValue + (column - other.column).absoluteValue
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
