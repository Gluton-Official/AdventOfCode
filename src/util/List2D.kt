package util

typealias List2D<T> = List<List<T>>
typealias Iterable2D<T> = Iterable<Iterable<T>>

val <T> List2D<T>.width: Int get() = rowOrNull(0)?.size ?: 0
val <T> List2D<T>.height: Int get() = size

operator fun <T> List2D<T>.get(row: Int, column: Int): T = this[row][column]
operator fun <T> List2D<T>.get(position: Position): T = get(position.row, position.column)
fun <T> List2D<T>.getOrNull(row: Int, column: Int): T? = getOrNull(row)?.getOrNull(column)
fun <T> List2D<T>.getOrNull(position: Position): T? = getOrNull(position.row, position.column)

operator fun <T> List<MutableList<T>>.set(row: Int, column: Int, value: T) { this[row][column] = value }
operator fun <T> List<MutableList<T>>.set(position: Position, value: T) { this[position.row, position.column] = value }
fun <T> List<MutableList<T>>.trySet(row: Int, column: Int, value: T): Boolean =
    getOrNull(row, column)?.also { this[row, column] = value } != null
fun <T> List<MutableList<T>>.trySet(position: Position, value: T): Boolean =
    getOrNull(position)?.also { this[position] = value } != null

fun <T> List<MutableList<T>>.update(row: Int, column: Int, action: (T) -> T) { this[row][column] = action(this[row][column]) }
fun <T> List<MutableList<T>>.update(position: Position, action: (T) -> T) { this[position] = action(this[position]) }

fun <T, R : Iterable<T>> List<R>.row(index: Int): R = this[index]
fun <T> Iterable<List<T>>.column(index: Int): List<T> = map { row -> row[index] }

fun <T, R : Iterable<T>> List<R>.rowOrNull(index: Int): R? = getOrNull(index)
fun <T> Iterable<List<T>>.columnOrNull(index: Int): List<T>? = map { row ->
    if (index in row.indices) row[index] else return null
}

fun <T, C : List<T>> MutableList<C>.addRow(value: C) = add(value)
fun <T> Iterable<MutableList<T>>.addColumn(value: Iterable<T>): List<Boolean> =
    zip(value) { row, columnElement ->
        row.add(columnElement)
    }

fun <T> List2D<T>.rows(): List2D<T> = this
fun <T> Collection<List<T>>.columns(): List2D<T> = List(firstOrNull()?.size ?: 0) { column ->
    map { row -> row[column] }
}

fun Collection<CharSequence>.columnStrings(): List<String> =
    first().indices.map { columnIndex ->
        buildString {
            this@columnStrings.forEach { row ->
                append(row[columnIndex])
            }
        }
    }

fun <T> Collection<List<T>>.transposed() = columns()
fun Collection<CharSequence>.transposedStrings() = columnStrings()

// TODO: use generics for iterable children
fun <T> Iterable2D<T>.forEach2D(action: (T) -> Unit) {
    forEach { row -> row.forEach(action) }
}
fun <T> Iterable2D<T>.forEach2DIndexed(action: Position.(T) -> Unit) {
    forEachIndexed { rowIndex, row ->
        row.forEachIndexed { columnIndex, it -> Position(rowIndex, columnIndex).action(it) }
    }
}

fun <T> Iterable2D<T>.onEach2D(action: (T) -> Unit) =
    onEach { row -> row.forEach(action) }

fun <T> Iterable2D<T>.find2D(action: (T) -> Boolean): T? = firstNotNullOfOrNull { row -> row.find(action) }
fun <T> Iterable2D<T>.find2DIndexed(action: Position.(T) -> Boolean): T? =
    firstNotNullOfOrNullIndexed { rowIndex, row ->
        row.findIndexed { columnIndex, it -> Position(rowIndex, columnIndex).action(it) }
    }

fun <T, R : Any> Iterable2D<T>.firstNotNullOf2D(action: (T) -> R?): R = firstNotNullOf { row -> row.firstNotNullOfOrNull(action) }
fun <T, R : Any> Iterable2D<T>.firstNotNullOf2DIndexed(action: Position.(T) -> R?): R =
    firstNotNullOfIndexed { rowIndex, row ->
        row.firstNotNullOfOrNullIndexed { columnIndex, it -> Position(rowIndex, columnIndex).action(it) }
    }

fun <T, R : Any> Iterable2D<T>.firstNotNullOfOrNull2D(action: (T) -> R?): R? = firstNotNullOfOrNull { row -> row.firstNotNullOfOrNull(action) }
fun <T, R : Any> Iterable2D<T>.firstNotNullOfOrNull2DIndexed(action: Position.(T) -> R?): R? =
    firstNotNullOfOrNullIndexed { rowIndex, row ->
        row.firstNotNullOfOrNullIndexed { columnIndex, it -> Position(rowIndex, columnIndex).action(it) }
    }

fun <T, R> Iterable2D<T>.fold2D(initial: R, operation: (R, T) -> R): R =
    fold(initial) { accumulator, row ->
        row.fold(accumulator, operation)
    }
fun <T, R> Iterable2D<T>.fold2DIndexed(initial: R, operation: Position.(R, T) -> R): R =
    foldIndexed(initial) { rowIndex, accumulator, row ->
        row.foldIndexed(accumulator) { columnIndex, accumulator, element ->
            Position(rowIndex, columnIndex).operation(accumulator, element)
        }
    }

fun <T> Iterable2D<T>.count2D(): Int = sumOf { it.count() }
fun <T> Iterable2D<T>.count2D(predicate: (T) -> Boolean): Int = sumOf { row -> row.count(predicate) }
fun <T> Iterable2D<T>.count2DIndexed(predicate: Position.(T) -> Boolean): Int =
    fold2DIndexed(0) { accumulator, element ->
        if (predicate(element)) accumulator + 1 else accumulator
    }

fun <T, R> Iterable2D<T>.map2D(transform: (T) -> R): List2D<R> = map { row -> row.map(transform) }
fun <R> Iterable<CharSequence>.mapStrings2D(transform: (Char) -> R): List2D<R> = map { row -> row.map(transform) }
fun <T, R> Iterable2D<T>.map2DIndexed(transform: Position.(T) -> R): List2D<R> = mapIndexed { rowIndex, row ->
    row.mapIndexed { columnIndex, element -> Position(rowIndex, columnIndex).transform(element) }
}
fun <R> Iterable<CharSequence>.mapStrings2DIndexed(transform: (Position, Char) -> R): List2D<R> =
    mapIndexed { rowIndex, row ->
        row.mapIndexed { columnIndex, char -> transform(Position(rowIndex, columnIndex), char) }
    }

fun <T, R> Iterable<T>.zip2D(vertical: Iterable<R>): List2D<Pair<T, R>> =
    vertical.map { rowElement -> map { it to rowElement } }
fun <T, R, V> Iterable<T>.zip2D(vertical: Iterable<R>, transform: Pair<T, R>.() -> V): List2D<V> =
    vertical.map { rowElement -> map { transform(it to rowElement) } }

fun <T> Iterable<T>.zipTo2D(): List2D<Pair<T, T>> =
    map { rowElement -> map { it to rowElement } }
fun <T, R> Iterable<T>.zipTo2D(transform: Pair<T, T>.() -> R): List2D<R> =
    map { rowElement -> map { transform(it to rowElement) } }

fun <T> Iterable2D<T>.prettyToString2D(): String = buildString {
    append('[')
    this@prettyToString2D.forEachIndexed { index, row ->
        if (index != 0) append(",\n ")
        append(row)
    }
    append(']')
}

fun <C : List<MutableList<T>>, T> C.setAllAs(positions: Iterable<Position>, value: T) = apply {
    positions.forEach { (row, column) -> this[row][column] = value }
}
fun <C : List<MutableList<T>>, T> C.setAllAs(positions: Iterable<Pair<Position, T>>) = apply {
    positions.forEach { (position, value) -> this[position.row][position.column] = value }
}
