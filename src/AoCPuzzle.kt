import java.io.File
import kotlin.test.assertEquals

abstract class AoCPuzzle(val day: Int) {
    private val name = "Day%02d".format(day)
    private val input: List<String> by lazy {
        ensureInputFile()
        readInput(name)
    }

    protected open val part1Test = Test()
    protected open val part2Test = Test()

    open fun part1(input: List<String>): Int = 0
    open fun part2(input: List<String>): Int = 0

    fun testPart1() = assertEquals(part1Test.expected, part1(part1Test.input.lines()))
    fun testPart2() = assertEquals(part2Test.expected, part2(part2Test.input.lines()))

    fun runPart1(): Int {
        ensureInputFile()
        return part1(input)
    }
    fun runPart2(): Int {
        ensureInputFile()
        return part2(input)
    }

    private fun ensureInputFile() {
        if (!File("resources/$name.txt").exists()) {
            fetchInput(day = day)
        }
    }

    protected data class Test(val expected: Int = 0, val input: String = "")
}
