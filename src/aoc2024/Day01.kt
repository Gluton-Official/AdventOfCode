package aoc2024

import AoCPuzzle
import util.Input
import util.transposed
import kotlin.math.absoluteValue

object Day01 : AoCPuzzle() {
    override val part1Tests = listOf(
		Test(11, """
            3   4
            4   3
            2   5
            1   3
            3   9
            3   3
        """.trimIndent()),
	)

    override fun part1(input: Input): Int {
        val (left, right) = input.map {
            it.split(Regex("\\s+")).map(String::toInt)
        }.transposed().map { it.sorted() }
        return left.zip(right).sumOf { (a, b) -> (a - b).absoluteValue }
    }

    override val part2Tests = listOf(
		Test(31, """
            3   4
            4   3
            2   5
            1   3
            3   9
            3   3
        """.trimIndent()),
	)

    override fun part2(input: Input): Int {
        val (left, right) = input.map {
            it.split(Regex("\\s+")).map(String::toInt)
        }.transposed()
        val map = right.associateWith { target ->
            right.count { it == target }
        }
        return left.sumOf { a -> map.getOrDefault(a, 0) * a }
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
