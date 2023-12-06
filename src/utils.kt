import io.github.cdimascio.dotenv.dotenv
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.cookie
import io.ktor.client.request.get
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
fun String.consume(action: (String, Char) -> String) {
    var remains = this
    while (remains.isNotEmpty()) {
        remains = action(remains, remains.first())
    }
}

fun <E> Collection<E>.consume(action: (Collection<E>, E) -> Collection<E>) {
    var remains = this
    while (remains.isNotEmpty()) {
        remains = action(remains, remains.first())
    }
}

fun String.consumeIndexed(action: (Int, String, Char) -> String) {
    val initialLength = length
    var remains = this
    while (remains.isNotEmpty()) {
        remains = action(initialLength - remains.length, remains, remains.first())
    }
}
