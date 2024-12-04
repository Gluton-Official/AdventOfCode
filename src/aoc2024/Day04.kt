package aoc2024

import AoCPuzzle
import util.Input
import util.prettyToString2D

object Day04 : AoCPuzzle() {
    override val part1Tests = listOf(
        Test(
            18, """
            MMMSXXMASM
            MSAMXMSMSA
            AMXSXMAAMM
            MSAMASMSMX
            XMASAMXAMM
            XXAMMXXAMA
            SMSMSASXSS
            SAXAMASAAA
            MAMMMXMMMM
            MXMXAXMASX
        """.trimIndent()
        ),
	)

    override fun part1(input: Input): Int {
        var count = 0
        val mas = "MAS".toList()
        val up = 0 to -1
        val down = 0 to 1
        val left = -1 to 0
        val right = 1 to 0
        val upRight = 1 to -1
        val downRight = 1 to 1
        val upLeft = -1 to -1
        val downLeft = -1 to 1
        val dirs = listOf(up, down, left, right, upRight, downRight, upLeft, downLeft)
        input.forEachIndexed { curY, row ->
            row.forEachIndexed { curX, c ->
                if (c == 'X') {
                    dirs.forEach { (x, y) ->
                        val coords = List(3) { i ->
                            (curX + x * (i + 1)) to (curY + y * (i + 1))
                        }
                        val xmasPos = coords.zip(mas)
                        if (xmasPos.all { (pos, c) ->
                            val (x, y) = pos
                            input.getOrNull(y)?.getOrNull(x) == c
                        }) {
                            count++
                        }
                    }
                }
            }
        }
        return count
    }

    override val part2Tests = listOf(
        Test(9, """
            MMMSXXMASM
            MSAMXMSMSA
            AMXSXMAAMM
            MSAMASMSMX
            XMASAMXAMM
            XXAMMXXAMA
            SMSMSASXSS
            SAXAMASAAA
            MAMMMXMMMM
            MXMXAXMASX
        """.trimIndent()),
	)

    override fun part2(input: Input): Int {
        var count = 0
        val mas = "MAS".toList()
        val upRight = 1 to -1
        val downRight = 1 to 1
        val upLeft = -1 to -1
        val downLeft = -1 to 1
        val dirs = listOf(
            listOf(upRight, 0 to 0, downLeft),
            listOf(upLeft, 0 to 0, downRight),
        )
        input.forEachIndexed { curY, row ->
            row.forEachIndexed { curX, c ->
                if (c == 'A') {
                    val coords = dirs.map { dir ->
                        buildList {
                            add(dir.map { (x, y) ->
                                (curX + x) to (curY + y)
                            })
                            add(dir.reversed().map { (x, y) ->
                                (curX + x) to (curY + y)
                            })
                        }
                    }
                    val masPos = coords.map {
                        it.map {
                            it.zip(mas)
                        }
                    }
                    if (masPos.all {
                        it.any {
                            it.all { (pos, c) ->
                                val (x, y) = pos
                                input.getOrNull(y)?.getOrNull(x) == c
                            }
                        }
                    }) {
                        count++
                    }
                }
            }
        }
        return count
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
