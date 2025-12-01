package util

internal class MultiIterator<T>(iterables: List<Iterable<T>>) : Iterator<T> {
    private val iterables: MutableList<Iterable<T>> = iterables.toMutableList()
    private var currentIterator: Iterator<T>? = null

    override fun hasNext(): Boolean = currentIterator?.hasNext()
        ?: iterables.removeFirstOrNull()?.run {
            currentIterator = iterator()
            currentIterator?.hasNext()
        } ?: false

    override fun next(): T = when {
        hasNext() -> currentIterator?.next()
        else -> null
    } ?: throw NoSuchElementException()
}
