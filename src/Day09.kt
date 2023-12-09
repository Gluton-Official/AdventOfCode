
object Day09 : AoCPuzzle() {
    override val part1Tests = listOf(
		Test(114, """
            0 3 6 9 12 15
            1 3 6 10 15 21
            10 13 16 21 30 45
        """.trimIndent()),
	)

    override fun part1(input: Input): Int = input.map {
        it.split(' ').map(String::toInt)
    }.sumOf {
        val sequences = mutableListOf(it.toMutableList())
        while (!sequences.last().all { it == 0 }) {
            sequences += sequences.last().zipWithNext { a, b -> b - a }.toMutableList()
        }
        for (sequenceIndex in sequences.indices.reversed()) {
            val sequence = sequences[sequenceIndex]
            if (sequenceIndex == sequences.indices.last) {
                sequence += 0
            } else {
                sequence += sequence.last() + sequences[sequenceIndex + 1].last()
            }
        }
        sequences.first().last()
    }

    override val part2Tests = listOf(
		Test(2, """
            0 3 6 9 12 15
            1 3 6 10 15 21
            10 13 16 21 30 45
        """.trimIndent()),
	)

    override fun part2(input: Input): Int = input.map {
        it.split(' ').map(String::toInt)
    }.sumOf {
        val sequences = mutableListOf(it.toMutableList())
        while (!sequences.last().all { it == 0 }) {
            sequences += sequences.last().zipWithNext { a, b -> b - a }.toMutableList()
        }
        for (sequenceIndex in sequences.indices.reversed()) {
            val sequence = sequences[sequenceIndex]
            if (sequenceIndex == sequences.indices.last) {
                sequence.add(0, 0)
            } else {
                sequence.add(0, sequence.first() - sequences[sequenceIndex + 1].first())
            }
        }
        sequences.first().first()
    }

    @JvmStatic
    fun main(args: Array<String>) {
        testPart1()
        runPart1()

        testPart2()
        runPart2()
    }
}
