import io.github.cdimascio.dotenv.dotenv
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.cookie
import io.ktor.client.request.get
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn
import java.io.File
import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.time.measureTimedValue

val dotenv = dotenv()

fun <T> T.println() = also { println(it) }

fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray())).toString(16).padStart(32, '0')

fun readInput(name: String) = Path("resources/$name.txt").readLines()

fun downloadInput(
    day: Int = Clock.System.todayIn(TimeZone.currentSystemDefault()).dayOfMonth,
    year: Int = Clock.System.todayIn(TimeZone.currentSystemDefault()).year,
) {
    require(day in 1..25)
    require(year >= 2015)
    val targetDate = LocalDate(year, 12, day)
    require(targetDate <= Clock.System.todayIn(TimeZone.currentSystemDefault()))

    val text: String = runBlocking {
        HttpClient {
            expectSuccess = true
        }.get("https://adventofcode.com/$year/day/$day/input") {
            cookie("session", dotenv["session"])
        }.body()
    }

    File("resources/Day${"%02d".format(day)}.txt").writeText(text)
}

infix fun IntRange.offset(offset: Int) = (start + offset)..(endInclusive + offset)
infix fun LongRange.offset(offset: Long) = (start + offset)..(endInclusive + offset)

infix fun LongRange.constrainWith(predicate: (Long) -> Boolean) = first(predicate)..reversed().first(predicate)

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

fun String.consume(action: (String, Char) -> String) {
    var remains = this
    while (remains.any()) {
        remains = action(remains, remains.first())
    }
}

fun <T : Iterable<E>, E> T.consume(action: (T, E) -> T) {
    var remains = this
    while (remains.any()) {
        remains = action(remains, remains.first())
    }
}

fun String.consumeIndexed(action: (Int, String, Char) -> String) {
    val initialLength = length
    var remains = this
    while (remains.any()) {
        remains = action(initialLength - remains.length, remains, remains.first())
    }
}

// TODO: try to do automatic splitting
fun <T, R> Iterable<T>.mapParallel(transform: (T) -> R): List<R> =
    runBlocking { map { async(Dispatchers.Default) { transform(it) } }.awaitAll() }

fun <T> timed(block: () -> T): T = measureTimedValue(block).run {
    println("${value.toString().padEnd(20)} $duration")
    value
}
