package aoc2024

import AoCPuzzle
import util.Input

object Day03 : AoCPuzzle() {
    override val part1Tests = listOf(
		Test(161, """
            xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))
        """.trimIndent()),
	)


    override fun part1(input: Input): Int {
        val regex = """mul\((\d{1,3}),(\d{1,3})\)""".toRegex()
        return input.sumOf {
            regex.findAll(it).sumOf {
                it.destructured.let { (a, b) -> a.toInt() * b.toInt() }
            }
        }
    }

    override val part2Tests = listOf(
		Test(48, """
            xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))
        """.trimIndent()),
	)

    override fun part2(input: Input): Int {
        val regex = """mul\((\d{1,3}),(\d{1,3})\)|do\(\)|don't\(\)""".toRegex()
        var enabled = true
        return input.sumOf {
            regex.findAll(it).sumOf { match ->
                when (match.value) {
                    "do()" -> enabled = true
                    "don't()" -> enabled = false
                    else if enabled -> return@sumOf match.destructured.let { (a, b) -> a.toInt() * b.toInt() }
                }
                0
            }
        }
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
