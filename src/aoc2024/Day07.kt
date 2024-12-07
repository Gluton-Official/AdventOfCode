package aoc2024

import AoCPuzzle
import util.Input

object Day07 : AoCPuzzle() {
    override val part1Tests = listOf(
		Test(3749L, """
            190: 10 19
            3267: 81 40 27
            83: 17 5
            156: 15 6
            7290: 6 8 6 15
            161011: 16 10 13
            192: 17 8 14
            21037: 9 7 18 13
            292: 11 6 16 20
        """.trimIndent()),
	)

    override fun part1(input: Input): Long {
        val equations = input.map(Equation::from)
        fun testSubList(target: Long, runningTotal: Long, numbers: List<Long>): Boolean = when {
            numbers.isEmpty() -> target == runningTotal
            testSubList(target, runningTotal + numbers.first(), numbers.drop(1)) -> true
            testSubList(target, runningTotal * numbers.first(), numbers.drop(1)) -> true
            else -> false
        }
        return equations.filter { (testResult, numbers) ->
            testSubList(testResult, 0, numbers)
        }.sumOf { it.testResult }
    }

    private data class Equation(val testResult: Long, val numbers: List<Long>) {
        companion object {
            fun from(string: String): Equation {
                val testResult = string.substringBefore(':').toLong()
                val numbers = string.substringAfter(':').trim().split(' ').map(String::toLong)
                return Equation(testResult, numbers)
            }
        }
    }

    override val part2Tests = listOf(
		Test(11387L, """
            190: 10 19
            3267: 81 40 27
            83: 17 5
            156: 15 6
            7290: 6 8 6 15
            161011: 16 10 13
            192: 17 8 14
            21037: 9 7 18 13
            292: 11 6 16 20
        """.trimIndent()),
	)

    override fun part2(input: Input): Long {
        val equations = input.map(Equation::from)
        fun testSubList(target: Long, runningTotal: Long, numbers: List<Long>): Boolean {
            if (numbers.isEmpty()) return target == runningTotal
            return when {
                testSubList(target, runningTotal + numbers.first(), numbers.drop(1)) -> true
                testSubList(target, runningTotal * numbers.first(), numbers.drop(1)) -> true
                testSubList(target, (runningTotal.toString() + numbers.first().toString()).toLong(), numbers.drop(1)) -> true
                else -> false
            }
        }
        return equations.filter { (testResult, numbers) ->
            testSubList(testResult, 0, numbers)
        }.sumOf { it.testResult }
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
