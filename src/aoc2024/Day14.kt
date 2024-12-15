package aoc2024

import AoCPuzzle
import util.Input
import util.Position
import util.Vector2
import util.compareTo
import util.div
import util.get
import util.minus
import util.mod
import util.plus
import util.prettyToString2D
import util.rem
import util.set
import util.setAllAs
import util.times
import kotlin.math.log
import kotlin.math.log2

object Day14 : AoCPuzzle() {
    override val part1Tests = listOf(
        Test(0L, """
            p=2,4 v=2,-3
        """.trimIndent()),
		Test(12L, """
            p=0,4 v=3,-3
            p=6,3 v=-1,-3
            p=10,3 v=-1,2
            p=2,0 v=2,-1
            p=0,0 v=1,3
            p=3,0 v=-2,-2
            p=7,6 v=-1,-3
            p=3,0 v=-1,-2
            p=9,3 v=2,3
            p=7,3 v=-1,2
            p=2,4 v=2,-3
            p=9,5 v=-3,-3
        """.trimIndent()),
	)

    private data class Robot(val position: Vector2<Long>, val velocity: Vector2<Long>) {
        companion object {
            private val inputRegex = """p=(\d+),(\d+) v=(-?\d+),(-?\d+)""".toRegex()

            fun from(input: String): Robot = inputRegex.find(input)!!.destructured.let { (px, py, vx, vy) ->
                Robot(Vector2(px.toLong(), py.toLong()), Vector2(vx.toLong(), vy.toLong()))
            }
        }
    }

    private val TEST_BATHROOM_SIZE = Vector2(11L, 7L)
    private val BATHROOM_SIZE = Vector2(101L, 103L)

    override fun part1(input: Input): Long {
        val robots = input.map(Robot::from)
        val bathroomSize = BATHROOM_SIZE
        val seconds = 100L

        val finalRobotPositions = robots.map { robot ->
            (robot.position + robot.velocity * seconds).mod(bathroomSize)
        }

        val halfBathroomSize = bathroomSize / 2
        var (quad1, quad2, quad3, quad4) = listOf(0L, 0L, 0L, 0L)
        finalRobotPositions.forEach {
            when {
                it.x < halfBathroomSize.x && it.y < halfBathroomSize.y -> quad1++
                it.x > halfBathroomSize.x && it.y < halfBathroomSize.y -> quad2++
                it.x < halfBathroomSize.x && it.y > halfBathroomSize.y -> quad3++
                it.x > halfBathroomSize.x && it.y > halfBathroomSize.y -> quad4++
            }
        }

        List(bathroomSize.y.toInt()) { MutableList<String>(bathroomSize.x.toInt()) { "." } }
            .also { bathroom ->
                finalRobotPositions.forEach {
                    val pos = Position(it.y.toInt(), it.x.toInt())
                    bathroom[pos] = when (val string = bathroom[pos]) {
                        "." -> "1"
                        else -> (string.toInt() + 1).toString()
                    }
                }
            }
            .prettyToString2D()
            .also { kotlin.io.println(it) }
        println(listOf(quad1, quad2, quad3, quad4))

        return quad1 * quad2 * quad3 * quad4
    }

    override val part2Tests = listOf(
		Test(0, """
            
        """.trimIndent()),
	)

    override fun part2(input: Input): Int {
        val robots = input.map(Robot::from)
        val bathroomSize = BATHROOM_SIZE

//        while (true) {
//            var minEntropy = Float.MAX_VALUE to 0
//            repeat(10000) { seconds ->
        val seconds = 6577
                val finalRobotPositions = robots.map { robot ->
                    (robot.position + robot.velocity * seconds.toLong()).mod(bathroomSize)
                }

        List(bathroomSize.y.toInt()) { MutableList<String>(bathroomSize.x.toInt()) { "." } }
            .also { bathroom ->
                finalRobotPositions.forEach {
                    val pos = Position(it.y.toInt(), it.x.toInt())
                    bathroom[pos] = when (val string = bathroom[pos]) {
                        "." -> "1"
                        else -> (string.toInt() + 1).toString()
                    }
                }
            }
            .prettyToString2D()
            .also { kotlin.io.println(it) }

                val robotMap = finalRobotPositions.groupingBy { it }.eachCount()

                val entropy = robotMap.values.fold(0f) { acc, robots ->
                    acc + robots * log2(robots.toFloat())
                }

//                if (entropy < minEntropy.first) {
//                    minEntropy = entropy to seconds
//                }
//            }
//            println(minEntropy.second)
//        }

        return 0
    }

    @JvmStatic
    fun main(args: Array<String>) {
//        if (testPart1()) {
//            runPart1()
//        }

//        if (testPart2()) {
            runPart2()
//        }
    }
}
