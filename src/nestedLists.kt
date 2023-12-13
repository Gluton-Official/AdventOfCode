import kotlin.math.absoluteValue
import kotlin.collections.forEach as collectionsForEach
import kotlin.collections.forEachIndexed as collectionsForEachIndexed
import kotlin.collections.onEach as collectionsOnEach
import kotlin.collections.fold as collectionsFold
import kotlin.collections.foldIndexed as collectionsFoldIndexed
import kotlin.collections.map as collectionsMap
import kotlin.collections.mapTo as collectionsMapTo
import kotlin.collections.mapIndexed as collectionsMapIndexed
import kotlin.collections.flatMap as collectionsFlatMap
import kotlin.collections.flatMapIndexed as collectionsFlatMapIndexed

typealias List2D<T> = List<List<T>>
typealias Iterable2D<T> = Iterable<Iterable<T>>

operator fun <T> List2D<T>.get(row: Int, column: Int): T = this[row][column]
operator fun <T> List2D<T>.get(position: Position): T = get(position.row, position.column)
fun <T> List2D<T>.getOrNull(row: Int, column: Int): T? = getOrNull(row)?.getOrNull(column)
fun <T> List2D<T>.getOrNull(position: Position): T? = getOrNull(position.row, position.column)

operator fun <T> List<MutableList<T>>.set(row: Int, column: Int, value: T) { this[row][column] = value }
operator fun <T> List<MutableList<T>>.set(position: Position, value: T) { this[position.row][position.column] = value }

fun <T, R : Iterable<T>> List<R>.row(index: Int): R = this[index]
fun <T> Iterable<List<T>>.column(index: Int): List<T> = collectionsMap { row -> row[index] }

fun <T, R : Iterable<T>> List<R>.rowOrNull(index: Int): R? = getOrNull(index)
fun <T> Iterable<List<T>>.columnOrNull(index: Int): List<T>? = collectionsMap { row ->
    if (index in row.indices) row[index] else return null
}

fun <T, C : List<T>> MutableList<C>.addRow(value: C) = add(value)
fun <T> Iterable<MutableList<T>>.addColumn(value: Iterable<T>): List<Boolean> =
    zip(value) { row, columnElement ->
        row.add(columnElement)
    }

fun <T> List2D<T>.rows(): List2D<T> = this
fun <T> Collection<List<T>>.columns(): List2D<T> =
    indices.collectionsMap { columnIndex -> collectionsMap { row -> row[columnIndex] } }

fun Collection<CharSequence>.columnStrings(): List<String> =
    indices.collectionsMap { columnIndex ->
        buildString {
            this@columnStrings.forEach { row ->
                append(row[columnIndex])
            }
        }
    }

fun <T> List<List<T>>.transposed() = columns()

// TODO: use generics for iterable children
fun <T> Iterable2D<T>.forEach(action: (T) -> Unit) {
    collectionsForEach { row -> row.collectionsForEach(action) }
}
fun <T> Iterable2D<T>.forEachIndexed(action: Position.(T) -> Unit) {
    collectionsForEachIndexed { rowIndex, row ->
        row.collectionsForEachIndexed { columnIndex, it -> Position(rowIndex, columnIndex).action(it) }
    }
}

fun <T> Iterable2D<T>.onEach(action: (T) -> Unit) =
    collectionsOnEach { row -> row.forEach(action) }

fun <T, R> Iterable2D<T>.fold(initial: R, operation: (R, T) -> R): R =
    collectionsFold(initial) { accumulator, row ->
        row.collectionsFold(accumulator, operation)
    }
fun <T, R> Iterable2D<T>.foldIndexed(initial: R, operation: Position.(R, T) -> R): R =
    collectionsFoldIndexed(initial) { rowIndex, accumulator, row ->
        row.collectionsFoldIndexed(accumulator) { columnIndex, accumulator, element ->
            Position(rowIndex, columnIndex).operation(accumulator, element)
        }
    }

fun <T> Iterable2D<T>.count(): Int = sumOf { it.count() }
fun <T> Iterable2D<T>.count(predicate: (T) -> Boolean): Int = sumOf { row -> row.count(predicate) }
fun <T> Iterable2D<T>.countIndexed(predicate: Position.(T) -> Boolean): Int =
    foldIndexed(0) { accumulator, element ->
        if (predicate(element)) accumulator + 1 else accumulator
    }

fun <T, R> Iterable2D<T>.map(transform: (T) -> R): List2D<R> = collectionsMap { row -> row.collectionsMap(transform) }
fun <T, R> Iterable2D<T>.mapIndexed(transform: Position.(T) -> R): List2D<R> = collectionsMapIndexed { rowIndex, row ->
    row.collectionsMapIndexed { columnIndex, element -> Position(rowIndex, columnIndex).transform(element) }
}

fun <T, R> Iterable<T>.zip2D(vertical: Iterable<R>): List2D<Pair<T, R>> =
    vertical.collectionsMap { rowElement -> collectionsMap { it to rowElement } }
fun <T, R, V> Iterable<T>.zip2D(vertical: Iterable<R>, transform: Pair<T, R>.() -> V): List2D<V> =
    vertical.collectionsMap { rowElement -> collectionsMap { transform(it to rowElement) } }

fun <T> Iterable<T>.zipTo2D(): List2D<Pair<T, T>> =
    collectionsMap { rowElement -> collectionsMap { it to rowElement } }
fun <T, R> Iterable<T>.zipTo2D(transform: Pair<T, T>.() -> R): List2D<R> =
    collectionsMap { rowElement -> collectionsMap { transform(it to rowElement) } }

fun <T> Iterable<T>.uniquePairs(): List<Pair<T, T>> = with(toMutableList()) {
    this@uniquePairs.collectionsFlatMap { zipWith(it).also { removeFirst() } }
}
fun <T, R> Iterable<T>.uniquePairs(transform: Pair<T, T>.() -> R): List<R> = with(toMutableList()) {
    this@uniquePairs.collectionsFlatMap { zipWith(it, transform).also { removeFirst() } }
}
fun <T, R> Iterable<T>.uniquePairsIndexed(transform: Pair<T, T>.(Position) -> R): List<R> =
    with(toMutableList()) {
        this@uniquePairsIndexed.collectionsFlatMapIndexed { rowIndex, rowElement ->
            zipWithIndexed(rowElement) { columnIndex, pair ->
                pair.transform(Position(rowIndex, columnIndex))
            }.also { removeFirst() }
        }
    }

data class Position(val row: Int, val column: Int) {
    fun distance(other: Position): Float = (other.row - row) / (other.column - column).toFloat()
    fun manhattanDistance(other: Position): Int =
        (row - other.row).absoluteValue + (column - other.column).absoluteValue
}
