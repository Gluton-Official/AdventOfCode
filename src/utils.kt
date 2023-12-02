import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.math.BigInteger
import java.net.HttpURLConnection
import java.net.URL
import java.security.MessageDigest
import java.util.Calendar
import kotlin.io.path.Path
import kotlin.io.path.readLines

fun readInput(name: String) = Path("resources/$name.txt").readLines()

fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

fun Any?.println() = also { println(it) }

fun fetchInput(day: Int? = null, year: Int? = null) {
    val (year, day) = Calendar.getInstance().run {
        (year ?: get(Calendar.YEAR)) to
        (day ?: get(Calendar.DAY_OF_MONTH))
    }
    val url = URL("https://adventofcode.com/$year/day/$day/input")
    (url.openConnection() as HttpURLConnection).apply {
        requestMethod = "GET"
        setRequestProperty("cookie", "session=53616c7465645f5fd6612317c323ec0ffcf499fe809b5f8eba0e913c51f9d964c79d71bb250fe8f3acc0f4fd51e97d94c58c0b209962b51298dcae0e978a1cea")
        connectTimeout = 5000
        readTimeout = 5000

        if (responseCode != 200) {
            error("Failed request: $responseCode $responseMessage")
        }

        BufferedReader(InputStreamReader(inputStream)).use {
            File("resources/Day${"%02d".format(day)}.txt").writeText(it.readText())
        }
    }.disconnect()
}
