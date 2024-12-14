package aoc2024

import AoCPuzzle
import util.Input
import util.Position
import util.Vector2
import kotlin.math.absoluteValue
import kotlin.math.floor
import kotlin.math.roundToInt
import kotlin.math.roundToLong

object Day13 : AoCPuzzle() {
    override val part1Tests = listOf(
		Test(480, """
            Button A: X+94, Y+34
            Button B: X+22, Y+67
            Prize: X=8400, Y=5400

            Button A: X+26, Y+66
            Button B: X+67, Y+21
            Prize: X=12748, Y=12176

            Button A: X+17, Y+86
            Button B: X+84, Y+37
            Prize: X=7870, Y=6450

            Button A: X+69, Y+23
            Button B: X+27, Y+71
            Prize: X=18641, Y=10279
        """.trimIndent()),
	)

    private const val EPSILON = 1e-3

    override fun part1(input: Input): Int {
        val machines = input.chunked(4).map { (buttonA, buttonB, prize) -> Machine.from(buttonA, buttonB, prize) }
        val tokens = machines.mapNotNull { machine ->
            val (prizeX, prizeY) = machine.prize.let { (x, y) -> x.toFloat() to y.toFloat() }
            val (buttonAX, buttonAY) = machine.buttonADistance.let { (x, y) -> x.toFloat() to y.toFloat() }
            val (buttonBX, buttonBY) = machine.buttonBDistance.let { (x, y) -> x.toFloat() to y.toFloat() }
            val buttonAPresses = (prizeY - buttonBY * prizeX / buttonBX) /  (buttonAY - buttonBY * buttonAX / buttonBX)
            val buttonBPresses = prizeX / buttonBX - (buttonAX / buttonBX) * (prizeY - buttonBY * prizeX / buttonBX) / (buttonAY - buttonBY * buttonAX / buttonBX)
            if ((buttonAPresses - buttonAPresses.roundToInt()).absoluteValue <= EPSILON && (buttonBPresses - buttonBPresses.roundToInt()) <= EPSILON) {
                buttonAPresses.roundToInt() * Machine.BUTTON_A_TOKENS + buttonBPresses.roundToInt() * Machine.BUTTON_B_TOKENS
            } else null
        }
        return tokens.sum()
    }

    private data class Machine(val buttonADistance: Vector2<Long>, val buttonBDistance: Vector2<Long>, val prize: Vector2<Long>) {
        companion object {
            const val BUTTON_A_TOKENS = 3
            const val BUTTON_B_TOKENS = 1

            private val buttonRegex = """Button [AB]: X\+(\d+), Y\+(\d+)""".toRegex()
            private val prizeRegex = """Prize: X=(\d+), Y=(\d+)""".toRegex()

            fun from(buttonA: String, buttonB: String, prize: String): Machine {
                val buttonADistance = buttonRegex.find(buttonA)!!.destructured.let { (x, y) -> Vector2(x.toLong(), y.toLong()) }
                val buttonBDistance = buttonRegex.find(buttonB)!!.destructured.let { (x, y) -> Vector2(x.toLong(), y.toLong()) }
                val prize = prizeRegex.find(prize)!!.destructured.let { (x, y) -> Vector2(x.toLong(), y.toLong()) }
                return Machine(buttonADistance, buttonBDistance, prize)
            }
        }
    }

    override val part2Tests = emptyList<Test>()

    override fun part2(input: Input): Long {
        val machines = input.chunked(4).map { (buttonA, buttonB, prize) -> Machine.from(buttonA, buttonB, prize) }
        val tokens = machines.mapNotNull { machine ->
            val (prizeX, prizeY) = machine.prize.let { (x, y) -> x.toDouble() + 10000000000000 to y.toDouble() + 10000000000000 }
            val (buttonAX, buttonAY) = machine.buttonADistance.let { (x, y) -> x.toDouble() to y.toDouble() }
            val (buttonBX, buttonBY) = machine.buttonBDistance.let { (x, y) -> x.toDouble() to y.toDouble() }
            val buttonAPresses = (prizeY - buttonBY * prizeX / buttonBX) /  (buttonAY - buttonBY * buttonAX / buttonBX)
            val buttonBPresses = prizeX / buttonBX - (buttonAX / buttonBX) * (prizeY - buttonBY * prizeX / buttonBX) / (buttonAY - buttonBY * buttonAX / buttonBX)
            if ((buttonAPresses - buttonAPresses.roundToLong()).absoluteValue <= EPSILON && (buttonBPresses - buttonBPresses.roundToLong()) <= EPSILON) {
                buttonAPresses.roundToLong() * Machine.BUTTON_A_TOKENS + buttonBPresses.roundToLong() * Machine.BUTTON_B_TOKENS
            } else null
        }
        return tokens.sum()
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
