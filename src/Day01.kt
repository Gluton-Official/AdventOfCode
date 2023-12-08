
object Day01 : AoCPuzzle() {
    override val part1Test: Test
        get() = Test(142, """
            1abc2
            pqr3stu8vwx
            a1b2c3d4e5f
            treb7uchet
        """.trimIndent())

    override fun part1(input: List<String>): Int = input.sumOf {
        "${it.first(Char::isDigit)}${it.last(Char::isDigit)}".toInt()
    }

    override val part2Test: Test
        get() = Test(281, """
            two1nine
            eightwothree
            abcone2threexyz
            xtwone3four
            4nineeightseven2
            zoneight234
            7pqrstsixteen
        """.trimIndent())

    override fun part2(input: List<String>): Int = input.sumOf {
        listOf(it.findAnyOf(digitStrings), it.findLastAnyOf(digitStrings))
            .map { it!!.second }
            .joinToString("") { wordToDigit[it] ?: it }
            .toInt()
    }

    private val wordToDigit = listOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")
        .zip((1..9).map(Int::toString)).toMap()
    private val digitStrings = wordToDigit.keys + wordToDigit.values

    @JvmStatic
    fun main(args: Array<String>) {
        testPart1()
        runPart1()

        testPart2()
        runPart2()
    }
}
