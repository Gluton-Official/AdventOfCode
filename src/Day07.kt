
object Day07 : AoCPuzzle() {
    override val part1Test: Test
        get() = Test(6440, """
            32T3K 765
            T55J5 684
            KK677 28
            KTJJT 220
            QQQJA 483
        """.trimIndent())

    override fun part1(input: List<String>): Int = input.map {
        val (cards, bid) = it.split(' ')
        Hand(cards.toCharArray().map { enumValueOf(it.toString()) }, bid.toInt())
    }.sorted().foldIndexed(0) { rank, acc, hand -> acc + hand.bid * (rank + 1) }

    override val part2Test: Test
        get() = Test(5905, """
            32T3K 765
            T55J5 684
            KK677 28
            KTJJT 220
            QQQJA 483
        """.trimIndent())

    override fun part2(input: List<String>): Int = input.map {
        val (cards, bid) = it.split(' ')
        Hand(cards.toCharArray().map { enumValueOf(it.toString()) }, bid.toInt(), jokers = true)
    }.sorted().foldIndexed(0) { rank, acc, hand -> acc + hand.bid * (rank + 1) }

    private sealed class Hand private constructor(
        open var cards: List<Card>,
        open val bid: Int,
        open var jokers: Boolean,
        private val rank: Int
    ) : Comparable<Hand> {
        data class HighCard(override var cards: List<Card>, override val bid: Int, override var jokers: Boolean = false) : Hand(cards, bid, jokers, 1)
        data class OnePair(override var cards: List<Card>, override val bid: Int, override var jokers: Boolean = false) : Hand(cards, bid, jokers, 2)
        data class TwoPair(override var cards: List<Card>, override val bid: Int, override var jokers: Boolean = false) : Hand(cards, bid, jokers, 3)
        data class ThreeOfAKind(override var cards: List<Card>, override val bid: Int, override var jokers: Boolean = false) : Hand(cards, bid, jokers, 4)
        data class FullHouse(override var cards: List<Card>, override val bid: Int, override var jokers: Boolean = false) : Hand(cards, bid, jokers, 5)
        data class FourOfAKind(override var cards: List<Card>, override val bid: Int, override var jokers: Boolean = false) : Hand(cards, bid, jokers, 6)
        data class FiveOfAKind(override var cards: List<Card>, override val bid: Int, override var jokers: Boolean = false) : Hand(cards, bid, jokers, 7)

        override fun compareTo(other: Hand): Int {
            return when (val valueComparison = rank compareTo other.rank) {
                0 -> cards.zip(other.cards)
                    .firstOrNull { it.first != it.second }
                    ?.run {
                        if (jokers || other.jokers) first compareToWithJokers second
                        else first compareTo second
                    }
                    ?: 0
                else -> valueComparison
            }
        }

        companion object {
            operator fun invoke(cards: List<Card>, bid: Int, jokers: Boolean): Hand {
                require(cards.size == 5)
                return when {
                    !jokers || cards.none { it == Card.J } -> Hand(cards, bid)
                    else -> {
                        cards
                            .filterNot { it == Card.J }
                            .takeIf { it.isNotEmpty() }
                            ?.distinct()
                            ?.maxOf { wildCardValue ->
                                Hand(
                                    cards.toMutableList().apply {
                                        replaceAll { if (it == Card.J) wildCardValue else it }
                                    },
                                    bid,
                                )
                            }
                            ?.apply {
                                this.cards = cards
                                this.jokers = true
                            }
                            ?: FiveOfAKind(cards, bid, jokers = true)
                    }
                }
            }

            operator fun invoke(cards: List<Card>, bid: Int): Hand {
                val cardCounts = cards.groupingBy { it }.eachCount()
                return when (cardCounts.size) {
                    1 -> FiveOfAKind(cards, bid)
                    2 -> when (cardCounts.values.first()) {
                        1, 4 -> FourOfAKind(cards, bid)
                        else -> FullHouse(cards, bid)
                    }
                    3 -> when {
                        cardCounts.values.contains(3) -> ThreeOfAKind(cards, bid)
                        else -> TwoPair(cards, bid)
                    }
                    else -> when {
                        cardCounts.values.contains(2) -> OnePair(cards, bid)
                        else -> HighCard(cards, bid)
                    }
                }
            }
        }
    }

    private enum class Card {
        `2`, `3`, `4`, `5`, `6`, `7`, `8`, `9`, T, J, Q, K, A;

        infix fun compareToWithJokers(other: Card): Int {
            val thisOrdinal = if (this == J) -1 else ordinal
            val otherOrdinal = if (other == J) -1 else other.ordinal
            return thisOrdinal compareTo otherOrdinal
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        testPart1()
        runPart1()

        testPart2()
        runPart2()
    }
}
