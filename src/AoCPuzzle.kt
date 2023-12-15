import com.github.ajalt.mordant.rendering.TextAlign
import com.github.ajalt.mordant.rendering.TextColors.brightGreen
import com.github.ajalt.mordant.rendering.TextColors.brightRed
import com.github.ajalt.mordant.rendering.TextColors.cyan
import com.github.ajalt.mordant.rendering.TextColors.gray
import com.github.ajalt.mordant.rendering.TextColors.yellow
import com.github.ajalt.mordant.rendering.TextStyle
import com.github.ajalt.mordant.rendering.TextStyles.reset
import com.github.ajalt.mordant.rendering.TextStyles.bold
import com.github.ajalt.mordant.rendering.Whitespace
import com.github.ajalt.mordant.table.ColumnWidth
import com.github.ajalt.mordant.table.grid
import com.github.ajalt.mordant.table.horizontalLayout
import com.github.ajalt.mordant.terminal.Terminal
import com.github.ajalt.mordant.widgets.HorizontalRule
import com.github.ajalt.mordant.widgets.Padding
import util.Input
import util.downloadInput
import util.readInput
import java.io.File
import kotlin.time.measureTimedValue

abstract class AoCPuzzle {
    private val name = this::class.simpleName!!
    private val day = name.substringAfter("Day").toInt()
    private val input: Input by lazy {
        if (!File("resources/$name.txt").exists()) downloadInput(day)
        readInput(name)
    }

    protected open val part1Tests = listOf(Test())
    protected open val part2Tests = listOf(Test())

    protected open fun part1(input: Input): Number = 0
    protected open fun part2(input: Input): Number = 0

    protected fun testPart1() = part1Tests.testAll("Part 1 Test", ::part1)
    protected fun testPart2() = part2Tests.testAll("Part 2 Test", ::part2)

    private fun <T> List<Test>.testAll(name: String, action: (Input) -> T): Boolean =
        singleOrNull()?.render(name, action) ?: mapIndexed { index, test ->
            test.render(name + " ${index + 1}", action)
        }.all { it }

    protected fun runPart1() = render("Part 1", ::part1)
    protected fun runPart2() = render("Part 2", ::part2)

    class Test(val expected: Number = 0, val input: String = "") {
        val inputLines = input.lines()
    }

    private val terminal = Terminal()

    private fun <T> render(name: String, action: (Input) -> T): T = terminal.run {
        println(HorizontalRule(cyan(name), ruleCharacter = "═", ruleStyle = TextStyle(cyan)))
        renderTimed(bold.style) { action(input) }
    }

    private fun <T> Test.render(name: String, action: (Input) -> T): Boolean =
        with(terminal) {
            println(HorizontalRule(yellow(name), ruleStyle = TextStyle(yellow)))
            val result = action(inputLines)
            val passed = result == expected
            if (passed) {
                println((brightGreen + bold)("Passed"))
            } else {
                println(grid {
                    style = brightRed
                    column(0) {
                        width = ColumnWidth.Expand(1)
                    }
                    column(1) {
                        width = ColumnWidth.Expand(1)
                        align = TextAlign.RIGHT
                    }
                    column(2) {
                        width = ColumnWidth.Expand(2)
                    }
                    row {
                        cell(bold("Failed")) {
                            rowSpan = 2
                        }
                        cell("actual:")
                        cell(bold(result.toString()))
                    }
                    row {
                        cell("expected:")
                        cell(bold(expected.toString()))
                    }
                })
            }
            passed
        }

    protected fun <R> renderTimed(style: TextStyle = reset.style, action: () -> R): R {
        val (value, duration) = measureTimedValue(action)
        terminal.println(horizontalLayout {
            whitespace = Whitespace.PRE_WRAP
            column(0) {
                width = ColumnWidth.Expand()
            }
            column(1) {
                width = ColumnWidth.Auto
                padding = Padding { left = 1 }
                align = TextAlign.RIGHT
            }
            cell(style(value.toString()))
            cell(gray(duration.toString()))
        })
        return value
    }

    protected fun println(message: Any?, style: TextStyle = gray) = terminal.println(
        style(message.toString()),
        whitespace = Whitespace.PRE_WRAP
    )
    protected fun <T> T.println(style: TextStyle = gray): T = also { println(this, style) }
}
