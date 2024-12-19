package aoc2024

import AoCPuzzle
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.IO_PARALLELISM_PROPERTY_NAME
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.selects.select
import util.Input
import util.mapParallel
import util.parallel
import util.rangeInclusive
import kotlin.coroutines.CoroutineContext
import kotlin.math.pow
import kotlin.test.assertEquals
import kotlin.test.todo

object Day17 : AoCPuzzle() {
    override val part1Tests = listOf(
		Test(0, """
            Register A: 729
            Register B: 0
            Register C: 0

            Program: 0,1,5,4,3,0
        """.trimIndent()),
	)

    private class Computer(
        var registerA: Long,
        var registerB: Long,
        var registerC: Long,
        val program: List<Int>
    ) {
        var instructionPointer: Int = 0

        val output = mutableListOf<Int>()

        private fun incrementInstructionPointer() { instructionPointer += 2 }

        val comboOperands = mapOf(
            0 to { 0L },
            1 to { 1L },
            2 to { 2L },
            3 to { 3L },
            4 to { registerA },
            5 to { registerB },
            6 to { registerC },
            7 to { error("Reserved") },
        )

        val instructions = mapOf(
            /* adv */ 0 to { operand: Int ->
                registerA = (registerA / 2.0.pow(comboOperands[operand]!!().toDouble())).toLong()
                incrementInstructionPointer()
            },
            /* bxl */ 1 to { operand: Int ->
                registerB = registerB.xor(operand.toLong())
                incrementInstructionPointer()
            },
            /* bst */ 2 to { operand: Int ->
                registerB = comboOperands[operand]!!().mod(8L)
                incrementInstructionPointer()
            },
            /* jnz */ 3 to { operand: Int ->
                if (registerA != 0L) {
                    instructionPointer = operand
                } else {
                    incrementInstructionPointer()
                }
            },
            /* bxc */ 4 to { operand: Int ->
                registerB = registerB.xor(registerC)
                incrementInstructionPointer()
            },
            /* out */ 5 to { operand: Int ->
                output += comboOperands[operand]!!().mod(8)
                incrementInstructionPointer()
            },
            /* bdv */ 6 to { operand: Int ->
                registerB = (registerA / 2.0.pow(comboOperands[operand]!!().toDouble())).toLong()
                incrementInstructionPointer()
            },
            /* cdv */ 7 to { operand: Int ->
                registerC = (registerA / 2.0.pow(comboOperands[operand]!!().toDouble())).toLong()
                incrementInstructionPointer()
            },
        )

        fun executeProgram(): String {
            while (instructionPointer < program.size - 1) {
                val opcode = program[instructionPointer]
                val instruction = instructions[opcode]!!
                instruction(program[instructionPointer + 1])
            }
            return output.joinToString(",")
        }

        fun clear() {
            registerA = 0
            registerB = 0
            registerC = 0
            instructionPointer = 0
            output.clear()
        }

        companion object {
            fun from(input: List<String>): Computer {
                val (a, b, c) = input.take(3).map { it.substringAfterLast(' ').toLong() }
                val program = input.last().substringAfterLast(' ').split(',').map(String::toInt)
                return Computer(a, b, c, program)
            }
        }
    }

    override fun part1(input: Input): Int {
        println(Computer.from(input).executeProgram())
        return 0
    }

    override val part2Tests = listOf(
		Test(117440L, """
            Register A: 2024
            Register B: 0
            Register C: 0

            Program: 0,3,5,4,3,0
        """.trimIndent()),
	)

    override fun part2(input: Input): Long {
        val computer = Computer.from(input)
        val targetProgram = computer.program.map(Int::toLong)

        // 2,4,1,3,7,5,4,2,0,3,1,5,5,5,3,0
        val supposedRegisterA = computer.program.asReversed().foldIndexed(0L) { index, previousRegisterA, instruction ->
            var registerA = previousRegisterA * 8
            while (true) {
                // compressed program, outputs the value of register B for a given value of register A
                val cycle = (((registerA and 7 xor 3 xor (registerA shr (registerA and 7 xor 3).toInt())) xor 5) and 7).toInt()
                if (cycle == instruction) {
                    computer.clear()
                    computer.registerA = registerA
                    val result = computer.executeProgram()
                    if (targetProgram.takeLast(index + 1) == result.split(',').map(String::toLong)) break
                }
                registerA++
            }
            registerA
        }.println()

        computer.registerA = supposedRegisterA
//        computer.registerA = 236555995274861
        val result = computer.executeProgram()
        assertEquals(targetProgram, result.split(',').map(String::toLong))

        return 0
    }

    @JvmStatic
    fun main(args: Array<String>) {
        if (testPart1()) {
            runPart1()
        }

//        if (testPart2()) {
            runPart2()
//        }
    }
}
