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

    override fun part1(input: Input): Long {
        val expandedImage = buildList {
            input.forEach {
                val row = it.replace('.', ' ')
                add(row.toMutableList())
                if (row.isBlank()) {
                    add(row.toMutableList())
                }
            }
        }.also { image ->
            var columnIndex = 0
            while (columnIndex < image.first().size) {
                var blankColumn = true
                for (rowIndex in image.indices) {
                    if (image[rowIndex][columnIndex] == '#') {
                        blankColumn = false
                        break
                    }
                }
                if (blankColumn) {
                    columnIndex++
                    image.onEach { row ->
                        row.add(columnIndex, ' ')
                    }
                }
                columnIndex++
            }
        }

        val galaxyPositions = buildList {
            expandedImage.forEachIndexed { rowIndex, row ->
                row.forEachIndexed { columnIndex, dataPoint ->
                    if (dataPoint == '#') {
                        add(Position(rowIndex.toLong(), columnIndex.toLong()))
                    }
                }
            }
        }

        return galaxyPositions.sumOf { first ->
            galaxyPositions.sumOf { second -> first.manhattanDistance(second) }
        } / 2
    }

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

    override fun part2(input: Input): Long {
        val scale = 1_000_000L
        val galaxyPositions = mutableListOf<Position>()
        val emptyRowIndices = mutableListOf<Int>()
        val emptyColumnIndices = mutableListOf<Int>()
        input.first().indices.forEach { columnIndex ->
            val emptyColumn = input.indices.none { rowIndexAgain -> input[rowIndexAgain][columnIndex] == '#' }
            if (emptyColumn) {
                emptyColumnIndices += columnIndex
            }
        }
        input.forEachIndexed { rowIndex, row ->
            var emptyRow = true
            val verticalSpace = emptyRowIndices.size
            row.forEachIndexed { columnIndex, dataPoint ->
                if (dataPoint == '#') {
                    val horizontalSpace = emptyColumnIndices.count { it < columnIndex }
                    galaxyPositions += Position(
                        rowIndex - verticalSpace + scale * verticalSpace,
                        columnIndex - horizontalSpace + scale * horizontalSpace
                    )
                    emptyRow = false
                }
            }
            if (emptyRow) {
                emptyRowIndices += rowIndex
            }
        }

        return galaxyPositions.sumOf { first ->
            galaxyPositions.sumOf { second -> first.manhattanDistance(second) }
        } / 2
    }

    private data class Position(val row: Long, val column: Long) {
        fun manhattanDistance(other: Position): Long =
            (row - other.row).absoluteValue + (column - other.column).absoluteValue
    }

    @JvmStatic
    fun main(args: Array<String>) {
        testPart1()
        runPart1()

        testPart2()
        runPart2()
    }
}
