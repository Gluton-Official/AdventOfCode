import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import java.math.BigInteger
import java.security.MessageDigest
import kotlin.time.measureTimedValue

fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16).padStart(32, '0')

val Int.rangeInclusive: IntRange get() = IntRange(0, this)
val Int.rangeExclusive: IntRange get() = IntRange(0, this - 1)

val Long.rangeInclusive: LongRange get() = LongRange(0, this)
val Long.rangeExclusive: LongRange get() = LongRange(0, this - 1)

infix fun IntRange.offset(offset: Int) = (start + offset)..(endInclusive + offset)
infix fun LongRange.offset(offset: Long) = (start + offset)..(endInclusive + offset)

infix fun LongRange.constrainWith(predicate: (Long) -> Boolean) = first(predicate)..reversed().first(predicate)

val IntRange.lengthInclusive: Int get() = endInclusive - start + 1
val IntRange.lengthExclusive: Int get() = endInclusive - start

val LongRange.lengthInclusive: Long get() = endInclusive - start + 1
val LongRange.lengthExclusive: Long get() = endInclusive - start

fun <T> List<T>.cyclicIterator(): Iterator<T> = asCyclicSequence().iterator()
fun <T> List<T>.asCyclicSequence(): Sequence<T> {
    var index = 0
    return generateSequence {
        index %= size
        get(index++)
    }
}

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

fun <T : CharSequence> T.consume(action: (T, Char) -> T) {
    var remains = this
    while (remains.any()) {
        remains = action(remains, remains.first())
    }
}
fun <T : Iterable<R>, R> T.consume(action: (T, R) -> T) {
    var remains = this
    while (remains.any()) {
        remains = action(remains, remains.first())
    }
}
fun <T : Iterable<R>, R, C> T.consumeTo(destination: C, action: C.(T, R) -> T): C {
    var remains = this
    while (remains.any()) {
        remains = destination.action(remains, remains.first())
    }
    return destination
}
fun <T : CharSequence> T.consumeIndexed(action: (Int, T, Char) -> T) {
    val initialLength = length
    var remains = this
    while (remains.any()) {
        remains = action(initialLength - remains.length, remains, remains.first())
    }
}
fun <T : List<R>, R> T.consumeIndexed(action: (Int, T, R) -> T) {
    val initialLength = size
    var remains = this
    while (remains.any()) {
        remains = action(initialLength - remains.size, remains, remains.first())
    }
}

fun <T, R> Iterable<T>.zipWith(other: R): List<Pair<T, R>> = map { it to other }
fun <T, R, V> Iterable<T>.zipWith(other: R, transform: (Pair<T, R>) -> V): List<V> = map { transform(it to other) }
fun <T, R, V> Iterable<T>.zipWithIndexed(other: R, transform: (Int, Pair<T, R>) -> V): List<V> =
    mapIndexed { index, it -> transform(index, it to other) }


// TODO: try to do automatic splitting
fun <T, R> Iterable<T>.mapParallel(transform: (T) -> R): List<R> =
    runBlocking { map { async(Dispatchers.Default) { transform(it) } }.awaitAll() }

fun <T> timed(block: () -> T): T = measureTimedValue(block).run {
    println("${value.toString().padEnd(20)} $duration")
    value
}
