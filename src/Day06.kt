
object Day06 : AoCPuzzle() {
    override val part1Tests = listOf(
	    Test(288L, """
            Time:      7  15   30
            Distance:  9  40  200
        """.trimIndent()),
    )

    override fun part1(input: Input): Long = input.map {
        it.substringAfter(':').trim().split("\\s+".toRegex()).map(String::toLong)
    }.zipWithNext().single().run { first.zip(second) }.map { (duration, recordDistance) ->
        Race(duration, recordDistance).winningHoldTimes().lengthInclusive
    }.reduce { acc, c -> acc * c }

    override val part2Tests = listOf(
		Test(71503L, """
            Time:      7  15   30
            Distance:  9  40  200
        """.trimIndent()),
	)

    override fun part2(input: Input): Long = input.map {
        it.substringAfter(':').replace("\\s+".toRegex(), "").toLong()
    }.zipWithNext().single().let { (duration, recordDistance) ->
        Race(duration, recordDistance).winningHoldTimes().lengthInclusive
    }

    data class Race(val duration: Long, val recordDistance: Long) {
        fun winningHoldTimes(): LongRange = (0..duration) constrainWith { holdTime ->
            (duration - holdTime) * holdTime > recordDistance
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        testPart1()
        runPart1()

        testPart2()
        runPart2()
    }
}
