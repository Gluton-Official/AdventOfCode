fun main() {
    fun part1(input: List<String>): Int = input.map {
        it.first(Char::isDigit).toString() + it.last(Char::isDigit).toString()
    }.sumOf(String::toInt)

    fun part2(input: List<String>): Int {
        val digits = listOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")
            .withIndex().associate { (i, v) -> v to (i + 1).toString() }
        val values = digits.keys + digits.values

        return input.sumOf {
            listOf(it.findAnyOf(values), it.findLastAnyOf(values))
                .map { it!!.second }
                .joinToString("") { digits[it] ?: it }
                .toInt()
        }
    }

    var testInput = """
        1abc2
        pqr3stu8vwx
        a1b2c3d4e5f
        treb7uchet
    """.trimIndent().lines()
    check(part1(testInput) == 142)

    testInput = """
        two1nine
        eightwothree
        abcone2threexyz
        xtwone3four
        4nineeightseven2
        zoneight234
        7pqrstsixteen
    """.trimIndent().lines()
    check(part2(testInput) == 281)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
