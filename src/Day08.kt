import util.Input
import util.cyclicIterator

object Day08 : AoCPuzzle() {
    override val part1Tests = listOf(
        Test(2, """
            RL

            AAA = (BBB, CCC)
            BBB = (DDD, EEE)
            CCC = (ZZZ, GGG)
            DDD = (DDD, DDD)
            EEE = (EEE, EEE)
            GGG = (GGG, GGG)
            ZZZ = (ZZZ, ZZZ)
        """.trimIndent()),
        Test(6, """
            LLR

            AAA = (BBB, BBB)
            BBB = (AAA, ZZZ)
            ZZZ = (ZZZ, ZZZ)
        """.trimIndent()),
    )

    override fun part1(input: Input): Int = NetworkMap(input).run {
        val directions = directions.cyclicIterator()
        var steps = 0
        var current = "AAA"
        do {
            current = nodes[current]!!.run {
                when (directions.next()) {
                    Direction.L -> first
                    Direction.R -> second
                }
            }
            steps++
        } while (current != "ZZZ")
        return steps
    }

    override val part2Tests = listOf(
        Test(6L, """
            LR

            11A = (11B, XXX)
            11B = (XXX, 11Z)
            11Z = (11B, XXX)
            22A = (22B, XXX)
            22B = (22C, 22C)
            22C = (22Z, 22Z)
            22Z = (22B, 22B)
            XXX = (XXX, XXX)
        """.trimIndent()),
    )

    override fun part2(input: Input): Long = NetworkMap(input).run {
        fun findCycle(startKey: String): Long {
            val directions = directions.cyclicIterator()
            var steps = 0L
            var current = startKey
            do {
                current = nodes[current]!!.run {
                    when (directions.next()) {
                        Direction.L -> first
                        Direction.R -> second
                    }
                }
                steps++
            } while (!current.endsWith('Z'))
            return steps
        }

        val starts = nodes.keys.filter { it.endsWith('A') }
        return starts.map(::findCycle).reduce(::lcm)
    }

    private fun lcm(a: Long, b: Long): Long = a * b / gcd(a, b)

    private fun gcd(a: Long, b: Long): Long {
        var (a, b) = a to b
        var temp: Long
        while (b != 0L) {
            temp = b
            b = a % b
            a = temp
        }
        return a
    }

    private data class NetworkMap(val directions: List<Direction>, val nodes: Map<String, Pair<String, String>>) {
        companion object {
            val nodeRegex = Regex("(?<key>\\w{3}) = \\((?<left>\\w{3}), (?<right>\\w{3})\\)")

            operator fun invoke(input: List<String>): NetworkMap = input.run {
                val directions: List<Direction> = first().toCharArray().map(Char::toString).map(::enumValueOf)
                val nodes = drop(1).dropWhile(String::isBlank).associate {
                    val (key, left, right) = nodeRegex.find(it)!!.destructured
                    key to (left to right)
                }
                return NetworkMap(directions, nodes)
            }
        }
    }

    private enum class Direction { R, L }

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
