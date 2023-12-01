fun main() {
    fun part1(input: List<String>): Int =
        input.map { it.first { it.isDigit() }.toString() + it.last { it.isDigit() }.toString() }.sumOf { it.toInt() }

    fun part2(input: List<String>): Int {
        val digits = listOf(
            "one",
            "two",
            "three",
            "four",
            "five",
            "six",
            "seven",
            "eight",
            "nine"
        )
        val allDigits = digits + (1..9).toList().map { it.toString() }
        val digitMap = digits.zip((1..9).toList().map { it.toString() }).toMap()

        return input.sumOf {
            (it.findAnyOf(allDigits)!!.let { (_, s) ->
                digitMap.getOrDefault(s, s)
            } + it.findLastAnyOf(allDigits)!!.let { (_, s) ->
                digitMap.getOrDefault(s, s)
            }).toInt()
        }
    }

    // test if implementation meets criteria from the description, like:
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
