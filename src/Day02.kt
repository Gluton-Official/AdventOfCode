
object Day02 : AoCPuzzle() {
    override val part1Tests = listOf(
		Test(8, """
            Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
            Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
            Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
            Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
            Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green
        """.trimIndent()),
	)

    override fun part1(input: Input): Int =
        input.map(Game::from).filter { game ->
            game.sets.all {
                it.red <= setLimit.red &&
                it.blue <= setLimit.blue &&
                it.green <= setLimit.green
            }
        }.sumOf(Game::id)

    private val setLimit = Set(
        red = 12,
        green = 13,
        blue = 14,
    )

    override val part2Tests = listOf(
		Test(2286, """
            Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
            Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
            Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
            Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
            Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green
        """.trimIndent()),
	)

    override fun part2(input: Input): Int =
        input.map(Game::from).sumOf { game ->
            game.sets.fold(Set()) { minSet, set -> minSet.apply {
                red = red.coerceAtLeast(set.red)
                blue = blue.coerceAtLeast(set.blue)
                green = green.coerceAtLeast(set.green)
            }}.run { red * green * blue }
        }

    data class Game(val id: Int, val sets: List<Set>) {
        companion object {
            fun from(string: String): Game {
                val (id, sets) = string.split(':').let { (idString, setsString) ->
                    idString.split(' ').last().toInt() to setsString.split(';')
                }
                return Game(id, sets.map { set ->
                    val setMap = set.split(',').map(String::trim).associate {
                        it.split(' ').let { (num, color) -> color to num.toInt() }
                    }
                    Set(setMap["red"] ?: 0, setMap["green"] ?: 0, setMap["blue"] ?: 0)
                })
            }
        }
    }

    data class Set(var red: Int = 0, var green: Int = 0, var blue: Int = 0)
    
    @JvmStatic
    fun main(args: Array<String>) {
        testPart1()
        runPart1()

        testPart2()
        runPart2()
    }
}
