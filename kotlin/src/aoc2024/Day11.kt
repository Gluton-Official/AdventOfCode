package aoc2024

import AoCPuzzle
import jdk.internal.vm.vector.VectorSupport.test
import util.Input
import kotlin.div
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow
import kotlin.rem

object Day11 : AoCPuzzle() {
    override val part1Tests = listOf(
        Test(
            55312L, """
            125 17
        """.trimIndent()
        ),
    )

    override fun part1(input: Input): Long {
        var stones = input.single().split(" ").map(String::toLong)
        return countStones(blink(25, stones))
    }

    private fun naivePart1(input: Input): Int {
        var stones = input.single().split(" ").map(String::toLong)
        repeat(25) {
            stones = stones.flatMap(::naiveBlinkStone)
        }
        return stones.size
    }

    private fun naiveBlinkStone(engravedValue: Long): List<Long> = when (engravedValue) {
        0L -> listOf(1)
        else if engravedValue.toString().length % 2 == 0 -> engravedValue.toString().let {
            val half = it.length / 2
            listOf(it.substring(0, half).toLong(), it.substring(half).toLong())
        }
        else -> listOf(engravedValue * 2024)
    }

    override val part2Tests = listOf(
        Test(
            65601038650482L, """
            125 17
        """.trimIndent()
        ),
    )

    override fun part2(input: Input): Long {
        var stones = input.single().split(" ").map(String::toLong)
        return countStones(blink(75, stones))
    }

    private fun countStones(stones: Map<Long, Long>): Long = stones.values.sum()

    private fun blink(times: Int, stones: List<Long>): Map<Long, Long> {
        var stones = stones.groupingBy { it }.eachCount().mapValues { it.value.toLong() }
        repeat(times) {
            stones = mutableMapOf<Long, Long>().apply {
                stones.forEach { (engravingValue, stonesCount) ->
                    blinkStone(engravingValue).forEach {
                        put(it, getOrDefault(it, 0) + stonesCount)
                    }
                }
            }
        }
        return stones
    }

    private fun blinkStone(engravedValue: Long): List<Long> {
        if (engravedValue == 0L) return listOf(1)
        val digits = (log10(engravedValue.toDouble()) + 1).toInt()
        return when (digits % 2) {
            0 -> 10f.pow(digits / 2).toInt().let { listOf(engravedValue / it, engravedValue % it) }
            else -> listOf(engravedValue * 2024)
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
