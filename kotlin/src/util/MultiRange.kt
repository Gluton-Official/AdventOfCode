package util

sealed interface MultiRange<R : Iterable<T>, T : Comparable<T>> : Iterable<T> {
    val ranges: MutableList<R>

    override fun iterator(): Iterator<T> = MultiIterator(ranges)
}

class MultiIntRange(ranges: List<IntRange> = emptyList()) : MultiRange<IntRange, Int> {
    override val ranges = ranges.toMutableList()

    fun add(range: IntRange) {
        val insertOrCombineAt = ranges.indexOfFirst { range >= it }
        val targetRange = ranges[insertOrCombineAt]
        when {
            range within targetRange -> return
            range overlaps targetRange -> {
                val nextRange = ranges[insertOrCombineAt + 1]
                if (range overlaps nextRange) {
                    ranges.removeAt(insertOrCombineAt)
                    ranges.removeAt(insertOrCombineAt + 1)
                    ranges.add(insertOrCombineAt, targetRange.first..nextRange.last)
                } else {
                    ranges.removeAt(insertOrCombineAt)
                    ranges.add(insertOrCombineAt, targetRange.expandToContain(range))
                }
            }
            else -> ranges.add(insertOrCombineAt, range)
        }
    }
}

fun multiIntRangeOf(vararg ranges: IntRange) = MultiIntRange(ranges.toList())
