@file:Suppress("NAME_SHADOWING")

package util

import com.github.ajalt.mordant.rendering.TextColors
import com.github.ajalt.mordant.rendering.TextColors.*
import com.github.ajalt.mordant.terminal.Terminal
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

private val dotenv = dotenv()

fun downloadInput(
    day: Int? = null,
    year: Int? = null,
    terminal: Terminal? = null,
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
        }.get("https://adventofcode.com/$year/day/$day/input".also {
            val msg = "Downloading $it..."
            terminal?.println(gray(msg)) ?: println(msg)
        }) {
            cookie("session", dotenv["session"])
        }.body()
    }

    File("resources/Year$year/Day${"%02d".format(day)}.txt".also {
        val msg = "Downloaded $it..."
        terminal?.println(gray(msg)) ?: println(msg)
    }).writeText(text)
}
