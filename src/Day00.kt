import util.Input

object Day00 : AoCPuzzle() {
    override val part1Tests = listOf(
		Test(0, """
            
        """.trimIndent()),
	)

    override fun part1(input: Input): Int {
        return 0
    }

    override val part2Tests = listOf(
		Test(0, """
            
        """.trimIndent()),
	)

    override fun part2(input: Input): Int {
        return 0
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
