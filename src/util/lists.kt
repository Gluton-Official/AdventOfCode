package util

import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking

fun <T> List<T>.cyclicIterator(): Iterator<T> = asCyclicSequence().iterator()
fun <T> List<T>.asCyclicSequence(): Sequence<T> {
    var index = 0
    return generateSequence {
        index %= size
        get(index++)
    }
}

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
fun <T, R> Iterable<T>.parallel(function: Iterable<T>.((T) -> Deferred<R>) -> List<Deferred<R>>, transform: (T) -> R): List<R> =
    runBlocking { function { async(Dispatchers.Default) { transform(it) } }.awaitAll() }

fun <T> Iterable<T>.uniquePairs(): List<Pair<T, T>> = with(toMutableList()) {
    this@uniquePairs.flatMap { zipWith(it).also { removeFirst() } }
}
fun <T, R> Iterable<T>.uniquePairs(transform: Pair<T, T>.() -> R): List<R> = with(toMutableList()) {
    this@uniquePairs.flatMap { zipWith(it, transform).also { removeFirst() } }
}
fun <T, R> Iterable<T>.uniquePairsIndexed(transform: Pair<T, T>.(Position) -> R): List<R> =
    with(toMutableList()) {
        this@uniquePairsIndexed.flatMapIndexed { rowIndex, rowElement ->
            zipWithIndexed(rowElement) { columnIndex, pair ->
                pair.transform(Position(rowIndex, columnIndex))
            }.also { removeFirst() }
        }
    }

fun Iterable<String>.prettyToString(): String = buildString {
    append('[')
    forEachIndexed { index, row ->
        if (index != 0) append(",\n ")
        append(row)
    }
    append(']')
}

fun <T> arrayDequeOf(vararg elements: T): ArrayDeque<T> = ArrayDeque(elements.toList())
