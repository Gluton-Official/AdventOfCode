package aoc2024

import AoCPuzzle
import util.Input
import util.Position
import util.astar
import util.getOrNull
import util.setAllAs

object Day18 : AoCPuzzle() {
    override val part1Tests = listOf(
		Test(22, """
            5,4
            4,2
            4,5
            3,0
            2,1
            6,3
            2,4
            1,5
            0,6
            3,3
            2,6
            5,1
        """.trimIndent()),
	)

    private const val size = 70

    override fun part1(input: Input): Int {
        val bytes = input.map { it.split(',').map(String::toInt).let { (x, y) -> Position(y, x) } }.take(1024)
        val memory = List(size + 1) { MutableList(size + 1) { '.' } }
        memory.setAllAs(bytes, '#')
        val path = memory.astar(
            Position(0, 0),
            Position(size, size),
            obstacles = listOf('#'),
        ) ?: error("pathfinding failed")
        memory.setAllAs(path, 'O')
        memory.joinToString("\n") { it.joinToString("") }.println()
        return path.size - 1
    }

    override val part2Tests = listOf(
		Test(21, """
            5,4
            4,2
            4,5
            3,0
            2,1
            6,3
            2,4
            1,5
            0,6
            3,3
            2,6
            5,1
            1,2
            5,5
            2,5
            6,5
            1,4
            0,4
            6,4
            1,1
            6,1
            1,0
            0,5
            1,6
            2,0
        """.trimIndent()),
	)

    override fun part2(input: Input): Int {
        val bytes = input.map { it.split(',').map(String::toInt).let { (x, y) -> Position(y, x) } }
        var bytesToUse = 1024
        do {
            val memory = List(size + 1) { MutableList(size + 1) { '.' } }
            memory.setAllAs(bytes.take(bytesToUse++), '#')
            val path = memory.astar(
                Position(0, 0),
                Position(size, size),
                obstacles = listOf('#'),
            )
        } while (path != null)
        val failByte = bytes[bytesToUse - 2]
        println("${failByte.column},${failByte.row}")
        val memory = List(size + 1) { MutableList(size + 1) { '.' } }
        memory.setAllAs(bytes.take(bytesToUse - 3), '#')
        val path = memory.astar(
            Position(0, 0),
            Position(size, size),
            obstacles = listOf('#'),
        )!!
        memory.setAllAs(path, 'O')
        kotlin.io.println(memory.joinToString("\n") { it.joinToString("") })
        return bytesToUse - 2
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
