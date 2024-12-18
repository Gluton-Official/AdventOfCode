package aoc2023

import AoCPuzzle
import util.Input
import util.offset
import kotlin.math.pow

object Day04 : AoCPuzzle() {
    override val part1Tests = listOf(
        Test(13, """
            Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
            Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19
            Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1
            Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83
            Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36
            Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11
        """.trimIndent()),
	)

    override fun part1(input: Input): Int =
        input.map(Card::invoke).sumOf { card ->
            2f.pow(card.wins - 1).toInt()
        }

    override val part2Tests = listOf(
        Test(30, """
            Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
            Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19
            Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1
            Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83
            Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36
            Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11
        """.trimIndent()),
	)

    override fun part2(input: Input): Int = input.map(Card::invoke)
        .associate { it.id to CardBucket(it) }.let { cards ->
            cards.entries.sumOf { (cardId, cardBucket) ->
                for (nextCardId in 1..cardBucket.card.wins offset cardId) {
                    cards[nextCardId]?.let { it.count += cardBucket.count } ?: break
                }
                cardBucket.count
            }
        }

    private data class CardBucket(val card: Card, var count: Int = 1)
    private data class Card(val id: Int, val winningNumbers: List<Int>, val yourNumbers: List<Int>) {
        val wins: Int by lazy {
            winningNumbers.intersect(yourNumbers.toSet()).count()
        }

        companion object {
            operator fun invoke(line: String): Card =
                line.split(':', '|').let { (cardName, winningNumbers, yourNumbers) ->
                    Card(
                        id = cardName.split(' ').last().toInt(),
                        winningNumbers = winningNumbers.trim().split(Regex("\\s+")).map(String::toInt),
                        yourNumbers = yourNumbers.trim().split(Regex("\\s+")).map(String::toInt)
                    )
                }
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
