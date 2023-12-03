
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

    override fun part1(input: List<String>): Int {
        val schematic = readSchematic(input)
        var sum = 0
        schematic.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { columnIndex, cell ->
                if (cell is Cell.Symbol) {
                    try {
                        schematic[rowIndex - 1][columnIndex - 1].let {
                            if (it is Cell.PartNumber && !it.counted) {
                                sum += it.value
                                it.counted = true
                            }
                        }
                    } catch (_: IndexOutOfBoundsException) {}
                    try {
                        schematic[rowIndex][columnIndex - 1].let {
                            if (it is Cell.PartNumber && !it.counted) {
                                sum += it.value
                                it.counted = true
                            }
                        }
                    } catch (_: IndexOutOfBoundsException) {}
                    try {
                        schematic[rowIndex + 1][columnIndex - 1].let {
                            if (it is Cell.PartNumber && !it.counted) {
                                sum += it.value
                                it.counted = true
                            }
                        }
                    } catch (_: IndexOutOfBoundsException) {}
                    try {
                        schematic[rowIndex - 1][columnIndex].let {
                            if (it is Cell.PartNumber && !it.counted) {
                                sum += it.value
                                it.counted = true
                            }
                        }
                    } catch (_: IndexOutOfBoundsException) {}
                    try {
                        schematic[rowIndex + 1][columnIndex].let {
                            if (it is Cell.PartNumber && !it.counted) {
                                sum += it.value
                                it.counted = true
                            }
                        }
                    } catch (_: IndexOutOfBoundsException) {}
                    try {
                        schematic[rowIndex - 1][columnIndex + 1].let {
                            if (it is Cell.PartNumber && !it.counted) {
                                sum += it.value
                                it.counted = true
                            }
                        }
                    } catch (_: IndexOutOfBoundsException) {}
                    try {
                        schematic[rowIndex][columnIndex + 1].let {
                            if (it is Cell.PartNumber && !it.counted) {
                                sum += it.value
                                it.counted = true
                            }
                        }
                    } catch (_: IndexOutOfBoundsException) {}
                    try {
                        schematic[rowIndex + 1][columnIndex + 1].let {
                            if (it is Cell.PartNumber && !it.counted) {
                                sum += it.value
                                it.counted = true
                            }
                        }
                    } catch (_: IndexOutOfBoundsException) {}
                }
            }
        }
        return sum
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

    override fun part2(input: List<String>): Int {
        val schematic = readSchematic(input)
        var sum = 0
        schematic.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { columnIndex, cell ->
                if (cell is Cell.Symbol && cell.value == '*') {
                    val gears = mutableListOf<Cell.PartNumber>()
                    try {
                        schematic[rowIndex - 1][columnIndex - 1].let {
                            if (it is Cell.PartNumber && !it.counted) {
                                gears.add(it)
                                it.counted = true
                            }
                        }
                    } catch (_: IndexOutOfBoundsException) {}
                    try {
                        schematic[rowIndex][columnIndex - 1].let {
                            if (it is Cell.PartNumber && !it.counted) {
                                gears.add(it)
                                it.counted = true
                            }
                        }
                    } catch (_: IndexOutOfBoundsException) {}
                    try {
                        schematic[rowIndex + 1][columnIndex - 1].let {
                            if (it is Cell.PartNumber && !it.counted) {
                                gears.add(it)
                                it.counted = true
                            }
                        }
                    } catch (_: IndexOutOfBoundsException) {}
                    try {
                        schematic[rowIndex - 1][columnIndex].let {
                            if (it is Cell.PartNumber && !it.counted) {
                                gears.add(it)
                                it.counted = true
                            }
                        }
                    } catch (_: IndexOutOfBoundsException) {}
                    try {
                        schematic[rowIndex + 1][columnIndex].let {
                            if (it is Cell.PartNumber && !it.counted) {
                                gears.add(it)
                                it.counted = true
                            }
                        }
                    } catch (_: IndexOutOfBoundsException) {}
                    try {
                        schematic[rowIndex - 1][columnIndex + 1].let {
                            if (it is Cell.PartNumber && !it.counted) {
                                gears.add(it)
                                it.counted = true
                            }
                        }
                    } catch (_: IndexOutOfBoundsException) {}
                    try {
                        schematic[rowIndex][columnIndex + 1].let {
                            if (it is Cell.PartNumber && !it.counted) {
                                gears.add(it)
                                it.counted = true
                            }
                        }
                    } catch (_: IndexOutOfBoundsException) {}
                    try {
                        schematic[rowIndex + 1][columnIndex + 1].let {
                            if (it is Cell.PartNumber && !it.counted) {
                                gears.add(it)
                                it.counted = true
                            }
                        }
                    } catch (_: IndexOutOfBoundsException) {}
                    with(gears) {
                        if (size == 2) {
                            sum += first().value * last().value
                        }
                    }
                }
            }
        }
        return sum
    }

    private fun readSchematic(input: List<String>): List<List<Cell>> = input.map {
        buildList {
            var row = it
            do {
                val cell = row.first()
                when {
                    cell.isDigit() -> {
                        val num = row.takeWhile(Char::isDigit)
                        Cell.PartNumber(num.toInt()).apply {
                            repeat(num.length) { add(this) }
                        }
                        row = row.drop(num.length)
                    }
                    cell == '.' -> {
                        val blanks = row.takeWhile { it == '.' }.count()
                        repeat(blanks) {
                            add(Cell.Empty)
                        }
                        row = row.drop(blanks)
                    }
                    else -> {
                        add(Cell.Symbol(cell))
                        row = row.drop(1)
                    }
                }
            } while (row.isNotEmpty())
        }
    }

    sealed interface Cell {
        data class PartNumber(val value: Int, var counted: Boolean = false) : Cell
        data class Symbol(val value: Char) : Cell
        data object Empty : Cell
    }

    @JvmStatic
    fun main(args: Array<String>) {
        testPart1()
        testPart2()

        runPart1().println()
        runPart2().println()
    }
}
