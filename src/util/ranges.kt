package util

val Int.rangeInclusive: IntRange get() = IntRange(0, this)
val Int.rangeExclusive: IntRange get() = IntRange(0, this - 1)

val Long.rangeInclusive: LongRange get() = LongRange(0, this)
val Long.rangeExclusive: LongRange get() = LongRange(0, this - 1)

val IntRange.zeroOffset: Int get() = -first

infix fun IntRange.offset(offset: Int) = (first + offset)..(last + offset)
infix fun LongRange.offset(offset: Long) = (first + offset)..(last + offset)

infix fun LongRange.constrainWith(predicate: (Long) -> Boolean) = first(predicate)..reversed().first(predicate)

fun IntRange.coerceIn(min: Int, max: Int) = first.coerceAtLeast(min)..last.coerceAtMost(max)
fun IntRange.coerceIn(range: IntRange) = first.coerceAtLeast(range.first)..last.coerceAtMost(range.last)

fun IntRange.expandToContain(value: Int) = first.coerceAtMost(value)..last.coerceAtLeast(value)
fun IntRange.expandToContain(range: IntRange) = first.coerceAtMost(range.first)..last.coerceAtLeast(range.last)

val IntRange.lengthInclusive: Int get() = endInclusive - start + 1
val IntRange.lengthExclusive: Int get() = endInclusive - start

val LongRange.lengthInclusive: Long get() = endInclusive - start + 1
val LongRange.lengthExclusive: Long get() = endInclusive - start

fun <T : ClosedRange<Long>> T.chunked(chunkSize: Long): List<LongRange> = buildList {
    val length = endInclusive - start
    val chunks = length / chunkSize
    val remainder = length % chunkSize
    var current = start
    for (i in 0..<chunks) {
        val end = current + chunkSize + if (i < remainder) 1 else 0
        add(current..<end)
        current = end
    }
}

fun <T : ClosedRange<Long>> T.split(count: Long): List<LongRange> = chunked((endInclusive - start) / count)
