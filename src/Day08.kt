
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
        var index = 0
        val directions = generateSequence {
            index %= directions.size
            directions[index++]
        }.iterator()
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
        val ends = nodes.keys.filter { it.endsWith('Z') }
        fun findCycle(startKey: String): Pair<Long, Map<String, Long?>> {
            var index = 0
            val directions = generateSequence {
                index %= directions.size
                directions[index++]
            }.iterator()
            val ends = ends.associateWith<String, Long?> { null }.toMutableMap()
            var steps = 0L
            var current = startKey
            while (true) {
                current = nodes[current]!!.run {
                    when (directions.next()) {
                        Direction.L -> first
                        Direction.R -> second
                    }
                }
                steps++
                if (current.endsWith('Z')) {
                    if (ends[current] == null) {
                        ends[current] = steps
                    } else {
                        ends[current] = steps - ends[current]!!
                        break
                    }
                }
            }
            return steps to ends
        }

        val starts = nodes.keys.filter { it.endsWith('A') }
        val cyclesForKey = starts.mapParallel { it to findCycle(it) }.toMap()

        val steps = cyclesForKey.values.map { it.first to it.second.values.find { it != null }!! }

        return steps.map { it.second }.reduce(::lcm)
    }

    private fun lcm(a: Long, b: Long): Long = a * b / gcd(a, b)

    private fun gcd(a: Long, b: Long): Long {
        var (a, b) = a to b
        var temp = 0L
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
        testPart1()
        runPart1()

        testPart2()
        runPart2()
    }
}
