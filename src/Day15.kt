import util.Input
import util.zipWithIndexed
import kotlin.streams.toList

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
        val boxes = (0..<256).associateWith { mutableMapOf<String, Int>() }.toMutableMap()

        input.first().split(',').forEach { step ->
            when {
                step.contains('=') -> {
                    val (label, focalLength) = step.split('=')
                    boxes[hash(label)]!![label] = focalLength.toInt()
                }
                step.contains('-') -> {
                    val (label) = step.split('-')
                    boxes[hash(label)]!! -= label
                }
            }
        }

        return boxes.entries.fold(0) { acc, (index, lenses) ->
            acc + lenses.values.zipWithIndexed(index + 1) { slotIndex, (focalLength, box) ->
                box * (slotIndex + 1) * focalLength
            }.sum()
        }
    }

    private fun hash(step: String): Int = step.chars().toList().fold(0) { acc, char -> ((acc + char) * 17) % 256 }

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
