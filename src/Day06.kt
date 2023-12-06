
object Day06 : AoCPuzzle() {
    override val part1Test: Test
        get() = Test(288, """
            Time:      7  15   30
            Distance:  9  40  200
        """.trimIndent())

    override fun part1(input: List<String>): Int {
        val times = input.find { it.startsWith("Time") }!!.substringAfter("Time:").trim().split("\\s+".toRegex()).map(String::toInt)
        val records = input.find { it.startsWith("Distance") }!!.substringAfter("Distance:").trim().split("\\s+".toRegex()).map(String::toInt)
        val races = times.zip(records)
        return races.map { it.buttonHoldTimes().count() }.reduce { acc, c -> acc * c }
    }

    override val part2Test: Test
        get() = Test(71503L, """
            Time:      7  15   30
            Distance:  9  40  200
        """.trimIndent())

    override fun part2(input: List<String>): Long {
        val time = input.find { it.startsWith("Time") }!!.substringAfter("Time:").replace("\\s+".toRegex(), "").toLong()
        val record = input.find { it.startsWith("Distance") }!!.substringAfter("Distance:").replace("\\s+".toRegex(), "").toLong()
        val race = time to record
        return race.winWays()
    }

    private fun Pair<Long, Long>.winWays(): Long {
        val (raceDuration, record) = this
        val minHoldTime = (0..raceDuration).first { buttonHoldTime ->
            time(buttonHoldTime, raceDuration) > record
        }
        val maxHoldTime = (0..raceDuration).reversed().first { buttonHoldTime ->
            time(buttonHoldTime, raceDuration) > record
        }
        return maxHoldTime - minHoldTime + 1
    }

    private fun time(buttonHoldTime: Long, raceDuration: Long): Long {
        val speed = buttonHoldTime
        val driveDuration = raceDuration - buttonHoldTime
        return driveDuration * speed
    }

    private fun Pair<Int, Int>.buttonHoldTimes(): List<Int> {
        val (raceTime, record) = this
        return buildList {
            for (holdTime in 0..raceTime) {
                val speed = holdTime
                val driveDuration = raceTime - holdTime
                val time = driveDuration * speed
                if (time > record) {
                    add(time)
                }
            }
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        testPart1()
        testPart2()

        runPart1()
        runPart2()
    }
}
