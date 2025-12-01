package aoc2024

import AoCPuzzle
import jdk.javadoc.internal.doclets.formats.html.markup.HtmlStyle.index
import util.Input
import util.toPair
import java.util.random.RandomGeneratorFactory.all

object Day05 : AoCPuzzle() {
    override val part1Tests = listOf(
		Test(143, """
            47|53
            97|13
            97|61
            97|47
            75|29
            61|13
            75|53
            29|13
            97|29
            53|29
            61|53
            97|53
            61|29
            47|13
            75|47
            97|75
            47|61
            75|61
            47|29
            75|13
            53|13

            75,47,61,53,29
            97,61,53,29,13
            75,29,13
            75,97,47,61,53
            61,13,29
            97,13,75,29,47
        """.trimIndent()),
	)

    override fun part1(input: Input): Int {
        val rules = input.takeWhile(String::isNotBlank).map { it.split('|').map(String::toInt).toPair() }.groupBy(
            keySelector = { it.first },
            valueTransform = { it.second}
        )
        val updates = input.takeLastWhile(String::isNotBlank).map { it.split(',').map(String::toInt) }

        return updates.filter { update ->
            update.withIndex().all { (index, page) ->
                update.subList(0, index).all { otherPage ->
                    otherPage !in rules[page].orEmpty()
                }
            }
        }.sumOf {
            it[it.lastIndex / 2]
        }
    }

    override val part2Tests = listOf(
		Test(123, """
            47|53
            97|13
            97|61
            97|47
            75|29
            61|13
            75|53
            29|13
            97|29
            53|29
            61|53
            97|53
            61|29
            47|13
            75|47
            97|75
            47|61
            75|61
            47|29
            75|13
            53|13

            75,47,61,53,29
            97,61,53,29,13
            75,29,13
            75,97,47,61,53
            61,13,29
            97,13,75,29,47
        """.trimIndent()),
	)

    override fun part2(input: Input): Int {
        val rules = input.takeWhile(String::isNotBlank).map { it.split('|').map(String::toInt).toPair() }.groupBy(
            keySelector = { it.first },
            valueTransform = { it.second}
        )
        val updates = input.takeLastWhile(String::isNotBlank).map { it.split(',').map(String::toInt) }

        return updates.filterNot { update ->
            update.withIndex().all { (index, page) ->
                update.subList(0, index).all { otherPage ->
                    otherPage !in rules[page].orEmpty()
                }
            }
        }.map {
            it.sortedWith { a, b ->
                when {
                    a in rules[b].orEmpty() -> 1
                    b in rules[a].orEmpty() -> -1
                    else -> 0
                }
            }
        }.sumOf {
            it[it.lastIndex / 2]
        }
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
