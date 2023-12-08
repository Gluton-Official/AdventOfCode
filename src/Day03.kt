
object Day03 : AoCPuzzle() {
    override val part1Test: Test
        get() = Test(4361, """
            467..114..
            ...*......
            ..35..633.
            ......#...
            617*......
            .....+.58.
            ..592.....
            ......755.
            ...${'$'}.*....
            .664.598..
        """.trimIndent())

    override fun part1(input: List<String>): Int = Schematic(input).run {
        cells.sumOf { row ->
            row.filterIsInstance<Symbol>().sumOf { symbol ->
                symbol.adjacentCells()
                    .filterIsInstance<PartNumber>()
                    .distinct()
                    .sumOf(PartNumber::value)
            }
        }
    }

    override val part2Test: Test
        get() = Test(467835, """
            467..114..
            ...*......
            ..35..633.
            ......#...
            617*......
            .....+.58.
            ..592.....
            ......755.
            ...${'$'}.*....
            .664.598..
        """.trimIndent())

    override fun part2(input: List<String>): Int = Schematic(input).run {
        cells.sumOf { row -> row.asSequence()
            .filterIsInstance<Symbol>()
            .filter(Symbol::isGear)
            .sumOf { gear ->
                gear.adjacentCells()
                    .filterIsInstance<PartNumber>()
                    .distinct()
                    .map(PartNumber::value)
                    .zipWithNext()
                    .singleOrNull()
                    ?.run { first * second }
                    ?: 0
            }
        }
    }

    private data class Schematic(val cells: List<List<Cell>>) {
        fun Symbol.adjacentCells() = sequence {
            for (row in (row - 1)..(row + 1)) {
                for (column in (column - 1)..(column + 1)) {
                    if (row == this@adjacentCells.row && column == this@adjacentCells.column) continue
                    cells.getOrNull(row)?.getOrNull(column)?.let { yield(it) }
                }
            }
        }

        companion object {
            operator fun invoke(input: List<String>) = Schematic(
                input.mapIndexed { rowIndex, row ->
                    buildList {
                        row.consumeIndexed { columnIndex, row, cell ->
                            when {
                                cell.isDigit() -> row.takeWhile(Char::isDigit).let {
                                    val partNumber = PartNumber(it.toInt())
                                    repeat(it.length) { add(partNumber) }
                                    row.drop(it.length)
                                }
                                cell == '.' -> row.takeWhile { it == '.' }.count().let {
                                    repeat(it) { add(Empty) }
                                    row.drop(it)
                                }
                                else -> {
                                    add(Symbol(cell, rowIndex, columnIndex))
                                    row.drop(1)
                                }
                            }
                        }
                    }
                }
            )
        }
    }

    private sealed interface Cell
    private data class PartNumber(val value: Int) : Cell
    private data class Symbol(val value: Char, val row: Int, val column: Int) : Cell {
        fun isGear(): Boolean = value == '*'
    }
    private data object Empty : Cell

    @JvmStatic
    fun main(args: Array<String>) {
        testPart1()
        runPart1()

        testPart2()
        runPart2()
    }
}
