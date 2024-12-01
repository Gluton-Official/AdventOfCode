package aoc2023

import AoCPuzzle
import util.Input
import util.zipWithIndexed

object Day15 : AoCPuzzle() {
    override val part1Tests = listOf(
        Test(1320, """
            rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7
        """.trimIndent()),
	)

    override fun part1(input: Input): Int = input.first().split(',').sumOf(::hash)

    override val part2Tests = listOf(
        Test(145, """
            rn=1,cm-,qp=3,cm=2,qp-,pc=4,ot=9,ab=5,pc-,pc=6,ot=7
        """.trimIndent()),
	)

    override fun part2(input: Input): Int {
        val boxes = buildMap<Int, MutableMap<String, Int>> {
            input.first().split(',').forEach { step ->
                when {
                    step.contains('=') -> {
                        val (label, focalLength) = step.split('=')
                        getOrPut(hash(label), ::mutableMapOf)[label] = focalLength.toInt()
                    }
                    step.contains('-') -> {
                        val (label) = step.split('-')
                        get(hash(label))?.remove(label)
                    }
                }
            }
        }
        return boxes.entries.fold(0) { acc, (index, lenses) ->
            acc + lenses.values.zipWithIndexed(index + 1) { slotIndex, (focalLength, box) ->
                box * (slotIndex + 1) * focalLength
            }.sum()
        }
    }

    private fun hash(value: String): Int = value.fold(0) { acc, char -> ((acc + char.code) * 17) % 256 }

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
