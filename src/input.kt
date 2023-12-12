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
import kotlin.io.path.Path
import kotlin.io.path.readLines

typealias Input = List<String>

val dotenv = dotenv()

fun readInput(name: String): Input = Path("resources/$name.txt").readLines()

fun downloadInput(
    day: Int? = null,
    year: Int? = null,
) {
    val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
    val day = day ?: today.dayOfMonth
    val year = year ?: today.year
    require(day in 1..25)
    require(year >= 2015)

    val targetDate = LocalDate(year, 12, day)
    require(targetDate <= today) { "$targetDate has not yet occurred (today: $today)" }

    val text: String = runBlocking {
        HttpClient {
            expectSuccess = true
        }.get("https://adventofcode.com/$year/day/$day/input") {
            cookie("session", dotenv["session"])
        }.body()
    }

    File("resources/Day${"%02d".format(day)}.txt").writeText(text)
}
