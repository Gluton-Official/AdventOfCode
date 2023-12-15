import util.Input
import util.columnStrings
import util.lengthExclusive
import util.transposedStrings

object Day14 : AoCPuzzle() {
    override val part1Tests = listOf(
		Test(136, """
            O....#....
            O.OO#....#
            .....##...
            OO.#O....O
            .O.....O#.
            O.#..O.#.#
            ..O..#O..O
            .......O..
            #....###..
            #OO..#....
        """.trimIndent()),
	)

    override fun part1(input: Input): Int = input.transposedStrings().let(::tilt).let(::calculateLoad)

    override val part2Tests = listOf(
		Test(64, """
            O....#....
            O.OO#....#
            .....##...
            OO.#O....O
            .O.....O#.
            O.#..O.#.#
            ..O..#O..O
            .......O..
            #....###..
            #OO..#....
        """.trimIndent()),
	)

    override fun part2(input: Input): Int {
        val cycles = 1_000_000_000
        val previousCycles = mutableListOf<Int>()
        var cycleRange: IntRange? = null
        var currentDish = input
        var cycle = 0
        while (cycle < cycles) {
            val uid = uniqueIdentifierOf(currentDish)
            val cycleLoopStart = previousCycles.indexOfFirst { it == uid }.takeUnless { it == -1 }
            if (cycleLoopStart != null) {
                cycleRange = cycleLoopStart..cycle
                break
            }
            previousCycles += uid
            currentDish = tiltCycle(currentDish)
            cycle++
        }
        val remainingCycles = (cycles - cycleRange!!.last) % cycleRange.lengthExclusive
        repeat(remainingCycles) {
            currentDish = tiltCycle(currentDish)
        }
        return calculateLoad(currentDish.columnStrings())
    }

    private fun calculateLoad(dish: List<String>): Int = dish.sumOf {
        it.foldIndexed<Int>(0) { index, load, rock ->
            when (rock) {
                'O' -> load + (it.length - index)
                else -> load
            }
        }
    }

    private fun tiltCycle(dish: List<String>): List<String> =
        dish.transposedStrings().let(::tilt) // north
            .transposedStrings().let(::tilt) // west
            .transposedStrings().map(String::reversed).let(::tilt) // south
            .transposedStrings().map(String::reversed).let(::tilt) // east
            .map(String::reversed).transposedStrings().map(String::reversed).transposedStrings() // original rotation (west)

    private fun tilt(dish: List<String>): List<String> = dish.map {
        var lastRockIndex = 0
        buildString {
            for ((index, rock) in it.withIndex()) {
                when (rock) {
                    'O' -> {
                        append('O')
                        lastRockIndex++
                    }
                    '#' -> {
                        append(".".repeat(index - lastRockIndex))
                        append('#')
                        lastRockIndex = index + 1
                    }
                }
            }
            append(".".repeat(it.length - lastRockIndex))
        }
    }

    private fun uniqueIdentifierOf(dish: List<String>): Int =
        dish.joinToString(",") { it.hashCode().toString() }.hashCode()

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
