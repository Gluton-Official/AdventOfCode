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
        val mul = """mul\((\d{1,3}),(\d{1,3})\)""".toRegex()
        return input.sumOf {
            mul.findAll(it).map {
                it.groupValues.drop(1).let { (a, b) -> a.toInt() * b.toInt() }
            }.sum()
        }
    }

    override val part2Tests = listOf(
		Test(48, """
            xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))
        """.trimIndent()),
	)

    override fun part2(input: Input): Int {
        val mul = """mul\((\d{1,3}),(\d{1,3})\)""".toRegex()
        val enable = """do\(\)""".toRegex()
        val disable = """don't\(\)""".toRegex()
        var enabled = true
        return input.sumOf {
            var sum = 0
            var pos = 0
            while (pos < it.length) {
                val next = listOf(
                    enable.find(it, pos),
                    disable.find(it, pos),
                    mul.find(it, pos)
                ).minBy { it?.range?.first ?: Int.MAX_VALUE } ?: break

                when {
                    next.value.startsWith("don't") -> enabled = false
                    next.value.startsWith("do") -> enabled = true
                    enabled -> sum += next.groupValues.drop(1).let { (a, b) -> a.toInt() * b.toInt() }
                }
                pos = next.range.last
            }
            sum
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
