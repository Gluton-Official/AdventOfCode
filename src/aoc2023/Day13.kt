package aoc2023

import AoCPuzzle
import util.Input
import util.columnStrings
import util.consume

object Day13 : AoCPuzzle() {
    override val part1Tests = listOf(
        Test(405, """
            #.##..##.
            ..#.##.#.
            ##......#
            ##......#
            ..#.##.#.
            ..##..##.
            #.#.##.#.

            #...##..#
            #....#..#
            ..##..###
            #####.##.
            #####.##.
            ..##..###
            #....#..#
        """.trimIndent()),
	)

    override fun part1(input: Input): Int {
        val patterns = buildList {
            input.consume { input, _ ->
                val pattern = input.takeWhile { it.isNotBlank() }
                add(pattern)
                input.drop(pattern.size + 1)
            }
        }

        return patterns.sumOf { pattern ->
            val horizontalMirrorIndex = findMirrorIndex(pattern) ?: 0
            val verticalMirrorIndex = findMirrorIndex(pattern.columnStrings()) ?: 0
            verticalMirrorIndex + 100 * horizontalMirrorIndex
        }
    }

    override val part2Tests = listOf(
        Test(400, """
            #.##..##.
            ..#.##.#.
            ##......#
            ##......#
            ..#.##.#.
            ..##..##.
            #.#.##.#.

            #...##..#
            #....#..#
            ..##..###
            #####.##.
            #####.##.
            ..##..###
            #....#..#
        """.trimIndent()),
	)

    override fun part2(input: Input): Int {
        val patterns = buildList {
            input.consume { input, _ ->
                val pattern = input.takeWhile { it.isNotBlank() }
                add(pattern)
                input.drop(pattern.size + 1)
            }
        }

        return patterns.sumOf { pattern ->
            val horizontalMirrorIndex = findSmudgyMirrorIndex(pattern) ?: 0
            val verticalMirrorIndex = findSmudgyMirrorIndex(pattern.columnStrings()) ?: 0
            verticalMirrorIndex + 100 * horizontalMirrorIndex
        }
    }

    private fun findMirrorIndex(pattern: List<String>): Int? {
        val hashes = pattern.map(String::hashCode)
        for (index in 1..hashes.lastIndex) {
            val after = hashes.drop(index).asSequence()
            val before = hashes.asReversed().drop(hashes.size - index).asSequence()

            val mirroredPairs = before.zip(after)
            if (mirroredPairs.all { (first, second) -> first == second }) {
                return index
            }
        }
        return null
    }

    private fun findSmudgyMirrorIndex(pattern: List<String>): Int? {
        for (index in 1..pattern.lastIndex) {
            val after = pattern.drop(index)
            val before = pattern.asReversed().drop(pattern.size - index)

            val mirroredPairs = before.zip(after)
            val diffs = mirroredPairs.sumOf { (first, second) ->
                first.zip(second).count { (a, b) -> a != b }
            }
            if (diffs == 1) return index
        }
        return null
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
